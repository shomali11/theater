package com.jpmc.theater.dao;

import com.jpmc.theater.models.Showing;
import com.jpmc.theater.providers.LocalDateProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShowingDaoTests {

    private static final LocalDate currentDate = LocalDateProvider.singleton().currentDate();
    private static final ShowingDao showingDao = new ShowingDao();

    @BeforeAll
    static void setup() {
        final LocalDateTime currentDateTime = LocalDateTime.of(currentDate, LocalTime.of(9, 0));
        final Showing showing = Showing.builder()
                .id("11")
                .movieId("1")
                .sequenceOfTheDay(1)
                .showStartTime(currentDateTime)
                .build();
        showingDao.addShowing(showing);
    }

    @Test
    void testInvalidAddRepeatId() {
        assertThrows(IllegalArgumentException.class, () -> {
            final LocalDateTime currentDateTime = LocalDateTime.of(currentDate, LocalTime.of(9, 0));
            final Showing showing = Showing.builder()
                    .id("11")
                    .movieId("1")
                    .sequenceOfTheDay(1)
                    .showStartTime(currentDateTime)
                    .build();
            showingDao.addShowing(showing);
        });
    }

    @Test
    void testInvalidAddRepeatSequence() {
        assertThrows(IllegalArgumentException.class, () -> {
            final LocalDateTime currentDateTime = LocalDateTime.of(currentDate, LocalTime.of(9, 0));
            final Showing showing = Showing.builder()
                    .id("22")
                    .movieId("1")
                    .sequenceOfTheDay(1)
                    .showStartTime(currentDateTime)
                    .build();
            showingDao.addShowing(showing);
        });
    }

    @Test
    void testValidGet() {
        final Showing showing = showingDao.getShowing("11");
        assertEquals("11", showing.getId());
        assertEquals("1", showing.getMovieId());
        assertEquals(1, showing.getSequenceOfTheDay());
    }

    @Test
    void testInvalidGet() {
        assertThrows(IllegalArgumentException.class, () -> {
            showingDao.getShowing("unknown");
        });
    }

    @Test
    void testValidList() {
        final LocalDate currentDate = LocalDateProvider.singleton().currentDate();
        final List<Showing> showings = showingDao.getShowings(currentDate);
        assertEquals(1, showings.size());

        final Showing showing = showings.get(0);
        assertEquals("11", showing.getId());
        assertEquals("1", showing.getMovieId());
        assertEquals(1, showing.getSequenceOfTheDay());
    }

    @Test
    void testInvalidList() {
        final LocalDate currentDate = LocalDate.of(2000, 1, 1);
        final List<Showing> showings = showingDao.getShowings(currentDate);
        assertEquals(0, showings.size());
    }
}
