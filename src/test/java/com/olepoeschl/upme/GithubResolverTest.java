package com.olepoeschl.upme;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GithubResolverTest {

    @Test
    void testConstructorArgsWithGetters() {
        var repoOwner = "SomeGithubUser";
        var repoName = "SomeCoolRepo";
        var expectedUrl = "https://api.github.com/repos/%s/%s/releases".formatted(repoOwner, repoName);

        var resolver = new GithubResolver(repoOwner, repoName);
        assertEquals(expectedUrl, resolver.getUrl());
    }

}
