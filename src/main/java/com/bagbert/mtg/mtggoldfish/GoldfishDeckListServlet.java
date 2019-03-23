package com.bagbert.mtg.mtggoldfish;

import com.bagbert.commons.football.exec.*;
import com.bagbert.mtg.Constants;
import com.bagbert.mtg.HttpUtils;
import com.bagbert.mtg.common.GcsFetcher;
import com.bagbert.mtg.gcs.CsvWriter;
import org.jsoup.nodes.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Fetches an HTML file from GCS and hands off to parser.
 * HTML is of a list page of decks containing a particular card (which are dynamically loaded,
 * hence why we can't fetch in the normal JSoup way).
 * This servlet is typically called by a Cloud Task (which are created by a Cloud Function).
 *
 * Url of the form: mtggoldfish/list?bucket=myBucket&file=path/to/htmlfile
 */
@WebServlet("mtggoldfish/list")
public class GoldfishDeckListServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String bucket = HttpUtils.getParam(req, "bucket");
    String file = HttpUtils.getParam(req, "file");
    String containsCard = null;
    String[] pathElements = file.split("/");
    if (pathElements.length > 3) {
      containsCard = pathElements[pathElements.length - 2];
    }

    GcsFetcher fetcher = new GcsFetcher(bucket, file);
    Parser<Document, GoldfishListItem> parser = new GoldfishDeckListParser(containsCard);
    ResultSetHandler<GoldfishListItem> writer = new CsvWriter<>(Constants.DEFAULT_BUCKET, GoldfishListItem.class);

    Executor<ResultSet<GoldfishListItem>> executor = new FetchParseWriteExecutor<>(fetcher, parser, writer);
    executor.execute();
  }

//  private static final String BASE_DECK_URL = Constants.ROOT_URL_GOLDFISH.concat("/deck/custom");
//  private static final String COMMANDERS_LIST_URL = BASE_DECK_URL.concat("/commander?page=%d#paper");
//  private static final String CONTAINS_CARD_URL_PATTERN = BASE_DECK_URL.concat("?page=%d&utf8=✓&mformat=commander&commander=%s&commit=Search#paper");
//
//  @Deprecated
//  String buildUrl(HttpServletRequest req) {
//    int page = HttpUtils.getIntParam(req, "page", 1);
//    String containsCard = HttpUtils.getParam(req, "containsCard");
//    if (containsCard != null) {
//      return String.format(CONTAINS_CARD_URL_PATTERN, page, containsCard);
//    }
//    throw new RuntimeException("Unimplemented! Handling 'containsCard' use case only");
//  }
}