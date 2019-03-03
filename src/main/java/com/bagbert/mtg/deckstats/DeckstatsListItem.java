package com.bagbert.mtg.deckstats;

import java.util.Date;

import com.bagbert.commons.football.opencsv.QuotingConverter;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;

public class DeckstatsListItem {

  @CsvBindByPosition(position = 0)
  private Integer deckId;
  @CsvCustomBindByPosition(position = 1, converter = QuotingConverter.class)
  private String deckName;
  @CsvBindByPosition(position = 2)
  private Integer userId;
  @CsvCustomBindByPosition(position = 3, converter = QuotingConverter.class)
  private String userName;
  @CsvBindByPosition(position = 4)
  private Integer likes;
  @CsvBindByPosition(position = 5)
  private Integer price;
  @CsvBindByPosition(position = 6)
  private Integer views;
  @CsvBindByPosition(position = 7)
  private String deckUrl;
  @CsvDate("yyyyMMdd-HH:mm:ssXX")
  @CsvBindByPosition(position = 8)
  private Date lastUpdated;
  @CsvDate("yyyy-MM-dd HH:mm:ssXX")
  @CsvBindByPosition(position = 9)
  private Date scrapeTime;


  public DeckstatsListItem(Integer deckId, String deckName, Integer userId, String userName,
      Integer likes, Integer price, Integer views, String deckUrl, Date lastUpdated, Date scrapeTime) {
    this.deckId = deckId;
    this.deckName = deckName;
    this.userId = userId;
    this.userName = userName;
    this.likes = likes;
    this.price = price;
    this.views = views;
    this.deckUrl = deckUrl;
    this.lastUpdated = lastUpdated;
    this.scrapeTime = scrapeTime;
  }

  public Integer getDeckId() {
    return deckId;
  }

  public String getDeckName() {
    return deckName;
  }

  public Integer getUserId() {
    return userId;
  }

  public String getUserName() {
    return userName;
  }

  public Integer getLikes() {
    return likes;
  }

  public Integer getPrice() {
    return price;
  }

  public Integer getViews() {
    return views;
  }

  public String getDeckUrl() {
    return this.deckUrl;
  }

  public Date getLastUpdated() {
    return this.lastUpdated;
  }

  public Date getScrapeTime() {
    return this.scrapeTime;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((deckId == null) ? 0 : deckId.hashCode());
    result = prime * result + ((deckName == null) ? 0 : deckName.hashCode());
    result = prime * result + ((likes == null) ? 0 : likes.hashCode());
    result = prime * result + ((price == null) ? 0 : price.hashCode());
    result = prime * result + ((userId == null) ? 0 : userId.hashCode());
    result = prime * result + ((userName == null) ? 0 : userName.hashCode());
    result = prime * result + ((views == null) ? 0 : views.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DeckstatsListItem other = (DeckstatsListItem) obj;
    if (deckId == null) {
      if (other.deckId != null)
        return false;
    }
    else if (!deckId.equals(other.deckId))
      return false;
    if (deckName == null) {
      if (other.deckName != null)
        return false;
    }
    else if (!deckName.equals(other.deckName))
      return false;
    if (likes == null) {
      if (other.likes != null)
        return false;
    }
    else if (!likes.equals(other.likes))
      return false;
    if (price == null) {
      if (other.price != null)
        return false;
    }
    else if (!price.equals(other.price))
      return false;
    if (userId == null) {
      if (other.userId != null)
        return false;
    }
    else if (!userId.equals(other.userId))
      return false;
    if (userName == null) {
      if (other.userName != null)
        return false;
    }
    else if (!userName.equals(other.userName))
      return false;
    if (views == null) {
      if (other.views != null)
        return false;
    }
    else if (!views.equals(other.views))
      return false;
    return true;
  }

}
