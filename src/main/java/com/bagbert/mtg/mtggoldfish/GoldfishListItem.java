package com.bagbert.mtg.mtggoldfish;

import com.bagbert.commons.football.opencsv.QuotingConverter;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;

import java.util.Date;
import java.util.Objects;

public class GoldfishListItem {

  @CsvBindByPosition(position = 0)
  private Integer deckId;
  @CsvCustomBindByPosition(position = 1, converter = QuotingConverter.class)
  private String deckName;
  @CsvCustomBindByPosition(position = 2, converter = QuotingConverter.class)
  private String userName;
  @CsvBindByPosition(position = 3)
  private String colours;
  @CsvBindByPosition(position = 4)
  private String currencySymbol;
  @CsvBindByPosition(position = 5)
  private Double priceOnline;
  @CsvBindByPosition(position = 6)
  private Double pricePaper;
  @CsvBindByPosition(position = 7)
  private String deckUrl;
  @CsvDate("yyyy-MM-dd HH:mm:ssXX")
  @CsvBindByPosition(position = 8)
  private Date scrapeTime;

  public GoldfishListItem(Integer deckId, String deckName, String userName, String colours, String currencySymbol,
                          Double priceOnline, Double pricePaper, String deckUrl, Date scrapeTime) {
    this.deckId = deckId;
    this.deckName = deckName;
    this.userName = userName;
    this.colours = colours;
    this.currencySymbol = currencySymbol;
    this.priceOnline = priceOnline;
    this.pricePaper = pricePaper;
    this.deckUrl = deckUrl;
    this.scrapeTime = scrapeTime;
  }

  public Integer getDeckId() {
    return deckId;
  }

  public String getDeckName() {
    return deckName;
  }

  public String getUserName() {
    return userName;
  }

  public String getColours() {
    return colours;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public Double getPriceOnline() {
    return priceOnline;
  }

  public Double getPricePaper() {
    return pricePaper;
  }

  public String getDeckUrl() {
    return deckUrl;
  }

  public Date getScrapeTime() {
    return scrapeTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof GoldfishListItem))
      return false;
    GoldfishListItem that = (GoldfishListItem) o;
    return Objects.equals(deckId, that.deckId) && Objects.equals(deckName, that.deckName) && Objects.equals(userName, that.userName) && Objects.equals(colours, that.colours) && Objects.equals(currencySymbol, that.currencySymbol) && Objects.equals(priceOnline, that.priceOnline) && Objects.equals(pricePaper, that.pricePaper) && Objects.equals(deckUrl, that.deckUrl) && Objects.equals(scrapeTime, that.scrapeTime);
  }

  @Override
  public int hashCode() {

    return Objects.hash(deckId, deckName, userName, colours, currencySymbol, priceOnline, pricePaper, deckUrl, scrapeTime);
  }
}
