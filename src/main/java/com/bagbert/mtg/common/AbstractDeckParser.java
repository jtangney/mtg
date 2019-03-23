package com.bagbert.mtg.common;

import com.bagbert.mtg.Constants;
import com.bagbert.mtg.utils.MtgUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractDeckstatsParser {

  protected final String source;
  protected String type;
  protected String cardName;

  public AbstractDeckstatsParser(String source, String type, String cardName) {
    this.source = source;
    this.type = type;
    this.cardName = cardName;
  }

  protected String buildPath() {
    if (!StringUtils.isEmpty(cardName)) {
      String formattedCardName = MtgUtils.formatCardName(cardName);
      return String.format("%s/%s/%s", source, type, formattedCardName);
    }
    return String.format("%s/%s", source, type);
  }

  protected String getPageNumber(String url) {
    Matcher matcher = Pattern.compile("page=(\\d+)").matcher(url);
    if (!matcher.find()) {
      return "";
    }
    return matcher.group(1);
  }


}
