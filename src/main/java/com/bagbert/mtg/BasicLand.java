package com.bagbert.mtg;

public enum BasicLand {
  PLAINS(Colour.WHITE),
  ISLAND(Colour.BLACK),
  SWAMP(Colour.BLACK),
  MOUNTAIN(Colour.RED),
  FOREST(Colour.GREEN),
  WASTES(Colour.NONE);

  private Colour colour;

  private BasicLand(Colour c) {
    this.colour = c;
  }

  public Colour colour() {
    return this.colour;
  }
}
