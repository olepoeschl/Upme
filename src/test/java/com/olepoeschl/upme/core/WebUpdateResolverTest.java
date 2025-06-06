package com.olepoeschl.upme.core;

import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class WebUpdateResolverTest {

    @Test
    void httpClientCanBeInjected() {
        try (HttpClient injectedClient = HttpClient.newHttpClient()) {
            var resolver = new WebUpdateResolver("some_url", injectedClient);
            assertEquals("some_url", resolver.getUrl());
            assertSame(injectedClient, resolver.getHttpClient());
        }
    }

}
