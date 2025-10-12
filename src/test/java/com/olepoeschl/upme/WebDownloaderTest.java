package com.olepoeschl.upme;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WebDownloaderTest {

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
            stubFor(get(urlEqualTo("/regular.txt"))
                .willReturn(aResponse()
                    .withBody(Files.readAllBytes(Paths.get(getClass().getResource("/WebDownloaderTest/regular.txt").toURI())))));
            // TODO
        }
    }

}
