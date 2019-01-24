package com.bagbert.mtg.deckstats;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.commons.football.tools.JSoupUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DeckstatsDeckParserTest {

  @Test
  public void testParse() throws Exception {
    Document doc = JSoupUtils.getDocumentFromResource(this.getClass(), "deckstats-deck-1.htm",
        "UTF-8", "https://deckstats.net/decks/102796/1147701-knights-of-the-pentagram-table/en");
    assertNotNull(doc);

    DeckstatsDeckParser parser = new DeckstatsDeckParser();
    String json = parser.getDeckJson(doc);
    assertNotNull(json);

    ResultSet<DeckstatsDeckCard> rs = parser.parse(doc);
    assertNotNull(rs);
    assertNotNull(rs.getResults());
    assertFalse(rs.getResults().isEmpty());
    // assertEquals(20, rs.getResults().size());
    assertEquals("1147701-knights-of-the-pentagram-table-17", rs.getFilename());

    List<DeckstatsDeckCard> list = new ArrayList<>(rs.getResults());
    DeckstatsDeckCard card = list.get(0);
    assertNotNull(card);
    assertEquals("Knights of the Pentagram Table", card.getDeckName());
    assertEquals(102796, card.getUserId().intValue());
    assertEquals("kessukoofah", card.getUserName());
    assertEquals(1147701, card.getDeckId().intValue());
    assertEquals(100, card.getCardCountTotal().intValue());
    assertEquals(70, card.getCardCountNonBasic().intValue());
    assertEquals(30, card.getCardCountBasicLands().intValue());

    assertEquals("Korlash, Heir to Blackblade", card.getCardName());
    assertTrue(card.isCommander());
    assertEquals(1, card.getQty().intValue());
    assertEquals(4, card.getCmc().intValue());
    assertEquals("Legendary Creature - Zombie Warrior", card.getType());
    assertEquals("Legendary Creature", card.getSuperType());
    assertTrue(card.isValid());
    assertFalse(card.isSideboard());
    assertFalse(card.isMaybe());
    assertFalse(card.isFoil());

  }

  @Test
  public void testParseFullCard() throws IOException {
    DeckstatsDeckParser parser = new DeckstatsDeckParser();
    String jsonStr = IOUtils.toString(this.getClass().getResourceAsStream("deckstats-card.json"),
        "UTF-8");
    JsonParser jsonParser = new JsonParser();
    JsonObject jsonCard = jsonParser.parse(jsonStr).getAsJsonObject();

    DeckstatsCard card = parser.parseCard(jsonCard);
    assertNotNull(card);
    assertNull(card.getPriceMiracle());
  }

  // this card was erroring?
  @Test
  public void testParseCardMissingFields() throws IOException {
    DeckstatsDeckParser parser = new DeckstatsDeckParser();
    String jsonStr = IOUtils
        .toString(this.getClass().getResourceAsStream("deckstats-card-noRarity.json"), "UTF-8");
    JsonParser jsonParser = new JsonParser();
    JsonObject jsonCard = jsonParser.parse(jsonStr).getAsJsonObject();

    DeckstatsCard card = parser.parseCard(jsonCard);
    assertNotNull(card);
    assertNull(card.getRarity());
  }

  @Test
  public void testMetadataToMap() throws IOException {
    String metadata = IOUtils
        .toString(this.getClass().getResourceAsStream("deckstats-deck-metadata.txt"), "UTF-8");
    assertNotNull(metadata);
    Map<String, String> map = new DeckstatsDeckParser().toMap(metadata);
    assertNotNull(map);
    assertFalse(map.isEmpty());
    assertTrue(map.containsKey("edit_deck_id"));
    assertEquals("1124983", map.get("edit_deck_id"));
    assertEquals("2", map.get("deck_revision1"));
    assertEquals("1540908616", map.get("deck_updated"));
  }
}
