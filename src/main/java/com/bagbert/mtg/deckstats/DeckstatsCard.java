package com.bagbert.mtg.deckstats;

import com.bagbert.mtg.utils.FormattingConverter;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;

/**
 * Represents an individual card within a DeckStats deck
 * 
 * @author jtangney
 */
public class DeckstatsCard {

  @CsvCustomBindByPosition(position = 0, converter = FormattingConverter.class)
  private String cardName;
  @CsvBindByPosition(position = 1)
  private Integer qty;
  @CsvBindByPosition(position = 2)
  private Boolean isValid;
  @CsvCustomBindByPosition(position = 3, converter = FormattingConverter.class)
  private String comment;
  @CsvBindByPosition(position = 4)
  private Integer setId;
  @CsvCustomBindByPosition(position = 5, converter = FormattingConverter.class)
  private String setString;
  @CsvCustomBindByPosition(position = 6, converter = FormattingConverter.class)
  private String override;
  @CsvBindByPosition(position = 7)
  private Boolean isCommander;
  @CsvBindByPosition(position = 8)
  private Boolean isSideboard;
  @CsvBindByPosition(position = 9)
  private Boolean isMaybe;
  @CsvBindByPosition(position = 10)
  private Boolean isFoil;
  @CsvBindByPosition(position = 11)
  private Boolean isCustomSet;
  @CsvCustomBindByPosition(position = 12, converter = FormattingConverter.class)
  private String castingCost;
  @CsvBindByPosition(position = 13)
  private Integer cmc;
  @CsvBindByPosition(position = 14)
  private Integer colourIdentity;
  @CsvCustomBindByPosition(position = 15, converter = FormattingConverter.class)
  private String type;
  @CsvCustomBindByPosition(position = 16, converter = FormattingConverter.class)
  private String superType;
  @CsvBindByPosition(position = 17)
  private String rarity;
  @CsvBindByPosition(position = 18)
  private Integer displaySetId;
  @CsvBindByPosition(position = 19)
  private Double priceMiracle;
  @CsvBindByPosition(position = 20)
  private Double priceMkm;
  @CsvBindByPosition(position = 21)
  private Double priceTcgPlayer;
  @CsvBindByPosition(position = 22)
  private Double priceCardHoarder;
  @CsvBindByPosition(position = 23)
  private Double priceCardKingdom;

  public DeckstatsCard(String cardName, Integer qty, Boolean isValid, String comment, Integer setId,
      String setString, String override, Boolean isCommander, Boolean isSideboard, Boolean isMaybe,
      Boolean isFoil, Boolean isCustomSet, String castingCost, Integer cmc, Integer colourIdentity,
      String type, String superType, String rarity, Integer displaySetId, Double priceMiracle,
      Double priceMkm, Double priceTcgPlayer, Double priceCardHoarder, Double priceCardKingdom) {
    this.cardName = cardName;
    this.qty = qty;
    this.isValid = isValid;
    this.comment = comment;
    this.setId = setId;
    this.setString = setString;
    this.override = override;
    this.isCommander = isCommander;
    this.isSideboard = isSideboard;
    this.isMaybe = isMaybe;
    this.isFoil = isFoil;
    this.isCustomSet = isCustomSet;
    this.castingCost = castingCost;
    this.cmc = cmc;
    this.colourIdentity = colourIdentity;
    this.type = type;
    this.superType = superType;
    this.rarity = rarity;
    this.displaySetId = displaySetId;
    this.priceMiracle = priceMiracle;
    this.priceMkm = priceMkm;
    this.priceTcgPlayer = priceTcgPlayer;
    this.priceCardHoarder = priceCardHoarder;
    this.priceCardKingdom = priceCardKingdom;
  }

  public DeckstatsCard(String cardName, Integer qty, Boolean isValid, String comment, Integer setId,
      String setString, String override, Boolean isCommander, Boolean isSideboard, Boolean isMaybe,
      Boolean isFoil, Boolean isCustomSet) {
    this(cardName, qty, isValid, comment, setId, setString, override, isCommander, isSideboard,
        isMaybe, isFoil, isCustomSet, null, null, null, null, null, null, null, null, null, null, null, null);
  }

  public String getCardName() {
    return cardName;
  }

  public Integer getQty() {
    return qty;
  }

  public Boolean isValid() {
    return isValid;
  }

  public Boolean getIsValid() {
    return isValid();
  }

  public String getComment() {
    return comment;
  }

  public Integer getSetId() {
    return setId;
  }

  public String getSetString() {
    return setString;
  }

  public String getOverride() {
    return override;
  }

  public Boolean isCommander() {
    return isCommander;
  }

  public Boolean isSideboard() {
    return isSideboard;
  }

  public Boolean isMaybe() {
    return isMaybe;
  }

  public Boolean isFoil() {
    return isFoil;
  }

  public Boolean isCustomSet() {
    return isCustomSet;
  }

  public Boolean getIsCommander() {
    return isCommander();
  }

  public Boolean getIsSideboard() {
    return isSideboard();
  }

  public Boolean getIsMaybe() {
    return isMaybe();
  }

  public Boolean getIsFoil() {
    return isFoil();
  }

  public Boolean getIsCustomSet() {
    return isCustomSet();
  }

  public String getCastingCost() {
    return castingCost;
  }

  public Integer getCmc() {
    return cmc;
  }

  public Integer getColourIdentity() {
    return colourIdentity;
  }

  public String getType() {
    return type;
  }

  public String getSuperType() {
    return superType;
  }

  public String getRarity() {
    return rarity;
  }

  public Integer getDisplaySetId() {
    return displaySetId;
  }

  public Double getPriceMiracle() {
    return priceMiracle;
  }

  public Double getPriceMkm() {
    return priceMkm;
  }

  public Double getPriceTcgPlayer() {
    return priceTcgPlayer;
  }

  public Double getPriceCardHoarder() {
    return priceCardHoarder;
  }

  public Double getPriceCardKingdom() {
    return priceCardKingdom;
  }

}
