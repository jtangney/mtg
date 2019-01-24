package com.bagbert.mtg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ColourTest {

  @Test
  public void testToBinaryString() {
    assertEquals("10", Integer.toBinaryString(2));
    assertEquals("100", Integer.toBinaryString(4));
    assertEquals("100", Integer.toBinaryString(4));
    assertEquals("1111100", Integer.toBinaryString(124));
  }

  @Test
  public void testToColourString() {
    // ignore land, colourless
    assertEquals("", Colour.toColourString(1));
    assertEquals("", Colour.toColourString(2));
    assertEquals("W", Colour.toColourString(4));
    assertEquals("U", Colour.toColourString(8));
    assertEquals("WU", Colour.toColourString(12));
    assertEquals("WUB", Colour.toColourString(28));
    assertEquals("WB", Colour.toColourString(20));
    assertEquals("B", Colour.toColourString(16));
    assertEquals("WUBRG", Colour.toColourString(124));
    assertEquals("RG", Colour.toColourString(96));
    assertEquals("BR", Colour.toColourString(48));

  }
}
