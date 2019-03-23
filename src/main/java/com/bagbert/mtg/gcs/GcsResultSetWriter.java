package com.bagbert.mtg.gcs;

import com.bagbert.commons.football.exec.AbstractGcsCsvResultSetWriter;

import java.io.IOException;

public class GcsResultSetWriter<T> extends AbstractGcsCsvResultSetWriter<T> {

  public GcsResultSetWriter(String bucket, Class<T> clazz) {
    super(bucket, clazz);
  }

  @Override
  public void uploadToGcs(String bucketname, String filename, String content)
      throws IOException {
    new GcsHelper().uploadToGcs(bucketname, filename, content);
  }
}