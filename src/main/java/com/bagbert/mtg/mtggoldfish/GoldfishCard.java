package com.bagbert.mtg.mtggoldfish;

import com.bagbert.mtg.utils.FormattingConverter;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;

public class GoldfishCard {

  @CsvCustomBindByPosition(position = 0, converter = FormattingConverter.class)
  private String cardName;
  @CsvBindByPosition(position = 1)
  private Integer qty;
  @CsvBindByPosition(position = 2)
  private Boolean isCommander;
  @CsvCustomBindByPosition(position = 3, converter = FormattingConverter.class)
  private String castingCost;
  @CsvBindByPosition(position = 4)
  private Integer cmc;
  @CsvCustomBindByPosition(position = 5, converter = FormattingConverter.class)
  private String colourIdentity;
  @CsvCustomBindByPosition(position = 6, converter = FormattingConverter.class)
  private String type;
  @CsvBindByPosition(position = 7)
  private Double price;

  public GoldfishCard(String cardName, Integer qty, Boolean isCommander, String castingCost, Integer cmc,
                      String colourIdentity, String type, Double price) {
    this.cardName = cardName;
    this.qty = qty;
    this.isCommander = isCommander;
    this.castingCost = castingCost;
    this.cmc = cmc;
    this.colourIdentity = colourIdentity;
    this.type = type;
    this.price = price;
  }

  public String getCardName() {
    return cardName;
  }

  public Integer getQty() {
    return qty;
  }

  public Boolean isCommander() {
    return isCommander;
  }

  public Boolean getIsCommander() {
    return isCommander();
  }

  public String getCastingCost() {
    return castingCost;
  }

  public Integer getCmc() {
    return cmc;
  }

  public String getColourIdentity() {
    return colourIdentity;
  }

  public String getType() {
    return type;
  }

  public Double getPrice() {
    return price;
  }
}
