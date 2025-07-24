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

/**
 * An {@link UpdateResolver} implementation that uses HTTP / HTTPS GET to check for available updates by reading a json array of {@link Version} objects from a specified
 * URL.
 * <p>
 *     This is the go-to resolver for applications with a self-hosted update server.
 * </p>
 *
 * @see Version
 * @version 1.0.0
 * @since 1.0.0
 */
public record WebResolver(String url) implements UpdateResolver {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Checks for available updates by sending an HTTP GET request to the specified URL, which is expected to return a
     * JSON array of {@link Version} objects. See the {@link Version} class for the expected structure of the JSON
     * response.
     *
     * @param currentVersionString the current version of the application as a semantic versioning string
     * @return an array of {@link Version} objects representing available updates
     * @throws IOException if an error occurs while sending the request or processing the response
     *
     * @see Version
     * @since 1.0.0
     * @implNote This method uses Java's built-in HTTP client.
     */
    @Override
    public Version[] checkAvailableUpdates(String currentVersionString) throws IOException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                var availableVersions = mapper.readValue(response.body(), new TypeReference<List<Version>>() {
                });

                var currentSemver = new Semver(currentVersionString);
                availableVersions.removeIf(v -> new Semver(v.versionString()).isLowerThanOrEqualTo(currentSemver));
                return availableVersions.toArray(new Version[0]);
            }
        } catch (Exception e) {
            throw new IOException("could not check for available updates: " + e.getMessage(), e);
        }
    }

}
