package com.olepoeschl.upme;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebResolverTest {

    @Test
    void testConstructorArgsWithGetters() {
        var expectedUrl = "https://some.url.com/updates.json";
        var resolver = new WebResolver(expectedUrl);
        assertEquals(expectedUrl, resolver.getUrl());

        expectedUrl = "https://another.url.com/updates.json";
        resolver = new WebResolver(expectedUrl);
        assertEquals(expectedUrl, resolver.getUrl());
    }

}
