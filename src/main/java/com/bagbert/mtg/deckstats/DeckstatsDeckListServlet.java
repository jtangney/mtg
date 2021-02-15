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

/**
 * Fetch-parse-writes a list of Commander decks. Writes to GCS when deployed on
 * AppEngine, otherwise just logs CSV.
" param. Can also specify a "page" param.
 *
 * Standard usage is to specify the Commander via the "commander" request param.
 * e.g. See the example page below, list of decks with Prossh as commander
 * https://deckstats.net/decks/f/edh-commander/?search_order=updated%2Cdesc&search_cards_commander[]=Prossh%2C+Skyraider+of+Kher&lng=en&page=1
 * Can also specify additional search cards via the "containsCard" param.
 *
 * Note that typical usage is to call the /deckstats/listscheduler?commander=X rather
 * than call this directly. This paginates through the full list.
 */
@WebServlet("deckstats/list")
public class DeckstatsDeckListServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private static final String COMMANDERS_LIST_URL = Constants.ROOT_URL_DECKSTATS.concat(
      "/decks/f/edh-commander/?lng=en");

  // use these request params to search decks that contain paricular cards, or
  // that have a particular commander. Can be used together
  private static final String CONTAINS_CARD_SEARCH_PARAM = "&search_cards[]=%s";
  private static final String WITH_COMMANDER_SEARCH_PARAM = "&search_cards_commander[]=%s";
  // need to also include all the other (default) search params
  private static final String DEFAULT_SEARCH_PARAMS = "&search_title=&search_format=10&search_season=0&search_price_min=&search_price_max=&search_number_cards_main=&search_number_cards_sideboard=&search_tags=&search_age_max_days=0&search_age_max_days_custom=&search_order=updated,desc&utf8=";

  // web only serves up first 10000 decks (20 decks per page, 500 pages)
  public static int MAX_PAGES = 500;

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String url = buildUrl(req);
    System.out.println(url);
    String containsCard = HttpUtils.getParam(req, "containsCard");
    String withCommander = HttpUtils.getParam(req, "commander");
    JSoupFetcher fetcher = new JSoupFetcher(url);
    Parser<Document, DeckstatsListItem> parser = new DeckstatsDeckListParser(withCommander);
    ResultSetHandler<DeckstatsListItem> writer = new CsvWriter<>(Constants.DEFAULT_BUCKET,
        DeckstatsListItem.class);

    Executor<ResultSet<DeckstatsListItem>> executor = new FetchParseWriteExecutor<>(fetcher, parser,
        writer);
    executor.execute();
  }

  String buildUrl(HttpServletRequest req) {
    int page = HttpUtils.getIntParam(req, "page", 1);
    String containsCard = HttpUtils.getParam(req, "containsCard");
    String withCommander = HttpUtils.getParam(req, "commander");
    return buildUrl(withCommander, containsCard, page);
  }

  // used by test
  static String buildUrl(String commanderName, String cardName, int page) {
    String url = COMMANDERS_LIST_URL;
    if (commanderName != null) {
      String cmdrSearch = String.format(WITH_COMMANDER_SEARCH_PARAM, commanderName);
      url = url.concat(cmdrSearch);
    }
    if (cardName != null) {
      String cardSearch = String.format(CONTAINS_CARD_SEARCH_PARAM, cardName);
      url = url.concat(cardSearch);
    }
    String pageSuffix = String.format("&page=%d", page);
    return url.concat(DEFAULT_SEARCH_PARAMS).concat(pageSuffix);
  }
}
