package com.bagbert.mtg.utils;

import org.apache.commons.lang3.StringUtils;

public class MtgUtils {

  public static String formatCardName(String cardName) {
    if (StringUtils.isEmpty(cardName)) {
      return null;
    }
    return StringUtils.removeAll(cardName, "[',]").replace(" ", "-").toLowerCase();
  }
}
