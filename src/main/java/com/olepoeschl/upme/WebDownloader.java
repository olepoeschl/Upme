package com.olepoeschl.upme;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.function.Consumer;

public class WebDownloader implements UpdateDownloader, AutoCloseable {

    private final HttpClient client;

    public WebDownloader() {
        client = HttpClient.newHttpClient();
    }

    @Override
    public Path downloadUpdate(String downloadUrl, Consumer<Float> progressCallback) throws IOException {
        Path updateFilePath = Files.createTempFile("", "");

        URI uri = URI.create(downloadUrl);
        HttpRequest req = HttpRequest.newBuilder()
            .uri(uri)
            .timeout(Duration.ofMillis(10_000))
            .GET()
            .build();

        HttpResponse.BodyHandler<InputStream> handler = HttpResponse.BodyHandlers.ofInputStream();
        HttpResponse<InputStream> response;
        try {
            response = client.send(req, handler);
        } catch (InterruptedException e) {
            throw new RuntimeException("Could not download update: interrupted while sending http download request", e);
        }

        HttpHeaders headers = response.headers();
        long contentLength = headers.firstValueAsLong("Content-Length").orElse(-1L);

        try (
            InputStream is = response.body();
            FileOutputStream fos = new FileOutputStream(updateFilePath.toFile());
        ) {
            byte[] buffer = new byte[4096];
            long totalBytesDownloaded = 0L;
            int bytesDownloaded;

            while((bytesDownloaded = is.read(buffer)) != -1) {
                totalBytesDownloaded += bytesDownloaded;
                fos.write(buffer, 0, bytesDownloaded);
                if(progressCallback != null && contentLength > 0)
                    progressCallback.accept((float) totalBytesDownloaded / contentLength);
            }
        } catch (IOException e) {
            throw new IOException("Could not download update: I/O error while downloading file", e);
        }

        return updateFilePath;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
