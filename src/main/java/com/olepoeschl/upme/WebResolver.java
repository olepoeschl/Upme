package com.olepoeschl.upme;

import java.util.List;

public class WebResolver implements UpdateResolver {

    private final String url;

    public WebResolver(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public List<Version> checkAvailableUpdates(String currentVersionString) {
        var expected = new Version[2];
        expected[0] = new Version("1.0.1", "http://example.com/download/1.0.1", "", null);
        expected[1] = new Version("1.0.2", "http://example.com/download/1.0.2", "Bug fixes", "123def");
        return List.of(expected);
    }

}
