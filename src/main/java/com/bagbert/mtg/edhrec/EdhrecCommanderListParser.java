package com.bagbert.mtg.edhrec;

import java.util.Set;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bagbert.commons.football.exec.Parser;
import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.mtg.MtgResultSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EdhrecCommanderListParser implements Parser<Document, EdhrecCommanderListItem> {

  @Override
  public ResultSet<EdhrecCommanderListItem> parse(Document input) throws Exception {
    String url = input.location();
    String colourCode = StringUtils.substringAfterLast(url, "/");
    String colourGroup = input.getElementsByTag("h2").first().text();

    // all the card details in a JSON obj inside a script tag
    Elements scripts = input.getElementsByTag("script");
    String json = null;
    for (Element script : scripts) {
      String text = script.data();
      if (text.startsWith("const json_dict")) {
        // remove trailing semi-colon and var initialiser
        json = StringUtils.substringBeforeLast(StringUtils.substringAfter(text, "="), ";").trim();
        break;
      }
    }
    JsonParser jsonParser = new JsonParser();
    JsonElement jsonTree = jsonParser.parse(json);
    if (jsonTree.isJsonObject()) {
      JsonObject jsonObj = jsonTree.getAsJsonObject();
      JsonArray cardlists = jsonObj.getAsJsonArray("cardlists");
      // first element is list of commanders; second is staple cards associated with that commander
      JsonObject commanders = cardlists.get(0).getAsJsonObject();
      String header = commanders.get("header").getAsString();
      String tag = commanders.get("header").getAsString();
      
      Set<EdhrecCommanderListItem> set = new ListOrderedSet<>();
      JsonArray cardviews = commanders.getAsJsonArray("cardviews");
      for (JsonElement element : cardviews) {
        JsonObject obj = element.getAsJsonObject();
        String commanderName = obj.get("name").getAsString();
        String relativeUrl = obj.get("url").getAsString();
        JsonArray cards = element.getAsJsonObject().get("cards").getAsJsonArray();
        obj = cards.get(0).getAsJsonObject();
        boolean isBanned = obj.get("is_banned").getAsBoolean();
        boolean isCommander = obj.get("is_commander").getAsBoolean();
        boolean isUnofficial = obj.get("is_unofficial").getAsBoolean();
        String image = obj.get("image").getAsString();
        set.add(new EdhrecCommanderListItem(commanderName, colourCode, colourGroup, relativeUrl,
            image, isCommander, isBanned, isUnofficial));
      }
      return new MtgResultSet<EdhrecCommanderListItem>("edhrec", set);
    }
    throw new RuntimeException("bad json");
  }

}
