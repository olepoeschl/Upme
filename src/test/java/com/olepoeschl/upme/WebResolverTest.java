package com.olepoeschl.upme;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebResolverTest {

    @Test
    void testConstructorArgsWithGetters() {
        var resolver = new WebResolver("https://some.url.com/updates.json");
        assertEquals("https://some.url.com/updates.json", resolver.getUrl());
    }

}
