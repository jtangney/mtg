package com.bagbert.mtg.edhrec;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.junit.Test;

import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.commons.football.tools.JSoupUtils;

public class EdhrecCommanderListParserTest {

  @Test
  public void test() throws Exception {
    Document doc = JSoupUtils.getDocumentFromResource(this.getClass(), "edhrec-commander-list.htm",
        "UTF-8", "https://edhrec.com/commanders/br");
    assertNotNull(doc);
    ResultSet<EdhrecCommanderListItem> rs = new EdhrecCommanderListParser().parse(doc);
    assertNotNull(rs);
    assertNotNull(rs.getResults());
    assertFalse(rs.getResults().isEmpty());

    EdhrecCommanderListItem[] results = rs.getResults().toArray(new EdhrecCommanderListItem[rs.getResults().size()]);
    assertEquals("Rakdos, Lord of Riots", results[0].getName());
    assertEquals("br", results[0].getColourCode());
    assertEquals("Rakdos", results[0].getColourGroup());
    assertEquals("/commanders/rakdos-lord-of-riots", results[0].getRelativeUrl());
    assertFalse(results[0].getImage().isEmpty());
    assertTrue(results[0].isCommander());
    assertFalse(results[0].isBanned());

    assertEquals("Mogis, God of Slaughter", results[2].getName());
    assertEquals("Neheb, the Worthy", results[6].getName());
    assertEquals("Judith, the Scourge Diva", results[results.length - 1].getName());
  }
}
