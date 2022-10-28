package com.jpmc.theater.dao;

import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.MovieCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class MovieDaoTests {

    private static final MovieDao movieDao = new MovieDao();

    @BeforeAll
    static void setup() {
        final Movie spiderMan = Movie.builder()
                .id("1")
                .title("Spider-Man: No Way Home")
                .runningTime(Duration.ofMinutes(90))
                .ticketPrice(12.5)
                .code(MovieCode.SPECIAL)
                .build();

        movieDao.addMovie(spiderMan);
    }

    @Test
    void testInvalidAdd() {
        assertThrows(IllegalArgumentException.class, () -> {
            final Movie spiderMan = Movie.builder()
                    .id("1")
                    .title("Spider-Man: No Way Home")
                    .runningTime(Duration.ofMinutes(90))
                    .ticketPrice(12.5)
                    .code(MovieCode.SPECIAL)
                    .build();

            movieDao.addMovie(spiderMan);
        });
    }

    @Test
    void testValidGet() {
        final Movie movie = movieDao.getMovie("1");
        assertEquals("1", movie.getId());
        assertEquals("Spider-Man: No Way Home", movie.getTitle());
        assertEquals(Duration.ofMinutes(90), movie.getRunningTime());
        assertEquals(MovieCode.SPECIAL, movie.getCode());
        assertTrue(movie.isSpecialCode());
        assertEquals(12.5, movie.getTicketPrice());
    }

    @Test
    void testInvalidGet() {
        assertThrows(IllegalArgumentException.class, () -> {
            movieDao.getMovie("unknown");
        });
    }
}
