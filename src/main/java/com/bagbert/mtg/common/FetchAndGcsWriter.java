package com.bagbert.mtg.common;

import com.bagbert.commons.football.exec.JSoupFetcher;
import com.bagbert.commons.football.tools.DateUtils;
import com.bagbert.mtg.Constants;
import com.bagbert.mtg.gcs.GcsHelper;
import com.google.common.flogger.FluentLogger;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;

public class FetchAndGcsWriter extends JSoupFetcher {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public FetchAndGcsWriter(String url) {
    super(url);
  }

  public Document fetch() {
    Document doc = super.fetch();
    try {
      this.writeToGcs(doc);
    }
    catch (IOException e) {
      logger.atWarning().log("Failed to write to GCS: %s", e.getMessage());
    }
    return doc;
  }

  void writeToGcs(Document doc) throws IOException {
    String url = doc.location();
    String deckId = StringUtils.substringAfterLast(url,"/").split("#")[0];
    String filename = String.format("%s-%s.html", DateUtils.toYYYYMMDD(new Date()), deckId);
    String path = String.format("%s/%s/%s", Constants.SOURCE_GOLDFISH, "commander-decks-scraped", filename);
    new GcsHelper().uploadToGcs(Constants.DEFAULT_BUCKET, path, doc.toString());
  }
}
