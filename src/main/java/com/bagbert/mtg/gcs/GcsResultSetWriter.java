package com.bagbert.mtg.gcs;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;

import com.bagbert.commons.football.exec.AbstractGcsCsvResultSetWriter;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class GcsResultSetWriter<T> extends AbstractGcsCsvResultSetWriter<T> {

  private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
      .initialRetryDelayMillis(10).retryMaxAttempts(10).totalRetryPeriodMillis(15000).build());

  public GcsResultSetWriter(String bucket, Class<T> clazz) {
    super(bucket, clazz);
  }

  @Override
  public void uploadToGcs(String defaultBucket, String filename, String content)
      throws IOException {
    GcsFileOptions withMeta = new GcsFileOptions.Builder()
        .mimeType("text/plain; charset=utf-8").build();
    GcsFilename fileName = new GcsFilename(defaultBucket, filename);
    GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, withMeta);
    OutputStream out = Channels.newOutputStream(outputChannel);
    out.write(content.getBytes("UTF-8"));
    outputChannel.close();
  }
}