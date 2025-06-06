package com.olepoeschl.upme.core;

import java.net.http.HttpClient;

public class WebUpdateResolver implements UpdateResolver {

    private final String url;
    private final HttpClient httpClient;

    public WebUpdateResolver(String url, HttpClient httpClient) {
        this.url = url;
        this.httpClient = httpClient;
    }

    public String getUrl() {
        return url;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}
