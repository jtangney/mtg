package com.bagbert.mtg.deckstats;

import java.util.Date;

import com.bagbert.commons.football.opencsv.NestedCsv;
import com.bagbert.commons.football.opencsv.NestedCsvConverter;
import com.bagbert.commons.football.tools.DateUtils;
import com.bagbert.mtg.utils.FormattingConverter;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;

/**
 * Represents an individual card within a DeckStats deck, including all the top-level deck details
 * (denormalised)
 * 
 * @author jtangney
 */
public class DeckstatsDeckCard {

  @CsvCustomBindByPosition(position = 0, converter = FormattingConverter.class)
  private String deckName;
  @CsvBindByPosition(position = 1)
  private Integer deckId;
  @CsvCustomBindByPosition(position = 2, converter = FormattingConverter.class)
  private String userName;
  @CsvBindByPosition(position = 3)
  private Integer userId;
  @CsvBindByPosition(position = 4)
  @CsvDate(DateUtils.DATETIME_FORMAT_PATTERN)
  private Date dateUpdated;
  @CsvBindByPosition(position = 5)
  private Integer deckRevision;
  @CsvBindByPosition(position = 6)
  private Integer cardCountTotal;
  @CsvBindByPosition(position = 7)
  private Integer cardCountNonBasic;
  @CsvBindByPosition(position = 8)
  private Integer cardCountBasicLands;
  @NestedCsv
  @CsvCustomBindByPosition(position = 9, converter = DeckstatsCardConverter.class)
  private DeckstatsCard card;

  public DeckstatsDeckCard(String deckName, Integer deckId, String userName, Integer userId,
      Date dateUpdated, Integer deckRevision, Integer cardCountTotal, Integer cardCountNonBasic,
      Integer cardCountBasicLands, String cardName, Integer qty, Boolean isValid, String comment,
      Integer setId, String setString, String override, Boolean isCommander, Boolean isSideboard,
      Boolean isMaybe, Boolean isFoil, Boolean isCustomSet, String castingCost, Integer cmc,
      Integer colourIdentity, String type, String superType, String rarity, Integer displaySetId,
      Double priceMiracle, Double priceMkm, Double priceTcgPlayer, Double priceCardHoarder,
      Double priceCardKingdom) {
    this(deckName, deckId, userName, userId, dateUpdated, deckRevision, cardCountTotal,
        cardCountNonBasic, cardCountBasicLands);
    this.card = new DeckstatsCard(cardName, qty, isValid, comment, setId, setString, override,
        isCommander, isSideboard, isMaybe, isFoil, isCustomSet, castingCost, cmc, colourIdentity,
        type, superType, rarity, displaySetId, priceMiracle, priceMkm, priceTcgPlayer,
        priceCardHoarder, priceCardKingdom);
  }

  public DeckstatsDeckCard(String deckName, Integer deckId, String userName, Integer userId,
      Date dateUpdated, Integer deckRevision, Integer cardCountTotal, Integer cardCountNonBasic,
      Integer cardCountBasicLands, DeckstatsCard card) {
    this(deckName, deckId, userName, userId, dateUpdated, deckRevision, cardCountTotal,
        cardCountNonBasic, cardCountBasicLands);
    this.card = card;
  }

  private DeckstatsDeckCard(String deckName, Integer deckId, String userName, Integer userId,
      Date dateUpdated, Integer deckRevision, Integer cardCountTotal, Integer cardCountNonBasic,
      Integer cardCountBasicLands) {
    this.deckName = deckName;
    this.deckId = deckId;
    this.userName = userName;
    this.userId = userId;
    this.dateUpdated = dateUpdated;
    this.deckRevision = deckRevision;
    this.cardCountTotal = cardCountTotal;
    this.cardCountNonBasic = cardCountNonBasic;
    this.cardCountBasicLands = cardCountBasicLands;
  }

  public String getDeckName() {
    return deckName;
  }

  public Integer getDeckId() {
    return deckId;
  }

  public Integer getCardCountTotal() {
    return cardCountTotal;
  }

  public Integer getCardCountNonBasic() {
    return cardCountNonBasic;
  }

  public Integer getCardCountBasicLands() {
    return cardCountBasicLands;
  }

  public String getUserName() {
    return userName;
  }

  public Integer getUserId() {
    return userId;
  }

  public Integer getDeckRevision() {
    return this.deckRevision;
  }

  public Date getDateUpdated() {
    return this.dateUpdated;
  }

  public DeckstatsCard getCard() {
    return this.card;
  }

  public String getCardName() {
    return card.getCardName();
  }

  public Integer getQty() {
    return card.getQty();
  }

  public Boolean isValid() {
    return card.isValid();
  }

  public String getComment() {
    return card.getComment();
  }

  public Integer getSetId() {
    return card.getSetId();
  }

  public String getSetString() {
    return card.getSetString();
  }

  public String getOverride() {
    return card.getOverride();
  }

  public Boolean isCommander() {
    return card.isCommander();
  }

  public Boolean isSideboard() {
    return card.isSideboard();
  }

  public Boolean isMaybe() {
    return card.isMaybe();
  }

  public Boolean isFoil() {
    return card.isFoil();
  }

  public Boolean isCustomSet() {
    return card.isCustomSet();
  }

  public String getCastingCost() {
    return card.getCastingCost();
  }

  public Integer getCmc() {
    return card.getCmc();
  }

  public Integer getColourIdentity() {
    return card.getColourIdentity();
  }

  public String getType() {
    return card.getType();
  }

  public String getSuperType() {
    return card.getSuperType();
  }

  public String getRarity() {
    return card.getRarity();
  }

  public Integer getDisplaySetId() {
    return card.getDisplaySetId();
  }

  public Double getPriceMiracle() {
    return card.getPriceMiracle();
  }

  public Double getPriceMkm() {
    return card.getPriceMkm();
  }

  public Double getPriceTcgPlayer() {
    return card.getPriceTcgPlayer();
  }

  public Double getPriceCardHoarder() {
    return card.getPriceCardHoarder();
  }

  public Double getPriceCardKingdom() {
    return card.getPriceCardKingdom();
  }

  public static class DeckstatsCardConverter extends NestedCsvConverter<DeckstatsCard> {

    public DeckstatsCardConverter() {
      super(DeckstatsCard.class);
    }
  }

}
