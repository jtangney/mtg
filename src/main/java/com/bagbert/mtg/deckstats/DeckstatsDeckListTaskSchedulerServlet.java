package com.bagbert.mtg.deckstats;

import com.bagbert.commons.football.exec.*;
import com.bagbert.mtg.Constants;
import com.bagbert.mtg.HttpUtils;
import com.bagbert.mtg.gcs.CsvWriter;
import com.google.cloud.tasks.v2beta3.*;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.Instant;

/**
 * Adds Cloud Tasks to fetch a range Deckstats deck list pages over a defined period.
 * i.e. paginates through the pages of deck lists.
 * Essentially allows us to wait a few seconds before fetching next page
 *
 * /deckstats/listscheduler?containsCard=someCmdr&endPage=N
 */
@WebServlet("deckstats/listscheduler")
public class DeckstatsDeckListTaskSchedulerServlet extends HttpServlet {

  private static final String RELATIVE_URI_ROOT = DeckstatsDeckListServlet.class.getAnnotation(WebServlet.class).name();

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    if (!HttpUtils.hasParam(req, "endPage")) {
      throw new RuntimeException("Missing required 'endPage' parameter");
    }
    int startPage = HttpUtils.getIntParam(req, "startPage", 1);
    int endPage = HttpUtils.getIntParam(req, "endPage", -1);
    int delay = HttpUtils.getIntParam(req, "delay", 20);
    String containsCard = HttpUtils.getParam(req, "containsCard");

    try (CloudTasksClient client = CloudTasksClient.create()) {
      int count = 0;
      for (int i = startPage; i <= endPage; i++) {
        String queuePath = QueueName.of(Constants.DEFAULT_PROJECT,
            Constants.DEFAULT_REGION, Constants.DECKLIST_QUEUE).toString();
        String pageUri = buildRelativeUri(i, containsCard);
        Task.Builder taskBuilder = Task.newBuilder()
            .setAppEngineHttpRequest(AppEngineHttpRequest.newBuilder()
                .setRelativeUri(pageUri).setHttpMethod(HttpMethod.GET).build());

        int offset = count * delay;
        taskBuilder.setScheduleTime(
            Timestamp.newBuilder().setSeconds(Instant.now().plusSeconds(offset).getEpochSecond()));
        Task task = client.createTask(queuePath, taskBuilder.build());
        count++;
      }
    }
  }

  String buildRelativeUri(int page, String containsCard) throws UnsupportedEncodingException {
    String uri = String.format("/deckstats/list?page=%d", page);
    if (!StringUtils.isEmpty(containsCard)) {
      return String.format("%s&containsCard=%s", uri, URLEncoder.encode(containsCard, "UTF-8"));
    }
    return uri;
  }
}
