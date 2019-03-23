package com.bagbert.mtg.mtggoldfish;

import com.bagbert.commons.football.exec.Parser;
import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.commons.football.tools.DateUtils;
import com.bagbert.mtg.Colour;
import com.bagbert.mtg.Constants;
import com.bagbert.mtg.MtgResultSet;
import com.bagbert.mtg.common.AbstractDeckParser;
import com.bagbert.mtg.utils.MtgUtils;
import com.google.common.flogger.FluentLogger;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class GoldfishDeckParser extends AbstractDeckParser
    implements Parser<Document, GoldfishDeckCard> {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public GoldfishDeckParser() {
    this(null);
  }

  public GoldfishDeckParser(String cardName) {
    super(Constants.SOURCE_GOLDFISH,"commander-decks", cardName);
  }

  @Override
  public ResultSet<GoldfishDeckCard> parse(Document document) throws Exception {
    Date now = new Date();
    String deckName = document.select("h2.deck-view-title").first().ownText();
    String[] urlElements = document.location().split("/");
    String deckIdStr = urlElements[urlElements.length - 1].split("#")[0];
    Integer deckId = Integer.valueOf(deckIdStr);
    String byUserName = document.select("span.deck-view-author").first().text();
    String userName = StringUtils.substringAfter(byUserName, "by ");
    Elements prices = document.select("div.price-box-price");
    Double priceOnline = toDouble(prices.get(0).text());
    Double pricePaper = toDouble(prices.get(1).text());

    Element descSection = document.select("div.deck-view-description").first();
    String deckFormat;
    String dateUploadedStr;
    if (descSection.childNode(0).toString().contains("Format:")) {
      deckFormat = descSection.childNode(0).toString();
      dateUploadedStr = descSection.childNode(2).toString();
    }
    else {
      deckFormat = descSection.childNode(2).toString();
      dateUploadedStr = descSection.childNode(4).toString();
    }
    deckFormat = StringUtils.substringAfter(deckFormat.trim(), "Format: ");
    Date dateUploaded = new SimpleDateFormat("MMM dd, yyyy").parse(dateUploadedStr.trim());
    String archetype = null;
    if (!descSection.select("a").isEmpty()) {
      archetype = descSection.select("a").first().text();
    }

    Element paperTab = document.select("div#tab-paper").first();
    Elements rows = paperTab.select("tr");
    String type = null;
    Set<GoldfishDeckCard> results = new ListOrderedSet<>();
    for (Element row : rows) {
      Elements cells = row.select("td");
      if (cells.first().className().contains("deck-header")) {
        type = getType(cells.first().text().trim());
        continue;
      }
      boolean isCommander = false;
      if ("commander".equalsIgnoreCase(type)) {
        isCommander = true;
        // for commander - set type to Creature (tho it could be e.g. Planeswalker)
        type = "Creature";
      }
      String cardName = cells.select("td.deck-col-card").first().text();
      // logger.atInfo().log("Processing card: %s", cardName);
      Integer qty = Integer.valueOf(cells.select("td.deck-col-qty").first().text());
      Double price = Double.valueOf(cells.select("td.deck-col-price").first().text());
      Element mana = cells.select("span.manacost").first();
      String  castingCost = getCastingCost(mana);
      Integer cmc = toCmc(castingCost);
      String colourIdentity = toColourIdentity(castingCost);

      GoldfishCard card = new GoldfishCard(cardName, qty, isCommander, castingCost, cmc, colourIdentity, type, price);
      GoldfishDeckCard deckCard = new GoldfishDeckCard(deckName, deckId, userName, dateUploaded, deckFormat,
          archetype, pricePaper, priceOnline, now, card);
      results.add(deckCard);
    }
    String path = super.buildPath();
    return new MtgResultSet<>(path, results, DateUtils.toYYYYMMDD(now), deckIdStr,
        MtgUtils.formatCardName(deckName));
  }

  String getType(String type) {
    String sub = StringUtils.substringBeforeLast(type, " ");
    if (sub.endsWith("s")) {
      return StringUtils.chop(sub);
    }
    return sub;
  }

  String getCastingCost(Element manacost) {
    if (manacost == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (Element img : manacost.select("img")) {
      sb.append(img.attr("alt"));
    }
    return sb.toString().toUpperCase();
  }

  /**
   * Inputs of the form "1br", "3wg" etc
   */
  Integer toCmc(String castingCost) {
    if (StringUtils.isEmpty(castingCost)) {
      return 0;
    }
    int count = 0;
    for (char c : castingCost.toCharArray()) {
      if (Character.isDigit(c)) {
        count += Character.getNumericValue(c);
        continue;
      }
      count++;
    }
    return count;
  }

  String toColourIdentity(String castingCost) {
    if (StringUtils.isEmpty(castingCost)) {
      return castingCost;
    }
    StringBuilder sb = new StringBuilder();
    for (char c : castingCost.toCharArray()) {
      String code = String.valueOf(c);
      if (Colour.isColourCode(code)) {
        if(sb.indexOf(code) < 0) {
          sb.append(code);
        }
      }
    }
    return sb.toString().toUpperCase();
  }


  List<String> descSectionParts(Element descSection) {
    return null;
  }
}

