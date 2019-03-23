package com.bagbert.mtg.common;

import com.bagbert.mtg.Constants;
import com.bagbert.mtg.utils.MtgUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractDeckParser {

  protected final String source;
  protected String type;
  protected String cardName;

  public AbstractDeckParser(String source, String type, String cardName) {
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

  protected Double toDouble(String str) {
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    try {
      Number num = NumberFormat.getNumberInstance().parse(str);
      return Double.valueOf(num.doubleValue());
    }
    catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

}
