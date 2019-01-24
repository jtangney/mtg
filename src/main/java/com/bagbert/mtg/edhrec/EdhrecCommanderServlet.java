package com.bagbert.mtg.edhrec;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.Document;

import com.bagbert.commons.football.exec.Executor;
import com.bagbert.commons.football.exec.FetchParseWriteExecutor;
import com.bagbert.commons.football.exec.JSoupFetcher;
import com.bagbert.commons.football.exec.Parser;
import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.commons.football.exec.ResultSetHandler;
import com.bagbert.commons.football.exec.ToCsvStringLogger;

@WebServlet("edhrec")
public class EdhrecCommanderServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public static String COMMANDERS_URL_ROOT = "https://edhrec.com/commanders";

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String colours = req.getParameter("colours");
    Validate.notBlank(colours);

    String url = String.format("%s/%s", COMMANDERS_URL_ROOT, colours);
    JSoupFetcher fetcher = new JSoupFetcher(url);
    Parser<Document, EdhrecCommanderListItem> parser = new EdhrecCommanderListParser();
    ResultSetHandler<EdhrecCommanderListItem> handler = new ToCsvStringLogger<>(
        EdhrecCommanderListItem.class);

    Executor<ResultSet<EdhrecCommanderListItem>> executor = new FetchParseWriteExecutor<>(fetcher,
        parser, handler);
    ResultSet<EdhrecCommanderListItem> rs = executor.execute();
  }
}
