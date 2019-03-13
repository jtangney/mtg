package com.bagbert.mtg.mtggoldfish;

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
  }
}