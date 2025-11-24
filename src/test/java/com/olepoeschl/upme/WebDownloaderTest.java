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
        void testRegular() throws URISyntaxException, IOException {
            byte[] expectedFileData = Files.readAllBytes(Paths.get(getClass().getResource("/WebDownloaderTest/regular.txt").toURI()));

            stubFor(get(urlEqualTo("/regular.txt"))
                .willReturn(aResponse()
                    .withBody(expectedFileData)));

            Path downloadedFile = null;
            try (
                WebDownloader downloader = new WebDownloader();
            ) {
                downloadedFile = downloader.downloadUpdate(BASE_URL + "/regular.txt", progress -> {});
                byte[] actualFileData = Files.readAllBytes(downloadedFile);

                assertArrayEquals(expectedFileData, actualFileData, "Actual file data does not match expected file data.");
            } finally {
                if(downloadedFile != null)
                    Files.deleteIfExists(downloadedFile);
            }
        }
    }

}
