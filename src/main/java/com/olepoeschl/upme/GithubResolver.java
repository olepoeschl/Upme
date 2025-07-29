package com.olepoeschl.upme;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.semver4j.Semver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@NullMarked
public class GithubResolver implements UpdateResolver {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final String releasesUrl, updateFileAssetPattern;
    private final Map<String, String> headers = new HashMap<String, String>();

    public GithubResolver(String repoOwner, String repoName, String updateFileAssetPattern) {
        releasesUrl = "https://api.github.com/repos/%s/%s/releases".formatted(repoOwner, repoName);
        this.updateFileAssetPattern = updateFileAssetPattern;
    }

    public String getUrl() {
        return releasesUrl;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public Version[] checkAvailableUpdates(String currentVersionString) throws IOException {
        var currentSemver = Semver.parse(currentVersionString);
        if(currentSemver == null)
            throw new IllegalArgumentException("not a valid semantic versioning string: " + currentVersionString);

        final List<Version> versions = new ArrayList<>();
        try {
            var reqBuilder = HttpRequest.newBuilder()
                .uri(URI.create(releasesUrl))
                .GET();
            for(var header : headers.entrySet())
                reqBuilder.header(header.getKey(), header.getValue());
            HttpRequest request = reqBuilder.build();

            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if(response.statusCode() < 200 || response.statusCode() > 299)
                    throw new IOException("could not fetch available updates form Github: status code "
                        + response.statusCode() + ": " + response.body());

                var rootNode = mapper.readTree(response.body());
                if(rootNode instanceof ArrayNode) {
                    rootNode.forEach(releaseNode -> {
                        var versionString = releaseNode.get("tag_name").asText();
                        if(currentSemver.isGreaterThanOrEqualTo(versionString))
                            return;

                        // check if the release has an asset that matches the updateFileAssetPattern
                        String updateAssetDownloadUrl = null; // null means there is no matching asset
                        String checksum = null;

                        var assetsNode = releaseNode.get("assets");
                        if(assetsNode instanceof ArrayNode) {
                            for(int i = 0; i < assetsNode.size(); i++) {
                                var asset = assetsNode.get(i);
                                if(asset.get("name").asText().equals(updateFileAssetPattern)) {
                                    updateAssetDownloadUrl = asset.get("browser_download_url").asText();
                                    checksum = asset.get("digest").asText().substring(7); // always begins with "sha256:"
                                    break;
                                }
                            }
                        }
                        // if a matching asset was found, add this version to the list
                        if(updateAssetDownloadUrl != null) {
                            var description = releaseNode.get("body").asText();
                            versions.add(new Version(versionString, updateAssetDownloadUrl, description, checksum));
                        }
                    });
                }

                return versions.toArray(Version[]::new);
            }
        } catch (Exception e) {
            throw new IOException("could not fetch available updates from Github: " + e.getMessage(), e);
        }
    }

}
