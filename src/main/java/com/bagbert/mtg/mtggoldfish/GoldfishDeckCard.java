package com.bagbert.mtg.mtggoldfish;

import com.bagbert.commons.football.opencsv.NestedCsv;
import com.bagbert.commons.football.opencsv.NestedCsvConverter;
import com.bagbert.commons.football.tools.DateUtils;
import com.bagbert.mtg.deckstats.DeckstatsCard;
import com.bagbert.mtg.deckstats.DeckstatsDeckCard;
import com.bagbert.mtg.utils.FormattingConverter;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;

import java.util.Date;

public class GoldfishDeckCard {
  @CsvCustomBindByPosition(position = 0, converter = FormattingConverter.class)
  private String deckName;
  @CsvBindByPosition(position = 1)
  private Integer deckId;
  @CsvCustomBindByPosition(position = 2, converter = FormattingConverter.class)
  private String userName;
  @CsvBindByPosition(position = 3)
  @CsvDate("yyyy-MM-dd HH:mm:ssXX")
  private Date dateSubmitted;
  @CsvCustomBindByPosition(position = 4, converter = FormattingConverter.class)
  private String format;
  @CsvCustomBindByPosition(position = 5, converter = FormattingConverter.class)
  private String archetype;
  @CsvBindByPosition(position = 6)
  private Double deckPricePaper;
  @CsvBindByPosition(position = 7)
  private Double deckPriceOnline;
  @CsvDate("yyyy-MM-dd HH:mm:ssXX")
  @CsvBindByPosition(position = 8)
  private Date scrapeTime;
  @NestedCsv
  @CsvCustomBindByPosition(position = 9, converter = GoldfishDeckCard.GoldfishCardConverter.class)
  private GoldfishCard card;

  public GoldfishDeckCard(String deckName, Integer deckId, String userName, Date dateSubmitted, String format, String archetype, Double deckPricePaper, Double deckPriceOnline, Date scrapeTime, GoldfishCard card) {
    this.deckName = deckName;
    this.deckId = deckId;
    this.userName = userName;
    this.dateSubmitted = dateSubmitted;
    this.format = format;
    this.archetype = archetype;
    this.deckPricePaper = deckPricePaper;
    this.deckPriceOnline = deckPriceOnline;
    this.scrapeTime = scrapeTime;
    this.card = card;
  }

  public String getDeckName() {
    return deckName;
  }

  public Integer getDeckId() {
    return deckId;
  }

  public String getUserName() {
    return userName;
  }

  public Date getDateSubmitted() {
    return dateSubmitted;
  }

  public String getFormat() {
    return format;
  }

  public String getArchetype() {
    return archetype;
  }

  public Double getDeckPricePaper() {
    return deckPricePaper;
  }

  public Double getDeckPriceOnline() {
    return deckPriceOnline;
  }

  public Date getScrapeTime() {
    return scrapeTime;
  }

  public GoldfishCard getCard() {
    return card;
  }

  public static class GoldfishCardConverter extends NestedCsvConverter<GoldfishCard> {

    public GoldfishCardConverter() {
      super(GoldfishCard.class);
    }
  }

}
