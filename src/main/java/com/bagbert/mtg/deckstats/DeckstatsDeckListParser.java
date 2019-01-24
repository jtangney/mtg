package com.bagbert.mtg.deckstats;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bagbert.commons.football.exec.Parser;
import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.mtg.MtgResultSet;

public class DeckstatsDeckListParser implements Parser<Document, DeckstatsListItem> {

  private String dir;

  public DeckstatsDeckListParser(String dir) {
    this.dir = dir;
  }

  public DeckstatsDeckListParser() {
    this("commander-decklists");
  }

  @Override
  public ResultSet<DeckstatsListItem> parse(Document input) throws Exception {
    String pageNumber = this.getPageNumber(input.location());
    Element listTable = input.select("table.decks_list").first();
    Elements rows = listTable.select("tr.touch_row");
    Set<DeckstatsListItem> items = new ListOrderedSet<>();
    for (Element row : rows) {
      String deckId = row.attr("data-deck-id");
      Elements cells = row.select("td");
      String deckUrl = cells.get(1).select("a").attr("href");
      String deckPath = StringUtils.substringAfter(deckUrl, "deckstats.net");
      String deckName = cells.get(1).text();
      String lastUpdatedStr = cells.get(2).ownText();
      Date lastUpdated = this.parseLastUpdated(lastUpdatedStr);
      String userName = cells.get(3).text();
      String userId = extractUserId(cells.get(3));
      String likes = cells.get(4).ownText();
      String price = cells.get(5).ownText();
      String views = cells.get(6).ownText();
      items.add(toListItem(deckId, deckName, userId, userName, likes, price, views, deckPath,
          lastUpdated));
    }
    String path = String.format("%s/%s", "deckstats", dir);
    String[] fileNameTokens = new String[] { "deckstats", dir, "page-" + pageNumber };
    return new MtgResultSet<>(path, items, fileNameTokens);
  }

  public DeckstatsListItem toListItem(String deckId, String deckName, String userId,
      String userName, String likes, String price, String views, String deckUrl, Date updated) {
    Integer dId = safeParseInt(deckId);
    Integer uId = safeParseInt(userId);
    Integer l = safeParseInt(retainOnlyDigits(likes));
    Integer v = safeParseInt(retainOnlyDigits(views));
    Integer p = safeParseInt(retainOnlyDigits(price));
    return new DeckstatsListItem(dId, deckName, uId, userName, l, p, v, deckUrl, updated);
  }

  private Integer safeParseInt(String input) {
    if (StringUtils.isEmpty(input)) {
      return null;
    }
    return Integer.parseInt(input);
  }

  String getPageNumber(String url) {
    Matcher matcher = Pattern.compile("page=(\\d+)").matcher(url);
    if (!matcher.find()) {
      return "";
    }
    return matcher.group(1);
  }

  String retainOnlyDigits(String input) {
    if (StringUtils.isEmpty(input)) {
      return null;
    }
    return input.replaceAll("\\D", "");
  }

  String extractUserId(Element userCell) {
    Element linkElement = userCell.select("a").first();
    if (linkElement == null) {
      return null;
    }
    String linkText = linkElement.attr("href");
    return getNumbersFromLink(linkText);
  }

  String getNumbersFromLink(String linkText) {
    Matcher matcher = Pattern.compile("\\/(\\d+)\\/").matcher(linkText);
    if (!matcher.find()) {
      return null;
    }
    return matcher.group(1);
  }

  /**
   * Inputs of the form "12 minutes ago", "3 months ago" etc.
   */
  Date parseLastUpdated(String str) {
    return parseLastUpdated(str, new Date());
  }

  Date parseLastUpdated(String str, Date input) {
    try {
      String[] elements = str.split(" ");
      if (elements.length != 3) {
        return null;
      }
      int qty = Integer.parseInt(elements[0]);
      long offset = toMillisOffset(qty, elements[1]);
      Date date = new Date(input.getTime() - offset);
      Date truncated = truncateDate(date, elements[1]);
      return truncated;
    }
    catch (Exception e) {
      return null;
    }
  }

  private Date truncateDate(Date date, String unitName) {
    if (StringUtils.startsWithIgnoreCase(unitName, "second")) {
      return DateUtils.truncate(date, Calendar.SECOND);
    }
    if (StringUtils.startsWithIgnoreCase(unitName, "minute")) {
      return DateUtils.truncate(date, Calendar.MINUTE);
    }
    if (StringUtils.startsWithIgnoreCase(unitName, "hour")) {
      return DateUtils.truncate(date, Calendar.HOUR_OF_DAY);
    }
    if (StringUtils.startsWithIgnoreCase(unitName, "day")
        || StringUtils.startsWithIgnoreCase(unitName, "month")) {
      return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
    }
    if (StringUtils.startsWithIgnoreCase(unitName, "year")) {
      return DateUtils.truncate(date, Calendar.MONTH);
    }
    return DateUtils.truncate(date, Calendar.MINUTE);
  }

  long toMillisOffset(int qty, String unitName) {
    if (StringUtils.startsWithIgnoreCase(unitName, "month")) {
      return qty * TimeUnit.DAYS.toMillis(30);
    }
    if (StringUtils.startsWithIgnoreCase(unitName, "year")) {
      return qty * TimeUnit.DAYS.toMillis(365);
    }
    TimeUnit unit = TimeUnit.valueOf(unitName.toUpperCase());
    return unit.toMillis(qty);
  }

}
