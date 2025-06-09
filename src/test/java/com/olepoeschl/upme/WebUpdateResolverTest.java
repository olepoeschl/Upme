package com.olepoeschl.upme;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;

public class WebUpdateResolverTest {

    @Nested
    class Constructor {
        @Test
        void givenValidArgs_thenGettersReturnTheirValues() {
            try (HttpClient injectedClient = HttpClient.newHttpClient()) {
                var resolver = new WebUpdateResolver("some_url", injectedClient);
                assertEquals("some_url", resolver.getUrl());
                assertSame(injectedClient, resolver.getHttpClient());
            }
        }
        @Test
        void givenNullArgs_thenThrowNullPointerException() {
            assertThrows(NullPointerException.class, () -> new WebUpdateResolver(null, HttpClient.newHttpClient()));
            assertThrows(NullPointerException.class, () -> new WebUpdateResolver("some_url", null));
            assertThrows(NullPointerException.class, () -> new WebUpdateResolver(null, null));
        }
    }

}
