package com.bagbert.mtg.mtggoldfish;

import com.bagbert.commons.football.exec.JSoupFetcher;
import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.commons.football.tools.DateUtils;
import com.bagbert.commons.football.tools.JSoupUtils;
import com.bagbert.mtg.deckstats.DeckstatsListItem;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class GoldfishDeckListParserTest {

  @Test
  public void testParse() throws Exception {
    Document doc = JSoupUtils.getDocumentFromResource(this.getClass(), "mtggoldfish-decklist.htm",
        "UTF-8", "https://www.mtggoldfish.com/deck/custom?page=1");
    assertNotNull(doc);
    GoldfishDeckListParser parser = new GoldfishDeckListParser("Xantcha, Sleeper Agent");
    ResultSet<GoldfishListItem> rs = parser.parse(doc);
    assertNotNull(rs);
    assertEquals(DateUtils.toYYYYMMDD(new Date())+"-page-1", rs.getFilename());
    assertEquals("mtggoldfish/commander-decklists/xantcha-sleeper-agent", rs.getPath());

    assertFalse(rs.getResults().isEmpty());
    assertEquals(28, rs.getResults().size());

    List<GoldfishListItem> list = new ArrayList<>(rs.getResults());
    GoldfishListItem item = list.get(0);
    assertEquals("Xantcha, MTGO Crasher", item.getDeckName());
    assertEquals(1702972, item.getDeckId().intValue());
    assertEquals("SaffronOlive", item.getUserName());
    assertEquals("br", item.getColours());
    assertEquals("$", item.getCurrencySymbol());
    assertEquals(487.56, item.getPricePaper().doubleValue());
    assertEquals(65.13, item.getPriceOnline().doubleValue());
    assertEquals("/deck/1702972#paper", item.getDeckUrl());
    assertNotNull(item.getScrapeTime());

    item = list.get(27);
    assertEquals("Mass Discard EDH", item.getDeckName());
    assertEquals(1611052, item.getDeckId().intValue());
    assertEquals("Seth2285", item.getUserName());
    assertEquals("br", item.getColours());
    assertEquals("$", item.getCurrencySymbol());
    assertEquals(452.82, item.getPricePaper().doubleValue());
    assertEquals(34.79, item.getPriceOnline().doubleValue());
    assertEquals("/deck/1611052#paper", item.getDeckUrl());
    assertNotNull(item.getScrapeTime());
  }

  @Test
  public void testGetDeckId() {
    GoldfishDeckListParser parser = new GoldfishDeckListParser("card");
    assertEquals(1702972, parser.getDeckId("/deck/1702972#online").intValue());
    assertEquals(1702972, parser.getDeckId("/deck/1702972").intValue());
  }

  @Ignore
  @Test
  public void testFetch() {
    JSoupFetcher fetcher = new JSoupFetcher("https://www.mtggoldfish.com/deck/custom?page=1&utf8=?&mformat=commander&commander=Xantcha,%20Sleeper%20Agent&commit=Search#paper");
    Document doc = fetcher.fetch();
    System.out.println(doc.toString());
  }
}
