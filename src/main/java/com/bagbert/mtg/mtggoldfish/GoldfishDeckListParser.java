package com.bagbert.mtg.mtggoldfish;

import com.bagbert.commons.football.exec.Parser;
import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.commons.football.tools.DateUtils;
import com.bagbert.mtg.Constants;
import com.bagbert.mtg.MtgResultSet;
import com.bagbert.mtg.common.AbstractDeckParser;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.Set;

public class GoldfishDeckListParser extends AbstractDeckParser
    implements Parser<Document, GoldfishListItem> {

  public GoldfishDeckListParser(String cardName) {
    super(Constants.SOURCE_GOLDFISH, "commander-decklists", cardName);
  }

  @Override
  public ResultSet<GoldfishListItem> parse(Document document) throws Exception {
    Elements tiles = document.select("div.deck-tile");
    Date now = new Date();
    Set<GoldfishListItem> items = new ListOrderedSet<>();
    for (Element tile : tiles) {
      Element heading = tile.select("h2").first();
      Element link = heading.select("a").last();
      String deckName = link.text();
      String href = link.attr("href");
      Integer deckId = getDeckId(href);
      String byName = tile.select("div.deck-tile-author").first().ownText();
      String userName = StringUtils.substringAfter(byName, "by ").trim();
      Element manacost = tile.select("span.manacost").first();
      String colours = "";
      for (Element img : manacost.select("img")) {
        colours += img.attr("alt");
      }
      Element deckPrice = tile.select("div.deck-price").first();
      String currencySymbol = deckPrice.ownText();
      Double priceOnline = getPrice(deckPrice,"span.deck-price-online");
      Double pricePaper = getPrice(deckPrice,"span.deck-price-paper");
      items.add(new GoldfishListItem(deckId, deckName, userName, colours, currencySymbol, priceOnline, pricePaper,
          href, now));
    }

    String pageNumber = this.getPageNumber(document.location());
    String path = buildPath();
    String[] fileNameTokens = new String[] { DateUtils.toYYYYMMDD(new Date()), "page-" + pageNumber };
    return new MtgResultSet<>(path, items, fileNameTokens);
  }

  Integer getDeckId(String deckLink) {
    String idType = deckLink.split("/")[2];
    String id =  idType.split("#")[0];
    return Integer.parseInt(id);
  }

  Double getPrice(Element element, String selector) {
    Element priceOnline = element.select(selector).first();
    if (priceOnline == null) {
      return null;
    }
    try {
      return Double.parseDouble(priceOnline.ownText());
    }
    catch(Exception e) {
      return null;
    }
  }
}
