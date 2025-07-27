package com.olepoeschl.upme;

import java.io.IOException;

public class GithubResolver implements UpdateResolver {

    public GithubResolver(String repoOwner, String repoName, String updateFileAssetRegex) {

    }

    @Override
    public Version[] checkAvailableUpdates(String currentVersionString) throws IOException {
        return new Version[0];
    }

    public String getUrl() {
        return "https://api.github.com/repos/%s/%s/releases".formatted("SomeGithubUser", "SomeCoolRepo");
    }
}
