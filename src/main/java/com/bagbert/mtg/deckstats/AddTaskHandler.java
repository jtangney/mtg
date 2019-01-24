package com.bagbert.mtg.deckstats;

import java.util.logging.Logger;

import com.bagbert.commons.football.exec.ResultSet;
import com.bagbert.commons.football.exec.ResultSetHandler;

@Deprecated
public class AddTaskHandler implements ResultSetHandler<DeckstatsListItem> {

  private static final Logger LOG = Logger.getLogger(AddTaskHandler.class.getName());

  @Override
  public void handle(ResultSet<DeckstatsListItem> rs) {
    // try (CloudTasksClient client = CloudTasksClient.create()) {
    // String queuePath = QueueName
    // .of(Constants.DEFAULT_PROJECT, Constants.DEFAULT_REGION, Constants.DECK_QUEUE).toString();
    //
    // for (DeckstatsListItem item : rs.getResults()) {
    // String uri = String.format("/deckstats/deck?deckUrl=", item.getDeckUrl());
    // Task.Builder taskBuilder = Task
    // .newBuilder()
    // .setAppEngineHttpRequest(AppEngineHttpRequest.newBuilder()
    // // .setBody(ByteString.copyFrom(payload, Charset.defaultCharset()))
    // .setRelativeUri(uri).setHttpMethod(HttpMethod.GET).build());
    //
    // Task task = client.createTask(queuePath, taskBuilder.build());
    // LOG.info("Created task for deck: " + uri);
    // }
    // }
    // catch (IOException e) {
    // LOG.log(Level.SEVERE, "Error adding task!");
    // throw new RuntimeException(e);
    // }
    //
  }

}
