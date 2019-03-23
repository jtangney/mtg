package com.bagbert.mtg.mtggoldfish;

import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.commons.football.tools.DateUtils;
import com.bagbert.commons.football.tools.JSoupUtils;
import com.bagbert.mtg.deckstats.DeckstatsDeckCard;
import com.bagbert.mtg.deckstats.DeckstatsDeckParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class GoldfishDeckParserTest {

  @Test
  public void testParse() throws Exception {
    Document doc = JSoupUtils.getDocumentFromResource(this.getClass(), "mtggoldfish-deck.html",
        "UTF-8", "https://www.mtggoldfish.com/deck/1699860#paper");
    assertNotNull(doc);

    GoldfishDeckParser parser = new GoldfishDeckParser();
    ResultSet<GoldfishDeckCard> rs = parser.parse(doc);
    assertNotNull(rs);
    assertNotNull(rs.getResults());
    assertFalse(rs.getResults().isEmpty());
    assertEquals(81, rs.getResults().size());
    assertEquals(DateUtils.toYYYYMMDD(new Date())+"-1699860-dantcha-wanto-atak",
        rs.getFilename());

    List<GoldfishDeckCard> list = new ArrayList<>(rs.getResults());
    GoldfishDeckCard card = list.get(0);
    assertNotNull(card);
    assertEquals("Dantcha Wanto Atak", card.getDeckName());
    assertEquals(1699860, card.getDeckId().intValue());
    assertEquals("gof", card.getUserName());
    assertEquals("Commander", card.getFormat());
    assertEquals("Xantcha, Sleeper Agent", card.getArchetype());
    assertEquals(37.28d, card.getDeckPriceOnline().doubleValue(), 0.01);
    assertEquals(275.58, card.getDeckPricePaper().doubleValue(), 0.01);
    assertTrue(card.getCard().isCommander());
    assertEquals("Xantcha, Sleeper Agent", card.getCard().getCardName());
    assertEquals(1, card.getCard().getQty().intValue());
    assertEquals("1BR", card.getCard().getCastingCost());
    assertEquals("BR", card.getCard().getColourIdentity());
    assertEquals(3, card.getCard().getCmc().intValue());
    assertEquals(3.25d, card.getCard().getPrice().doubleValue(), 0.01);
    assertEquals("Creature", card.getCard().getType());

    card = list.get(list.size() - 1);
    assertNotNull(card);
    assertEquals("Dantcha Wanto Atak", card.getDeckName());
    assertEquals(1699860, card.getDeckId().intValue());
    assertEquals("gof", card.getUserName());
    assertEquals("Commander", card.getFormat());
    assertEquals("Xantcha, Sleeper Agent", card.getArchetype());
    assertEquals(37.28d, card.getDeckPriceOnline().doubleValue(), 0.01);
    assertEquals(275.58, card.getDeckPricePaper().doubleValue(), 0.01);
    assertFalse(card.getCard().isCommander());
    assertEquals("Temple of Malice", card.getCard().getCardName());
    assertEquals(1, card.getCard().getQty().intValue());
    assertNull(card.getCard().getCastingCost());
    assertNull(card.getCard().getColourIdentity());
    assertEquals(0, card.getCard().getCmc().intValue());
    assertEquals(5.50d, card.getCard().getPrice().doubleValue(), 0.01);
    assertEquals("Land", card.getCard().getType());
  }

  @Test
  public void testParse2() throws Exception {
    Document doc = JSoupUtils.getDocumentFromResource(this.getClass(), "mtggoldfish-deck-2.html",
        "UTF-8", "https://www.mtggoldfish.com/deck/1695465#paper");
    assertNotNull(doc);

    GoldfishDeckParser parser = new GoldfishDeckParser();
    ResultSet<GoldfishDeckCard> rs = parser.parse(doc);
    assertNotNull(rs);
  }

    @Test
  public void testToCmc() {
    GoldfishDeckParser parser = new GoldfishDeckParser();
    assertEquals(0, parser.toCmc(null).intValue());
    assertEquals(1, parser.toCmc("b").intValue());
    assertEquals(1, parser.toCmc("1").intValue());
    assertEquals(3, parser.toCmc("3").intValue());
    assertEquals(4, parser.toCmc("3r").intValue());
    assertEquals(4, parser.toCmc("2rb").intValue());
    assertEquals(4, parser.toCmc("rbgw").intValue());
  }

  @Test
  public void testGetCastingCost() {
    GoldfishDeckParser parser = new GoldfishDeckParser();
    String manacost = "<span class='manacost'><img class=\"common-manaCost-manaSymbol sprite-mana_symbols_3\" alt=\"3\" src=\"//assets1.mtggoldfish.com/assets/s-d69cbc552cfe8de4931deb191dd349a881ff4448ed3251571e0bacd0257519b1.gif\" /><img class=\"common-manaCost-manaSymbol sprite-mana_symbols_r\" alt=\"r\" src=\"//assets1.mtggoldfish.com/assets/s-d69cbc552cfe8de4931deb191dd349a881ff4448ed3251571e0bacd0257519b1.gif\" /></span>";
    Document doc = Jsoup.parse(manacost);
    assertEquals("3R", parser.getCastingCost(doc));

    manacost = "<span class='manacost'><img class=\"common-manaCost-manaSymbol sprite-mana_symbols_x\" alt=\"x\" src=\"//assets1.mtggoldfish.com/assets/s-d69cbc552cfe8de4931deb191dd349a881ff4448ed3251571e0bacd0257519b1.gif\" /><img class=\"common-manaCost-manaSymbol sprite-mana_symbols_b\" alt=\"b\" src=\"//assets1.mtggoldfish.com/assets/s-d69cbc552cfe8de4931deb191dd349a881ff4448ed3251571e0bacd0257519b1.gif\" /><img class=\"common-manaCost-manaSymbol sprite-mana_symbols_r\" alt=\"r\" src=\"//assets1.mtggoldfish.com/assets/s-d69cbc552cfe8de4931deb191dd349a881ff4448ed3251571e0bacd0257519b1.gif\" /></span>";
    doc = Jsoup.parse(manacost);
    assertEquals("XBR", parser.getCastingCost(doc));
  }

  @Test
  public void testToColourIdentity() {
    GoldfishDeckParser parser = new GoldfishDeckParser();
    assertNull(parser.toColourIdentity(null));
    assertEquals("R", parser.toColourIdentity("r"));
    assertEquals("R", parser.toColourIdentity("rr"));
    assertEquals("R", parser.toColourIdentity("2r"));
    assertEquals("BR", parser.toColourIdentity("br"));
    assertEquals("BR", parser.toColourIdentity("bbrr"));
    assertEquals("GW", parser.toColourIdentity("Xgw"));
  }

  @Test
  public void testDescSection() {
    GoldfishDeckParser parser = new GoldfishDeckParser();
    String section = "<div class='deck-view-description'>\n" + "User Submitted Deck\n" + "<br>\n" + "Format: Commander\n" + "<br>\n" + "Mar 05, 2019\n" + "<br>\n" + "Archetype: <a href=\"/archetype/72877\">Xantcha, Sleeper Agent</a>\n" + "\n" + "</div>";
    Document doc = Jsoup.parse(section);
    Element desc = doc.select("div.deck-view-description").first();
    Elements brs = desc.select("br");
    String format = brs.get(0).nextSibling().toString();
    assertEquals("Format: Commander", format.trim());
    String date = brs.get(1).nextSibling().toString();
    assertEquals("Mar 05, 2019", date.trim());

    // first bit missing "User submitting deck"
    section = "<div class='deck-view-description'>\n" + "Format: Commander\n" + "<br>\n" + "Mar 04, 2019\n" + "<br>\n" + "Archetype: <a href=\"/archetype/72877\">Xantcha, Sleeper Agent</a>\n" + "\n" + "</div>";
    doc = Jsoup.parse(section);
    desc = doc.select("div.deck-view-description").first();
    System.out.println(desc.childNode(0).toString());
    System.out.println(desc.childNode(1).toString());

  }
}
