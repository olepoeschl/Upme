package com.olepoeschl.upme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class WebDownloaderTest {


    // this token has read access to the UpmeMockRepo repository and is used exclusively in this test, that is for fetching Releases
    private static String GITHUB_TOKEN;

    private static final String BASE_URL = "http://localhost:8080";

    private WireMockServer wireMockServer;

    @BeforeAll
    static void setupOnce() {
        GITHUB_TOKEN = System.getenv("GITHUB_TOKEN");
    }

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
            if (downloadedFile != null)
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
            if (downloadedFile != null)
                Files.deleteIfExists(downloadedFile);
        }
    }

    @Test
    void testWithGithubResolver() throws IOException {
        String mockRepoOwner = "olepoeschl";
        String mockRepoName = "UpmeMockRepo";
        String mockRepoUpdateFileAssetPattern = "application.jar";
        var resolver = new GithubResolver(mockRepoOwner, mockRepoName, mockRepoUpdateFileAssetPattern);
        resolver.addHeader("Authorization", "Bearer " + GITHUB_TOKEN);

        try (var webDownloader = new WebDownloader()) {
            Version[] versions = resolver.checkAvailableUpdates("0.0.1");
            Version updateVersion = versions[(int) (Math.random() * versions.length)];
            Path updateFile = webDownloader.downloadUpdate(updateVersion.downloadUrl(), progress -> {});

            String fileContentExpectedContains = updateVersion.versionString();
            assertTrue(Files.readString(updateFile).contains(fileContentExpectedContains));
        }


    }

}
