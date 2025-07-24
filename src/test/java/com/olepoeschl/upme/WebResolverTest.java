package com.olepoeschl.upme;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WebResolverTest {

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

    @Test
    void testConstructorArgsWithGetters() {
        var expectedUrl = "https://some.url.com/updates.json";
        var resolver = new WebResolver(expectedUrl);
        assertEquals(expectedUrl, resolver.url());

        expectedUrl = "https://another.url.com/updates.json";
        resolver = new WebResolver(expectedUrl);
        assertEquals(expectedUrl, resolver.url());
    }

    @Nested
    class CheckAvailableUpdates {
        @Test
        void testRegular() throws IOException {
            var url = "/updates.json";
            stubFor(get(urlEqualTo(url))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody("[{\"versionString\":\"0.0.9\",\"downloadUrl\":\"http://example.com/download/0.0.9\"},{\"versionString\":\"1.0.1\",\"downloadUrl\":\"http://example.com/download/1.0.1\"},{\"versionString\":\"1.0.2\",\"downloadUrl\":\"http://example.com/download/1.0.2\",\"description\":\"Bug fixes\", \"checksumSha3256\":\"123def\"}]")));
            var expected = new Version[2];
            expected[0] = new Version("1.0.1", "http://example.com/download/1.0.1", null, null);
            expected[1] = new Version("1.0.2", "http://example.com/download/1.0.2", "Bug fixes", "123def");

            var resolver = new WebResolver("http://127.0.0.1:8080" + url);
            var updates = resolver.checkAvailableUpdates("1.0.0");

            assertEquals(expected.length, updates.length, "Expected two updates, got " + updates.length);
            for (int i = 0; i < expected.length; i++)
                assertEquals(expected[i], updates[i], "Expected update " + i + " to be " + expected[i] + ", got " + updates[i]);
        }

        @Test
        void testServerNotReachable() {
            var resolver = new WebResolver("http://127.0.0.1:8080/this/url/does/not/exist");
            assertThrows(IOException.class, () -> resolver.checkAvailableUpdates("1.0.0"),
                "Expected IOException when server is not reachable");
        }

        @Test
        void testServerReturnsInvalidJson() {
            var url = "/updates.json";
            stubFor(get(urlEqualTo(url))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody("This is not valid JSON")));

            var resolver = new WebResolver("http://127.0.0.1:8080" + url);
            assertThrows(IOException.class, () -> resolver.checkAvailableUpdates("1.0.0"),
                "Expected IOException when server returns invalid JSON");
        }
    }
}
