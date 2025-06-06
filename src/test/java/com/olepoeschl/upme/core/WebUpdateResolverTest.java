package com.olepoeschl.upme.core;

import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class WebUpdateResolverTest {

    @Test
    void givenValidArgs_whenConstructor_thenCreatesInstanceWithInjectedArgs()  {
        try (HttpClient injectedClient = HttpClient.newHttpClient()) {
            var resolver = new WebUpdateResolver("some_url", injectedClient);
            assertEquals("some_url", resolver.getUrl());
            assertSame(injectedClient, resolver.getHttpClient());
        }
    }

    @Test
    void givenNullArgs_whenConstructor_thenThrowsException() {
        try {
            new WebUpdateResolver(null, null);
        } catch (NullPointerException e) {
            assertEquals("url must not be null", e.getMessage());
        }

        try {
            new WebUpdateResolver("some_url", null);
        } catch (NullPointerException e) {
            assertEquals("httpClient must not be null", e.getMessage());
        }
    }


}
