package com.bagbert.mtg.deckstats;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bagbert.commons.football.tools.DateUtils;
import com.bagbert.mtg.Constants;
import com.bagbert.mtg.common.AbstractDeckParser;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bagbert.commons.football.exec.Parser;
import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.mtg.MtgResultSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DeckstatsDeckParser extends AbstractDeckParser
    implements Parser<Document, DeckstatsDeckCard> {

  private static final Logger LOG = Logger.getLogger(DeckstatsDeckParser.class.getName());
  private String dir;

  public DeckstatsDeckParser() {
    this(null);
  }

  public DeckstatsDeckParser(String commanderName) {
    super(Constants.SOURCE_DECKSTATS,"commander-decks", commanderName);
  }

  @Override
  public ResultSet<DeckstatsDeckCard> parse(Document input) throws Exception {
    String json = getDeckJson(input);
    if (json == null) {
      throw new RuntimeException("Failed to find deck json!!");
    }
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(json).getAsJsonObject();

    Integer userId, deckId, deckRevision;
    Date dateUpdated;
    // old format
    Map<String, String> metadata = getDeckMetadata(input);
    if (metadata != null && metadata.containsKey("deck_id")) {
      userId = Integer.valueOf(metadata.get("deck_owner_id"));
      deckId = Integer.valueOf(metadata.get("deck_id"));
      deckRevision = Integer.valueOf(metadata.get("deck_revision1"));
      long timestamp = Long.parseLong(metadata.get("deck_updated"));
      dateUpdated = Date.from(Instant.ofEpochSecond(timestamp));
    }
    // new format (from Jan 2021)
    else {
      userId = getInteger(root, "owner_id");
      deckId = getInteger(root, "saved_id");
      deckRevision = getInteger(root, "revision");
      long timestamp = getLong(root, "updated");
      dateUpdated = Date.from(Instant.ofEpochSecond(timestamp));
    }

    String url = input.location();
    String[] elements = url.split("/");
    String deckIdName = elements[5];

    String userNameTitle = input.select("div#deck_folder_subtitle").first().text();
    String suffix = StringUtils.substringAfter(userNameTitle, "in ");
    String userName = StringUtils.substringBefore(suffix, "'s Decks");

    String deckName = root.get("name").getAsString();
    Integer deckCountTotal = getInteger(root, "number_main");
    Integer deckCountNonBasic = getInteger(root, "number_main_nonbasic");
    Integer deckCountBasicLands = (deckCountTotal != null && deckCountNonBasic != null)
        ? deckCountTotal - deckCountNonBasic : null;
    Integer sideboardCountTotal = getInteger(root, "number_sideboard");

    JsonArray sections = root.get("sections").getAsJsonArray();
    List<DeckstatsCard> cards = new ArrayList<>();
    // add the main sections
    for (JsonElement section : sections) {
      addCards(section.getAsJsonObject(), "cards", cards);
    }
    // SKIPPING sideboard etc
    // add the sideboards and maybes
    // addCards(root, "sideboard", cards);
    // addCards(root, "maybeboard", cards);

    Date scrapeTime = new Date();
    Set<DeckstatsDeckCard> results = new ListOrderedSet<>();
    for (DeckstatsCard card : cards) {
      DeckstatsDeckCard deckCard = new DeckstatsDeckCard(deckName, deckId, userName, userId,
          dateUpdated, deckRevision, deckCountTotal, deckCountNonBasic, deckCountBasicLands, card, scrapeTime);
      results.add(deckCard);
    }

    String path = super.buildPath();
    return new MtgResultSet<>(path, results, DateUtils.toYYYYMMDD(scrapeTime),
        deckIdName, String.valueOf(deckRevision));
  }

  void addCards(JsonObject parent, String name, List<DeckstatsCard> results) {
    JsonArray cards = parent.get(name).getAsJsonArray();
    for (JsonElement cardElem : cards) {
      JsonObject jsonCard = cardElem.getAsJsonObject();
      DeckstatsCard card = parseCard(jsonCard);
      results.add(card);
    }
  }

  DeckstatsCard parseCard(JsonObject card) {
    try {
      String cardName = getString(card, "name");
      LOG.fine("Procesing card: " + cardName);
      Integer qty = getInteger(card, "amount");
      Boolean isValid = getBoolean(card, "valid");
      String comment = getString(card, "comment");
      Integer setId = getInteger(card, "set_id");
      String setString = getString(card, "set_string");
      // Override is an Obj! Just get the override_string instead
      String override = getString(card, "override_string");
      Boolean isCommander = getBoolean(card, "isCommander");
      Boolean isSideboard = getBoolean(card, "isSideboard");
      Boolean isMaybe = getBoolean(card, "isMaybe");
      Boolean isFoil = getBoolean(card, "isFoil");
      Boolean isCustomSet = getBoolean(card, "is_custom_set");

      if (!card.has("data") || !card.get("data").isJsonObject()) {
        return new DeckstatsCard(cardName, qty, isValid, comment, setId, setString, override,
            isCommander, isSideboard, isMaybe, isFoil, isCustomSet);
      }
      JsonObject data = card.get("data").getAsJsonObject();
      String castingCost = getString(data, "cost");
      Integer cmc = getInteger(data, "cc", "converted_cost");
      Integer colourIdentity = getInteger(data, "color_identity");
      String type = getString(data, "type");
      String superType = getString(data, "supertype");
      String rarity = getString(data, "rarity");
      Integer displaySetId = getInteger(data, "display_set_id");
      Double priceMiracle = getDouble(data, "price_miracle");
      Double priceMkm = getDouble(data, "price_mkm");
      Double priceTcgPlayer = getDouble(data, "price_tcgplayer");
      Double priceCardHoarder = getDouble(data, "price_cardhoarder");
      Double priceCardKingdom = getDouble(data, "price_cardkingdom");

      return new DeckstatsCard(cardName, qty, isValid, comment, setId, setString, override,
          isCommander, isSideboard, isMaybe, isFoil, isCustomSet, castingCost, cmc, colourIdentity,
          type, superType, rarity, displaySetId, priceMiracle, priceMkm, priceTcgPlayer,
          priceCardHoarder, priceCardKingdom);
    }
    catch (Exception e) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      LOG.warning("Error parsing card: " + gson.toJson(card));
      throw new RuntimeException("Error parsing card " + getString(card, "name"), e);
    }
  }

  String getString(JsonObject obj, String field) {
    if (!hasField(obj, field)) {
      return null;
    }
    return obj.get(field).getAsString();
  }

  Boolean getBoolean(JsonObject obj, String field) {
    if (!hasField(obj, field)) {
      return null;
    }
    return obj.get(field).getAsBoolean();
  }

  Double getDouble(JsonObject obj, String field) {
    if (!hasField(obj, field)) {
      return null;
    }
    return obj.get(field).getAsDouble();
  }

  Integer getInteger(JsonObject obj, String field1, String field2) {
    if (hasField(obj, field1)) {
      return obj.get(field1).getAsInt();
    }
    if (hasField(obj, field2)) {
      return obj.get(field2).getAsInt();
    }
    return null;
  }

  Integer getInteger(JsonObject obj, String field) {
    if (!hasField(obj, field)) {
      return null;
    }
    return obj.get(field).getAsInt();
  }

  Long getLong(JsonObject obj, String field) {
    if (!hasField(obj, field)) {
      return null;
    }
    return obj.get(field).getAsLong();
  }

  boolean hasField(JsonObject obj, String field) {
    return obj.has(field) && !obj.get(field).isJsonNull();
  }

  Map<String, String> getDeckMetadata(Document input) {
    Elements scripts = input.select("script");
    for (Element script : scripts) {
      if (script.data().contains("edit_deck_id")) {
        return toMap(script.data());
      }
    }
    return null;
  }

  Map<String, String> toMap(String data) {
    Map<String, String> map = new HashMap<>();
    String trimmed = data.replaceAll("[\\n\\r]", "");
    String[] elements = trimmed.split(";");
    for (String element : elements) {
      if (!element.contains("=")) {
        continue;
      }
      element = com.bagbert.commons.football.tools.StringUtils.extTrim(element);
      String[] pair = element.split("=");
      map.put(pair[0].trim(), pair[1].trim());
    }
    return map;
  }

  String getDeckJson(Document input) {
    Elements scripts = input.select("script");
    for (Element script : scripts) {
      // old format
      if (script.data().contains("deck_json =")) {
        return extractJson(script.data());
      }
      // new format (from Jan2021, tho there was a big gap!)
      if (script.data().contains("init_deck_data")) {
        Matcher matcher = Pattern.compile("init_deck_data\\((\\{.*?\\})\\)").matcher(script.data());
        if (matcher.find()) {
          return matcher.group(1);
        }
        return null;
      }
    }
    return null;
  }

  String extractJson(String fullScript) {
    Matcher matcher = Pattern.compile("deck_json = (\\{.*\\});").matcher(fullScript);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }
}
