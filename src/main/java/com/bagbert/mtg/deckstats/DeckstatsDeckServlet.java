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
 * Fetch-parse-writes a single Deckstats deck.
 * URL to fetch passed as 'deckUrl' param. Commander name passed
 * optionally passed as 'commander' param
 */
@WebServlet("deckstats/deck")
public class DeckstatsDeckServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static String ROOT_URL = Constants.ROOT_URL_DECKSTATS;
  private static String DECK_ROOT_URL = ROOT_URL + "/decks/";

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String deckUrlParam = HttpUtils.getParam(req, "deckUrl");
    String deckUrl = format(deckUrlParam);
    String commander = HttpUtils.getParam(req, "commander");
    JSoupFetcher fetcher = new JSoupFetcher(deckUrl);
    Parser<Document, DeckstatsDeckCard> parser = new DeckstatsDeckParser(commander);
    ResultSetHandler<DeckstatsDeckCard> handler = new CsvWriter<>(Constants.DEFAULT_BUCKET,
        DeckstatsDeckCard.class);

    Executor<ResultSet<DeckstatsDeckCard>> executor = new FetchParseWriteExecutor<>(fetcher, parser,
        handler);
    ResultSet<DeckstatsDeckCard> rs = executor.execute();
  }

  String format(String deckUrlParam) {
    // if it's a full url just leave it
    if (deckUrlParam.startsWith("http")) {
      return deckUrlParam;
    }
    if (deckUrlParam.startsWith("/decks")) {
      return ROOT_URL.concat(deckUrlParam);
    }
    return DECK_ROOT_URL.concat(deckUrlParam);
  }

}
