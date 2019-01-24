package com.bagbert.mtg.gcs;

import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.commons.football.exec.ResultSetHandler;
import com.bagbert.commons.football.exec.ToCsvStringLogger;
import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.api.utils.SystemProperty.Environment;

public class CsvWriter<T> implements ResultSetHandler<T> {

  private String bucket;
  protected Class<T> clazz;

  public CsvWriter(String bucket, Class<T> clazz) {
    this.bucket = bucket;
    this.clazz = clazz;
  }

  @Override
  public void handle(ResultSet<T> rs) {
    // if dev server just log csv
    // TODO: ugly!
    if (!isAppEngineProd()) {
      new ToCsvStringLogger<T>(clazz).handle(rs);
      return;
    }
    new GcsResultSetWriter<T>(bucket, clazz).handle(rs);
  }

  public boolean isAppEngineProd() {
    return SystemProperty.environment.value() == Environment.Value.Production;
  }

}
