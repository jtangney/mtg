package com.bagbert.mtg;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.lang3.StringUtils;

import com.bagbert.commons.football.tools.CollectionUtils;

public enum Colour {
  WHITE("W"), // 4
  BLUE("U"), // 8
  BLACK("B"), // 16
  RED("R"), // 32
  GREEN("G"), // 64
  NONE("C");

  private String code;

  private Colour(String code) {
    this.code = code;
  }

  public String code() {
    return this.code;
  }

  public static Colour infer(String str) {
    for (Colour c : Colour.values()) {
      if (c.name().equalsIgnoreCase(str)) {
        return c;
      }
    }
    for (Colour c : Colour.values()) {
      if (c.code.equalsIgnoreCase(str)) {
        return c;
      }
    }
    throw new IllegalArgumentException("Could not infer Colour!");
  }

  public static Set<Colour> inferMultiple(String str) {
    Set<Colour> set = new ListOrderedSet<>();
    char[] chars = str.toCharArray();
    for (char c : chars) {
      set.add(infer(String.valueOf(c)));
    }
    return set;
  }

  /**
   * Returns a String with a character for each included colour. Input is a int whose binary
   * representation maps to the set of colours 
   * e.g. 
   * 1 = 0000001 = no colour; land 
   * 2 = 0000010 = colourless 
   * 4 = 0000100 = white 
   * 12 = 0001100 = white-blue
   */
  public static String toColourString(int colourIdentity) {
    if (colourIdentity <= 2) {
      return "";
    }
    String binary = Integer.toBinaryString(colourIdentity);
    String padded = StringUtils.leftPad(binary, 7, "0");
    String reversed = StringUtils.reverse(padded);
    char[] digits = reversed.toCharArray();
    List<String> list = new ArrayList<>();
    for (Colour c : Colour.values()) {
      if (c == NONE) {
        continue;
      }
      int index = c.ordinal() + 2;
      if (digits[index] == '1') {
        list.add(c.code);
      }
    }
    return CollectionUtils.toString(list, "");
  }

  public static boolean isColourCode(String input) {
    for (Colour col: Colour.values()) {
      if(col.code().equalsIgnoreCase(input)) {
        return true;
      }
    }
    return false;
  }
}
