package com.olepoeschl.upme;

import java.io.IOException;

public class GithubResolver implements UpdateResolver {

    private final String repoOwner, repoName, updateFileAssetRegex;

    public GithubResolver(String repoOwner, String repoName, String updateFileAssetRegex) {
        this.repoOwner = repoOwner;
        this.repoName = repoName;
        this.updateFileAssetRegex = updateFileAssetRegex;
    }

    @Override
    public Version[] checkAvailableUpdates(String currentVersionString) throws IOException {
        return new Version[0];
    }

    public String getUrl() {
        // implementation hint: use jackson Tree model to perform search in the json structure
        return "https://api.github.com/repos/%s/%s/releases".formatted(repoOwner, repoName);
    }
}
