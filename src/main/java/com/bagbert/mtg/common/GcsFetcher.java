package com.bagbert.mtg;

import com.bagbert.commons.football.exec.Fetcher;
import com.bagbert.mtg.gcs.GcsHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GcsFetcher implements Fetcher<Document> {

  private final String filename;
  private final String bucket;

  public GcsFetcher(String bucket, String filename) {
    this.bucket = bucket;
    this.filename = filename;
  }

  @Override
  public Document fetch() {
    String content = new GcsHelper().readFile(bucket, filename);
    return Jsoup.parse(content);
  }
}
