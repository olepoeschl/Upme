package com.olepoeschl.upme;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.semver4j.Semver;

import java.io.IO;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GithubResolverTest {

    private static final String githubMockRepoAccessToken = "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";

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
    class CheckAvailableUpdates {
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
                    .setHeader("Authorization", "Bearer " + githubMockRepoAccessToken)
                    .build();

                try (HttpClient client = HttpClient.newHttpClient()) {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    var rootNode = mapper.readTree(response.body());

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
            var mockRepoVersions = fetchMockRepoVersions();
            var resolver = new GithubResolver(mockRepoOwner, mockRepoName, mockRepoUpdateFileAssetPattern);

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
            var currentSemver = Semver.parse(currentVersion);
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
