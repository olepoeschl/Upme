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
        return List.of();
    }

}
