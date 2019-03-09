package com.bagbert.mtg.mtggoldfish;

import com.bagbert.commons.football.exec.*;
import com.bagbert.mtg.Constants;
import com.bagbert.mtg.HttpUtils;
import com.bagbert.mtg.deckstats.DeckstatsDeckListParser;
import com.bagbert.mtg.deckstats.DeckstatsListItem;
import com.bagbert.mtg.gcs.CsvWriter;
import org.jsoup.nodes.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("mtggoldfish/list")
public class GoldfishDeckListServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private static final String BASE_DECK_URL = Constants.ROOT_URL_GOLDFISH.concat("/deck/custom");
  private static final String COMMANDERS_LIST_URL = BASE_DECK_URL.concat("/commander?page=%d#paper");
  private static final String CONTAINS_CARD_URL_PATTERN = BASE_DECK_URL.concat("?page=%d&utf8=✓&mformat=commander&commander=%s&commit=Search#paper");

  // web only serves up first 10000 decks (20 decks per page, 500 pages)
  public static int MAX_PAGES = 500;

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String url = buildUrl(req);
    String containsCard = HttpUtils.getParam(req, "containsCard");
    JSoupFetcher fetcher = new JSoupFetcher(url);
    Parser<Document, GoldfishListItem> parser = new GoldfishDeckListParser(containsCard);
    ResultSetHandler<GoldfishListItem> writer = new CsvWriter<>(Constants.DEFAULT_BUCKET, GoldfishListItem.class);

    Executor<ResultSet<GoldfishListItem>> executor = new FetchParseWriteExecutor<>(fetcher, parser, writer);
    executor.execute();
  }

  String buildUrl(HttpServletRequest req) {
    int page = HttpUtils.getIntParam(req, "page", 1);
    String containsCard = HttpUtils.getParam(req, "containsCard");
    if (containsCard != null) {
      return String.format(CONTAINS_CARD_URL_PATTERN, page, containsCard);
    }
    throw new RuntimeException("Unimplemented! Handling 'containsCard' use case only");
  }
}