package com.olepoeschl.upme;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.semver4j.Semver;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

// TODO: documentation
public class WebResolver implements UpdateResolver {

    private final String url;

    private static final ObjectMapper mapper = new ObjectMapper();

    public WebResolver(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public Version[] checkAvailableUpdates(String currentVersionString) throws IOException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                var availableVersions = mapper.readValue(response.body(), new TypeReference<List<Version>>() {});

                var currentSemver = new Semver(currentVersionString);
                availableVersions.removeIf(v -> new Semver(v.versionString()).isLowerThanOrEqualTo(currentSemver));
                return availableVersions.toArray(new Version[0]);
            }
        } catch (Exception e) {
            throw new IOException("could not check for available updates: " + e.getMessage(), e);
        }
    }

}
