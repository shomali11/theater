package com.jpmc.theater.services;

import com.jpmc.theater.dao.ShowingDao;
import com.jpmc.theater.models.Showing;
import com.jpmc.theater.providers.LocalDateProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShowingServiceTests {

    private static final ShowingDao showingDao = new ShowingDao();
    private static final ShowingService showingService = new ShowingService(showingDao);

    @BeforeAll
    static void setup() {
        final LocalDate currentDate = LocalDateProvider.singleton().currentDate();
        final LocalDateTime currentDateTime = LocalDateTime.of(currentDate, LocalTime.of(9, 0));
        final Showing showing = Showing.builder()
                .id("11")
                .movieId("1")
                .sequenceOfTheDay(1)
                .showStartTime(currentDateTime)
                .build();
        showingService.addShowing(showing);
    }

    @Test
    void testValidGet() {
        final Showing showing = showingService.getShowing("11");
        assertEquals("11", showing.getId());
        assertEquals("1", showing.getMovieId());
        assertEquals(1, showing.getSequenceOfTheDay());
    }

    @Test
    void testInvalidGet() {
        assertThrows(IllegalArgumentException.class, () -> {
            showingService.getShowing("unknown");
        });
    }
}
