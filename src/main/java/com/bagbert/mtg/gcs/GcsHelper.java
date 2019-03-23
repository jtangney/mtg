package com.bagbert.mtg.gcs;

import com.bagbert.commons.football.exec.GcsUploader;
import com.google.appengine.tools.cloudstorage.*;

import java.io.*;
import java.nio.channels.Channels;
import java.util.stream.Collectors;

public class GcsHelper implements GcsUploader {

  private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
      .initialRetryDelayMillis(10).retryMaxAttempts(10).totalRetryPeriodMillis(15000).build());

  @Override
  public void uploadToGcs(String bucketname, String filename, String content)
      throws IOException {
    GcsFileOptions withMeta = new GcsFileOptions.Builder()
        .mimeType("text/plain; charset=utf-8").build();
    GcsFilename fileName = new GcsFilename(bucketname, filename);
    GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, withMeta);
    OutputStream out = Channels.newOutputStream(outputChannel);
    out.write(content.getBytes("UTF-8"));
    outputChannel.close();
  }

  public String readFile(String bucketname, String filename) {
    try {
      GcsFilename gcsfile = new GcsFilename(bucketname, filename);
      GcsInputChannel readChannel = gcsService.openReadChannel(gcsfile, 0);
      InputStream in = Channels.newInputStream(readChannel);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      return reader.lines().collect(Collectors.joining(System.getProperty("line.separator")));
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
