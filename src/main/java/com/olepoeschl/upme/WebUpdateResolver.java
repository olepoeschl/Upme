package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Objects;

@NullMarked
public class WebUpdateResolver implements UpdateResolver {

    private final String url;
    private final HttpClient httpClient;

    public WebUpdateResolver(String url, HttpClient httpClient) {
        this.url = Objects.requireNonNull(url, "url must not be null");
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient must not be null");
    }

    public String getUrl() {
        return url;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public List<UpdateInfo> checkForUpdates() {
        // TODO
        return List.of();
    }
}
