package com.bagbert.mtg.mtggoldfish;

import com.bagbert.commons.football.exec.*;
import com.bagbert.mtg.Constants;
import com.bagbert.mtg.HttpUtils;
import com.bagbert.mtg.common.FetchAndGcsWriter;
import com.bagbert.mtg.gcs.CsvWriter;
import org.jsoup.nodes.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("mtggoldfish/deck")
public class GoldfishDeckServlet extends HttpServlet {

  private static final Logger LOG = Logger.getLogger(GoldfishDeckServlet.class.getName());

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    LOG.info(String.format("Processing request: %s?%s", req.getRequestURL().toString(), req.getQueryString()));
    String deckUrlParam = HttpUtils.getParam(req, "deckUrl");
    String containsCard = HttpUtils.getParam(req, "containsCard");
    String deckUrl = format(deckUrlParam);
    // fetch the HTML but also write it to GCS!
    Fetcher<Document> fetcher = new FetchAndGcsWriter(deckUrl);
    Parser<Document, GoldfishDeckCard> parser = new GoldfishDeckParser(containsCard);
    ResultSetHandler<GoldfishDeckCard> handler = new CsvWriter<>(Constants.DEFAULT_BUCKET, GoldfishDeckCard.class);

    Executor<ResultSet<GoldfishDeckCard>> executor = new FetchParseWriteExecutor<>(fetcher, parser,
        handler);
    ResultSet<GoldfishDeckCard> rs = executor.execute();
  }

  String format(String deckUrlParam) {
    return String.format("%s%s#paper", Constants.ROOT_URL_GOLDFISH, deckUrlParam);
  }

}