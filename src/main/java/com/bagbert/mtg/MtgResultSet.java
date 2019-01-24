package com.bagbert.mtg;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.bagbert.commons.football.exec.ResultSet;

public class MtgResultSet<T> implements ResultSet<T> {

  private static final long serialVersionUID = 1L;

  private String path;
  private Set<T> results;
  private String[] tokens;

  public MtgResultSet(String path, Set<T> results, String... tokens) {
    this.path = path;
    this.results = results;
    this.tokens = tokens;
  }

  @Override
  public Set<T> getResults() {
    return results;
  }

  @Override
  public String getFilename() {
    return StringUtils.join(tokens, "-");
  }

  @Override
  public String getPath() {
    return path;
  }

}
