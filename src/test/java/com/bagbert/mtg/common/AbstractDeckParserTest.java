package com.bagbert.mtg.common;

import com.bagbert.mtg.deckstats.DeckstatsDeckListParser;
import com.bagbert.mtg.mtggoldfish.GoldfishDeckParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AbstractDeckParserTest {

  @Test
  public void testToDouble() {
    AbstractDeckParser parser = new GoldfishDeckParser();
    assertNull(parser.toDouble(null));
    assertEquals(Double.valueOf(1), parser.toDouble("1.0"), 0.01);
    assertEquals(Double.valueOf(1000), parser.toDouble("1,000"), 0.01);
    assertEquals(Double.valueOf(123.45), parser.toDouble("123.45"), 0.01);
  }

  @Test
  public void testGetPageNumber() {
    AbstractDeckParser parser = new DeckstatsDeckListParser();
    assertEquals("1",
        parser.getPageNumber("https://deckstats.net/decks/f/edh-commander/?lng=en&page=1"));
    assertEquals("499",
        parser.getPageNumber("https://deckstats.net/decks/f/edh-commander/?lng=en&page=499"));
    assertEquals("", parser.getPageNumber("https://deckstats.net/decks/f/edh-commander/?lng=en"));
  }


}
