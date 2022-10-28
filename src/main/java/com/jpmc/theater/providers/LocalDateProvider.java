package com.jpmc.theater.providers;

import java.time.LocalDate;

public class LocalDateProvider {
    private static volatile LocalDateProvider instance = null;

    /**
     * Thread Safe Singleton Lazy Initialization
     *
     * @return make sure to return singleton instance
     */
    public static LocalDateProvider singleton() {
        if (instance == null) {
            synchronized (LocalDateProvider.class) {
                if (instance == null) {
                    instance = new LocalDateProvider();
                }
            }
        }
        return instance;
    }

    public LocalDate currentDate() {
        return LocalDate.now();
    }
}
