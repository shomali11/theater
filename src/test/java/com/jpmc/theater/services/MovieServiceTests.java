package com.jpmc.theater.services;

import com.jpmc.theater.dao.MovieDao;
import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.MovieCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class MovieServiceTests {

    private static final MovieDao movieDao = new MovieDao();
    private static final MovieService movieService = new MovieService(movieDao);

    @BeforeAll
    static void setup() {
        final Movie spiderMan = Movie.builder()
                .id("1")
                .title("Spider-Man: No Way Home")
                .runningTime(Duration.ofMinutes(90))
                .ticketPrice(12.5)
                .code(MovieCode.SPECIAL)
                .build();

        movieService.addMovie(spiderMan);
    }

    @Test
    void testValidGet() {
        final Movie movie = movieService.getMovie("1");
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
            movieService.getMovie("unknown");
        });
    }
}
