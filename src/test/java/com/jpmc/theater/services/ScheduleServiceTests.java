package com.jpmc.theater.services;

import com.jpmc.theater.dao.MovieDao;
import com.jpmc.theater.dao.ShowingDao;
import com.jpmc.theater.models.*;
import com.jpmc.theater.providers.LocalDateProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScheduleServiceTests {

    private static final MovieDao movieDao = new MovieDao();
    private static final ShowingDao showingDao = new ShowingDao();
    private static final MovieService movieService = new MovieService(movieDao);
    private static final ShowingService showingService = new ShowingService(showingDao);
    private static final ScheduleService scheduleService = new ScheduleService(movieService, showingService);

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
    void testValidGetSchedule() {
        final LocalDate currentDate = LocalDateProvider.singleton().currentDate();
        final Schedule schedule = scheduleService.getSchedule(currentDate);

        assertEquals(currentDate.toString(), schedule.getDate());
        assertEquals(1, schedule.getMovieShowingList().size());

        final MovieShowing movieShowing = schedule.getMovieShowingList().get(0);
        assertEquals(1, movieShowing.getSequenceOfTheDay());
        assertEquals(currentDate + "T09:00", movieShowing.getShowStartTime());
        assertEquals("Spider-Man: No Way Home", movieShowing.getTitle());
        assertEquals("1 hour 30 minutes", movieShowing.getRunningTime());
        assertEquals(12.5, movieShowing.getTicketPrice());
    }

    @Test
    void testInvalidGetSchedule() {
        final LocalDate currentDate = LocalDate.of(2020, 1, 1);
        final Schedule schedule = scheduleService.getSchedule(currentDate);

        assertEquals(currentDate.toString(), schedule.getDate());
        assertEquals(0, schedule.getMovieShowingList().size());
    }
}
