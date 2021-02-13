package com.bagbert.mtg.deckstats;

import org.junit.Test;

import static org.junit.Assert.*;

public class DeckstatsDeckListServletTest {

  @Test
  public void testBuildUrl() {
    String url = DeckstatsDeckListServlet.buildUrl(null, null, 1);
    assertNotNull(url);
    assertFalse(url.contains("search_cards[]"));
    assertFalse(url.contains("search_cards_commander[]"));
    assertTrue(url.contains("page=1"));

    url = DeckstatsDeckListServlet.buildUrl("myCmdr", null, 5);
    assertNotNull(url);
    assertFalse(url.contains("search_cards[]"));
    assertTrue(url.contains("search_cards_commander[]=myCmdr"));
    assertTrue(url.contains("page=5"));

    url = DeckstatsDeckListServlet.buildUrl("myCmdr", "someCard", 5);
    assertNotNull(url);
    assertTrue(url.contains("search_cards[]=someCard"));
    assertTrue(url.contains("search_cards_commander[]=myCmdr"));
    assertTrue(url.contains("page=5"));
  }
}
