package com.olepoeschl.upme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.semver4j.Semver;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

public class GithubResolverTest {

    // this token has read access to the UpmeMockRepo repository and is used exclusively in this test, that is for fetching Releases
    private static String GITHUB_TOKEN;

    @BeforeAll
    static void setup() {
        GITHUB_TOKEN = System.getenv("GITHUB_TOKEN");
    }


    @Test
    void testUrlIsCorrectlyConstructed() {
        var repoOwner = "SomeGithubUser";
        var repoName = "SomeCoolRepo";
        var expectedUrl = "https://api.github.com/repos/%s/%s/releases".formatted(repoOwner, repoName);

        var updateFileAssetPattern = "application.jar";
        var resolver = new GithubResolver(repoOwner, repoName, updateFileAssetPattern);
        assertEquals(expectedUrl, resolver.getUrl());
    }

    @Test
    void testAddHeaders() {
        var expectedHeaders = new HashMap<String, String>();
        expectedHeaders.put("header1", "header1 value");
        expectedHeaders.put("header2", "header2 value");
        expectedHeaders.put("auth", "token xyz_123_abc456lmn789");
        expectedHeaders.put("Accept-Language", "de");

        var resolver = new GithubResolver("some_owner", "some_repo", "some_file_pattern");
        for(var entry : expectedHeaders.entrySet())
            resolver.addHeader(entry.getKey(), entry.getValue());

        Map<String, String> gotHeaders = resolver.getHeaders();
        assertEquals(expectedHeaders, gotHeaders);
    }

    @Nested
    class CheckAvailableUpdatesTest {
        private static final String mockRepoOwner = "olepoeschl";
        private static final String mockRepoName = "UpmeMockRepo";
        private static final String mockRepoUpdateFileAssetPattern = "application.jar";
        private static final String mockNonexistentRepoName = "non.existent.repo,,";
        private static final ObjectMapper mapper = new ObjectMapper();

        private static List<Version> fetchMockRepoVersions() throws IOException {
            final List<Version> versions = new ArrayList<>();
            try {
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/repos/%s/%s/releases".formatted(mockRepoOwner, mockRepoName)))
                    .GET()
                    .header("Authorization", "Bearer " + GITHUB_TOKEN)
                    .build();

                try (HttpClient client = HttpClient.newHttpClient()) {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    var rootNode = mapper.readTree(response.body());

                    if(response.statusCode() < 200 || response.statusCode() > 299)
                        throw new IOException("could not fetch available updates form Github: status code "
                            + response.statusCode() + ": " + response.body());

                    if(rootNode instanceof ArrayNode) {
                        rootNode.forEach(releaseNode -> {
                            // check if the release has an asset that matches the updateFileAssetPattern
                            String updateAssetDownloadUrl = null; // null means there is no matching asset
                            String checksum = null;

                            var assetsNode = releaseNode.get("assets");
                            if(assetsNode instanceof ArrayNode) {
                                for(int i = 0; i < assetsNode.size(); i++) {
                                    var asset = assetsNode.get(i);
                                    if(asset.get("name").asText().equals(mockRepoUpdateFileAssetPattern)) {
                                        updateAssetDownloadUrl = asset.get("browser_download_url").asText();
                                        checksum = asset.get("digest").asText().substring(7); // always begins with "sha256:"
                                        break;
                                    }
                                }
                            }
                            // if a matching asset was found, add this version to the list
                            if(updateAssetDownloadUrl != null) {
                                var versionString = releaseNode.get("tag_name").asText();
                                var description = releaseNode.get("body").asText();
                                versions.add(new Version(versionString, updateAssetDownloadUrl, description, checksum));
                            }
                        });
                    }
                    return versions;
                }
            } catch (Exception e) {
                throw new IOException("could not fetch MockRepo versions: " + e.getMessage(), e);
            }
        }

        @Test
        void resolveMockRepo() throws IOException {
            assumeFalse(GITHUB_TOKEN == null);

            var mockRepoVersions = fetchMockRepoVersions();
            var resolver = new GithubResolver(mockRepoOwner, mockRepoName, mockRepoUpdateFileAssetPattern);
            resolver.addHeader("Authorization", "Bearer " + GITHUB_TOKEN);

            for(var version : mockRepoVersions) {
                var currentVersion = version.versionString();
                var currentSemver = Semver.parse(currentVersion);
                Version[] expectedAvailableUpdates = mockRepoVersions.stream().filter(v ->
                    currentSemver.isLowerThan(v.versionString())).toArray(Version[]::new);

                Version[] gotAvailableUpdates = resolver.checkAvailableUpdates(currentVersion);

                assertEquals(expectedAvailableUpdates.length, gotAvailableUpdates.length,
                    "wrong results for version " + currentVersion);
                for(int i = 0; i < expectedAvailableUpdates.length; i++)
                    assertEquals(expectedAvailableUpdates[i], gotAvailableUpdates[i]);
            }

            // test if the resolver correctly resolves all releases
            var currentVersion = "0.0.0";
            Version[] expectedAvailableUpdates = mockRepoVersions.toArray(Version[]::new);
            Version[] gotAvailableUpdates = resolver.checkAvailableUpdates(currentVersion);
            assertEquals(expectedAvailableUpdates.length, gotAvailableUpdates.length,
                "wrong results for version " + currentVersion);
            for(int i = 0; i < expectedAvailableUpdates.length; i++)
                assertEquals(expectedAvailableUpdates[i], gotAvailableUpdates[i]);
        }

        @Test
        void throwsIllegalArgumentException_WhenInvalidVersionString() {
            var resolver = new GithubResolver(mockRepoOwner, mockRepoName, mockRepoUpdateFileAssetPattern);
            assertThrows(IllegalArgumentException.class, () ->
                resolver.checkAvailableUpdates("no_a_valid_version"));
        }

        @Test
        void testRepoNotResolvable() {
            var resolver = new GithubResolver(mockRepoOwner, mockNonexistentRepoName, mockRepoUpdateFileAssetPattern);
            assertThrows(IOException.class, () -> resolver.checkAvailableUpdates("0.3.0"),
                "Expected IOException when response failed");
        }
    }
}
