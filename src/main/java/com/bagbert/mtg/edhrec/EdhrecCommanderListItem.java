package com.bagbert.mtg.edhrec;

import com.bagbert.commons.football.opencsv.QuotingConverter;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;

public class EdhrecCommanderListItem {

  @CsvCustomBindByPosition(position = 0, converter = QuotingConverter.class)
  private String name;
  @CsvBindByPosition(position = 1)
  private String colourCode;
  @CsvBindByPosition(position = 2)
  private String colourGroup;
  @CsvBindByPosition(position = 3)
  private String relativeUrl;
  @CsvBindByPosition(position = 4)
  private String image;
  @CsvBindByPosition(position = 5)
  private boolean isCommander;
  @CsvBindByPosition(position = 6)
  private boolean isBanned;
  @CsvBindByPosition(position = 7)
  private boolean isUnofficial;

  public EdhrecCommanderListItem(String name, String colourCode, String colourGroup,
      String relativeUrl, String image, boolean isCommander, boolean isBanned,
      boolean isUnofficial) {
    this.name = name;
    this.colourCode = colourCode;
    this.colourGroup = colourGroup;
    this.relativeUrl = relativeUrl;
    this.image = image;
    this.isCommander = isCommander;
    this.isBanned = isBanned;
    this.isUnofficial = isUnofficial;
  }

  public String getName() {
    return name;
  }

  public String getColourCode() {
    return colourCode;
  }

  public String getColourGroup() {
    return colourGroup;
  }

  public String getRelativeUrl() {
    return relativeUrl;
  }

  public String getImage() {
    return image;
  }

  public boolean isCommander() {
    return isCommander;
  }

  public boolean getIsCommander() {
    return isCommander();
  }

  public boolean isBanned() {
    return isBanned;
  }

  public boolean getIsBanned() {
    return isBanned();
  }

  public boolean isUnofficial() {
    return isUnofficial;
  }

  public boolean getIsUnofficial() {
    return isUnofficial();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((image == null) ? 0 : image.hashCode());
    result = prime * result + (isBanned ? 1231 : 1237);
    result = prime * result + (isCommander ? 1231 : 1237);
    result = prime * result + (isUnofficial ? 1231 : 1237);
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((relativeUrl == null) ? 0 : relativeUrl.hashCode());
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
    EdhrecCommanderListItem other = (EdhrecCommanderListItem) obj;
    if (image == null) {
      if (other.image != null)
        return false;
    }
    else if (!image.equals(other.image))
      return false;
    if (isBanned != other.isBanned)
      return false;
    if (isCommander != other.isCommander)
      return false;
    if (isUnofficial != other.isUnofficial)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    }
    else if (!name.equals(other.name))
      return false;
    if (relativeUrl == null) {
      if (other.relativeUrl != null)
        return false;
    }
    else if (!relativeUrl.equals(other.relativeUrl))
      return false;
    return true;
  }

}
