package com.bagbert.mtg.deckstats;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.junit.Test;

import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.commons.football.tools.DateUtils;
import com.bagbert.commons.football.tools.JSoupUtils;

public class DeckstatsDeckListParserTest {

  @Test
  public void testParse() throws Exception {
    Document doc = JSoupUtils.getDocumentFromResource(this.getClass(), "deckstats-list.htm",
        "UTF-8", "https://deckstats.net/decks/f/edh-commander/?lng=en&page=1");
    assertNotNull(doc);
    DeckstatsDeckListParser parser = new DeckstatsDeckListParser();
    ResultSet<DeckstatsListItem> rs = parser.parse(doc);
    assertNotNull(rs);
    assertEquals(DateUtils.toYYYYMMDD(new Date())+"-page-1", rs.getFilename());
    assertEquals("deckstats/commander-decklists", rs.getPath());

    assertFalse(rs.getResults().isEmpty());
    assertEquals(20, rs.getResults().size());

    List<DeckstatsListItem> list = new ArrayList<>(rs.getResults());
    DeckstatsListItem item = list.get(0);
    assertEquals("DD", item.getDeckName());
    assertEquals(1181108, item.getDeckId().intValue());
    assertEquals("Batbabse666", item.getUserName());
    assertNull(item.getLikes());
    assertEquals(419, item.getPrice().intValue());
    assertEquals(12, item.getViews().intValue());

    item = list.get(list.size() - 1);
    assertEquals("/decks/92816/1070805-atogatogatogatog/en", item.getDeckUrl());
    assertEquals("Atogatogatogatog", item.getDeckName());
    assertEquals(1070805, item.getDeckId().intValue());
    assertEquals("hxxzr", item.getUserName());
    assertEquals(92816, item.getUserId().intValue());
    assertNull(item.getLikes());
    assertEquals(100, item.getPrice().intValue());
    assertEquals(169, item.getViews().intValue());

  }

  @Test
  public void testGetNumbersFromLink() {
    DeckstatsDeckListParser parser = new DeckstatsDeckListParser();
    assertEquals("12345", parser.getNumbersFromLink("/12345/"));
    assertEquals("12345", parser.getNumbersFromLink("https://deckstats.net/decks/12345/?lng=en"));
    assertNull(parser.getNumbersFromLink("https://deckstats.net/decks/?lng=en"));
  }

  @Test
  public void testGetPageNumber() {
    DeckstatsDeckListParser parser = new DeckstatsDeckListParser();
    assertEquals("1",
        parser.getPageNumber("https://deckstats.net/decks/f/edh-commander/?lng=en&page=1"));
    assertEquals("499",
        parser.getPageNumber("https://deckstats.net/decks/f/edh-commander/?lng=en&page=499"));
    assertEquals("", parser.getPageNumber("https://deckstats.net/decks/f/edh-commander/?lng=en"));
  }

  @Test
  public void testRetainOnlyDigits() {
    DeckstatsDeckListParser parser = new DeckstatsDeckListParser();
    assertEquals("1234", parser.retainOnlyDigits("$1,234"));
    assertEquals("234", parser.retainOnlyDigits("$234"));
  }

  @Test
  public void testToMillis() {
    DeckstatsDeckListParser parser = new DeckstatsDeckListParser();
    assertEquals(1000, parser.toMillisOffset(1, "seconds"));
    assertEquals(60000, parser.toMillisOffset(1, "minutes"));
    assertEquals(120000, parser.toMillisOffset(2, "minutes"));
    assertEquals(1 * 24 * 60 * 60 * 1000, parser.toMillisOffset(1, "days"));
    assertEquals(5 * 24 * 60 * 60 * 1000, parser.toMillisOffset(5, "days"));

    assertEquals(30L * 24 * 60 * 60 * 1000, parser.toMillisOffset(1, "month"));
    assertEquals(3 * 30L * 24 * 60 * 60 * 1000, parser.toMillisOffset(3, "month"));
    assertEquals(365L * 24 * 60 * 60 * 1000, parser.toMillisOffset(1, "year"));
  }

  @Test
  public void testParseDateUpdatedString() throws ParseException {
    DeckstatsDeckListParser parser = new DeckstatsDeckListParser();
    String pattern = "yyyyMMdd-HH:mm:ssXX";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    Date start = DateUtils.parseDate("20190120-15:30:15Z", pattern);
    assertEquals(sdf.parse("20190120-15:29:00Z"), parser.parseLastUpdated("1 minutes ago", start));
    assertEquals(sdf.parse("20190120-14:45:00Z"), parser.parseLastUpdated("45 minutes ago", start));
    assertEquals(sdf.parse("20190120-13:00:00Z"), parser.parseLastUpdated("2 hours ago", start));
    assertEquals(sdf.parse("20190110-00:00:00Z"), parser.parseLastUpdated("10 days ago", start));
    assertEquals(sdf.parse("20181121-00:00:00Z"), parser.parseLastUpdated("2 months ago", start));
  }
}
