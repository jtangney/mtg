package com.bagbert.mtg;

import com.bagbert.commons.football.exec.Fetcher;
import com.google.appengine.tools.cloudstorage.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.util.stream.Collectors;

public class GcsFetcher implements Fetcher<Document> {

  private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
      .initialRetryDelayMillis(10).retryMinAttempts(2).retryMaxAttempts(3).totalRetryPeriodMillis(10000).build());

  private final String filename;
  private final String bucket;

  public GcsFetcher(String bucket, String filename) {
    this.bucket = bucket;
    this.filename = filename;
  }

  @Override
  public Document fetch() {
    try {
      GcsFilename gcsfile = new GcsFilename(bucket, filename);
      GcsInputChannel readChannel = gcsService.openReadChannel(gcsfile, 0);
      InputStream in = Channels.newInputStream(readChannel);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String content = reader.lines().collect(Collectors.joining(System.getProperty("line.separator")));
      return Jsoup.parse(content);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
