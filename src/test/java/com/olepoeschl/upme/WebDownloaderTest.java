package com.olepoeschl.upme;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebDownloaderTest {

    private static final String BASE_URL = "http://localhost:8080";

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        configureFor("localhost", 8080);
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Nested
    class DownloadUpdateTest {
        @Test
        void testWithProgressUpdates() throws URISyntaxException, IOException {
            byte[] expectedFileData = Files.readAllBytes(Paths.get(getClass().getResource("/WebDownloaderTest/regular.txt").toURI()));

            stubFor(get(urlEqualTo("/regular.txt"))
                .willReturn(aResponse()
                    .withBody(expectedFileData)
                    .withHeader("Content-Length", String.valueOf(expectedFileData.length))
                    // the content-length header is crucial for progress updates, as otherwise the length is unknown,
                    // hence progress cannot be calculated
                ));

            Path downloadedFile = null;
            try (
                WebDownloader downloader = new WebDownloader();
            ) {
                final float[] lastProgress = {0f};
                downloadedFile = downloader.downloadUpdate(
                    BASE_URL + "/regular.txt",
                    progress -> lastProgress[0] = progress
                );
                byte[] actualFileData = Files.readAllBytes(downloadedFile);

                assertArrayEquals(expectedFileData, actualFileData, "Actual file data does not match expected file data.");
                assertEquals(1f, lastProgress[0], "Progress callback was not called with 100% progress.");

            } finally {
                if(downloadedFile != null)
                    Files.deleteIfExists(downloadedFile);
            }
        }

        @Test
        void testShouldNotHaveProgressUpdates() throws URISyntaxException, IOException {
            byte[] expectedFileData = Files.readAllBytes(Paths.get(getClass().getResource("/WebDownloaderTest/regular.txt").toURI()));

            stubFor(get(urlEqualTo("/regular.txt"))
                .willReturn(aResponse()
                        .withBody(expectedFileData)
                    // NO Content-Length header this time, so progress updates cannot be calculated
                ));

            Path downloadedFile = null;
            try (
                WebDownloader downloader = new WebDownloader();
            ) {
                final float[] lastProgress = {-1f};
                downloadedFile = downloader.downloadUpdate(
                    BASE_URL + "/regular.txt",
                    progress -> lastProgress[0] = progress
                );
                byte[] actualFileData = Files.readAllBytes(downloadedFile);

                assertArrayEquals(expectedFileData, actualFileData, "Actual file data does not match expected file data.");
                assertEquals(-1f, lastProgress[0], "Progress callback should not have been called when content length is unknown.");

            } finally {
                if(downloadedFile != null)
                    Files.deleteIfExists(downloadedFile);
            }
        }
    }

}
