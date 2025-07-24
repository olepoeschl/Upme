package com.olepoeschl.upme;

import java.util.List;

public class WebResolver implements UpdateResolver {

    public WebResolver(String url) {

    }

    public String getUrl() {
        return "https://some.url.com/updates.json";
    }

    @Override
    public List<Version> checkAvailableUpdates(String currentVersionString) {
        return List.of();
    }

}
