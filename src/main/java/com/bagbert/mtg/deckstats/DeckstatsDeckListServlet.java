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
  private static final String COMMANDERS_LIST_URL = "https://deckstats.net/decks/f/edh-commander/?lng=en";

  // web only serves up first 10000 decks (20 decks per page, 500 pages)
  public static int MAX_PAGES = 500;

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int page = HttpUtils.getIntParam(req, "page", 1);
    String url = String.format("%s&page=%d", COMMANDERS_LIST_URL, page);
    JSoupFetcher fetcher = new JSoupFetcher(url);
    Parser<Document, DeckstatsListItem> parser = new DeckstatsDeckListParser();
    ResultSetHandler<DeckstatsListItem> writer = new CsvWriter<>(Constants.DEFAULT_BUCKET,
        DeckstatsListItem.class);

    Executor<ResultSet<DeckstatsListItem>> executor = new FetchParseWriteExecutor<>(fetcher, parser,
        writer);
    executor.execute();
  }
}
