package com.bagbert.mtg.deckstats;

import com.bagbert.mtg.Constants;
import com.bagbert.mtg.utils.MtgUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractDeckstatsParser {

  protected final String source = Constants.SOURCE_DECKSTATS;
  protected String type;
  protected String cardName;

  public AbstractDeckstatsParser(String type, String cardName) {
    this.type = type;
    this.cardName = cardName;
  }

  String buildPath() {
    if (!StringUtils.isEmpty(cardName)) {
      String formattedCardName = MtgUtils.formatCardName(cardName);
      return String.format("%s/%s/%s", source, type, formattedCardName);
    }
    return String.format("%s/%s", source, type);
  }

}
