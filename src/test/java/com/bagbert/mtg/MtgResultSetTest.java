package com.bagbert.mtg;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

public class MtgResultSetTest {

  @Test
  public void testFilename() {
    MtgResultSet<String> rs = new MtgResultSet<>("some/path", Collections.emptySet());
    assertEquals("", rs.getFilename());
    rs = new MtgResultSet<>("some/path", Collections.emptySet(), "token");
    assertEquals("token", rs.getFilename());
    rs = new MtgResultSet<>("some/path", Collections.emptySet(), "another", "token");
    assertEquals("another-token", rs.getFilename());
    rs = new MtgResultSet<>("some/path", Collections.emptySet(), "another", "token", null);
    assertEquals("another-token", rs.getFilename());
  }
}
