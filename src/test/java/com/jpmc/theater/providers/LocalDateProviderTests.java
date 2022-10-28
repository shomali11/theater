package com.jpmc.theater.providers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalDateProviderTests {

    @Test
    void testSingleton() {
        final LocalDateProvider provider1 = LocalDateProvider.singleton();
        final LocalDateProvider provider2 = LocalDateProvider.singleton();
        assertEquals(provider1, provider2);
    }
}
