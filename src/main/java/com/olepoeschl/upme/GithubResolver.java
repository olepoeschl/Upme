package com.olepoeschl.upme;

import java.io.IOException;

public class GithubResolver implements UpdateResolver {

    private final String releasesUrl, updateFileAssetRegex;

    public GithubResolver(String repoOwner, String repoName, String updateFileAssetRegex) {
        releasesUrl = "https://api.github.com/repos/%s/%s/releases".formatted(repoOwner, repoName);
        this.updateFileAssetRegex = updateFileAssetRegex;
    }

    @Override
    public Version[] checkAvailableUpdates(String currentVersionString) throws IOException {
        // implementation hint: use jackson Tree model to perform search in the json structure
        return new Version[0];
    }

    public String getUrl() {
        return releasesUrl;
    }
}
