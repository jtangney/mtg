package com.bagbert.mtg.deckstats;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Document;

import com.bagbert.commons.football.exec.Executor;
import com.bagbert.commons.football.exec.FetchParseWriteExecutor;
import com.bagbert.commons.football.exec.JSoupFetcher;
import com.bagbert.commons.football.exec.Parser;
import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.commons.football.exec.ResultSetHandler;
import com.bagbert.mtg.Constants;
import com.bagbert.mtg.HttpUtils;
import com.bagbert.mtg.gcs.CsvWriter;

@WebServlet("deckstats/list")
public class DeckstatsDeckListServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private static final String COMMANDERS_LIST_URL = Constants.ROOT_URL_DECKSTATS.concat("/decks/f/edh-commander/?lng=en");
  private static final String CONTAINS_CARD_URL_SUFFIX = "&search_title=&search_format=10&search_season=0&search_price_min=&search_price_max=&search_number_cards_main=&search_number_cards_sideboard=&search_cards[]=%s&search_tags=&search_age_max_days=0&search_age_max_days_custom=&search_order=updated,desc&utf8=";

  // web only serves up first 10000 decks (20 decks per page, 500 pages)
  public static int MAX_PAGES = 500;

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String url = buildUrl(req);
    System.out.println(url);
    String containsCard = HttpUtils.getParam(req, "containsCard");
    JSoupFetcher fetcher = new JSoupFetcher(url);
    Parser<Document, DeckstatsListItem> parser = new DeckstatsDeckListParser(containsCard);
    ResultSetHandler<DeckstatsListItem> writer = new CsvWriter<>(Constants.DEFAULT_BUCKET,
        DeckstatsListItem.class);

    Executor<ResultSet<DeckstatsListItem>> executor = new FetchParseWriteExecutor<>(fetcher, parser,
        writer);
    executor.execute();
  }

  String buildUrl(HttpServletRequest req) {
    int page = HttpUtils.getIntParam(req, "page", 1);
    String containsCard = HttpUtils.getParam(req, "containsCard");
    return buildUrl(containsCard, page);
  }

  // used by test
  static String buildUrl(String cardName, int page) {
    if (cardName != null) {
      String urlSuffix = String.format(CONTAINS_CARD_URL_SUFFIX, cardName);
      return String.format("%s%s&page=%d", COMMANDERS_LIST_URL, urlSuffix, page);
    }
    return String.format("%s&page=%d", COMMANDERS_LIST_URL, page);
  }
}
