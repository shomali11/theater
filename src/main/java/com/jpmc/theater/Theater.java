package com.jpmc.theater;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.MovieCode;
import com.jpmc.theater.models.Schedule;
import com.jpmc.theater.models.Showing;
import com.jpmc.theater.printers.SchedulePrinter;
import com.jpmc.theater.providers.LocalDateProvider;
import com.jpmc.theater.services.MovieService;
import com.jpmc.theater.services.ScheduleService;
import com.jpmc.theater.services.ShowingService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Theater {

    public static void main(final String[] args) {
        // Guice allows me to inject dependencies
        final Injector injector = Guice.createInjector(new AbstractModule() {});

        // Populate our in memory database with some movies
        final MovieService movieService = injector.getInstance(MovieService.class);
        for (final Movie movie: getMovies()) {
            movieService.addMovie(movie);
        }

        // Populate our in memory database with some showings using the current date
        final LocalDate currentDate = LocalDateProvider.singleton().currentDate();
        final ShowingService showingService = injector.getInstance(ShowingService.class);
        for (final Showing showing: getShowings(currentDate)) {
            showingService.addShowing(showing);
        }

        // Calculate the schedule for that date
        final ScheduleService scheduleService = injector.getInstance(ScheduleService.class);
        final Schedule schedule = scheduleService.getSchedule(currentDate);

        // Print twice, once via text and another via JSON
        final SchedulePrinter schedulePrinter = injector.getInstance(SchedulePrinter.class);
        schedulePrinter.printTextSchedule(schedule);
        schedulePrinter.printJsonSchedule(schedule);
    }

    // Some example movies to populate our in memory database with
    private static List<Movie> getMovies() {
        final Movie spiderMan = Movie.builder()
                .id("1")
                .title("Spider-Man: No Way Home")
                .runningTime(Duration.ofMinutes(90))
                .ticketPrice(12.5)
                .code(MovieCode.SPECIAL)
                .build();

        final Movie turningRed = Movie.builder()
                .id("2")
                .title("Turning Red")
                .runningTime(Duration.ofMinutes(85))
                .ticketPrice(11)
                .code(MovieCode.NORMAL)
                .build();

        final Movie theBatMan = Movie.builder()
                .id("3")
                .title("The Batman")
                .runningTime(Duration.ofMinutes(95))
                .ticketPrice(9)
                .code(MovieCode.NORMAL)
                .build();

        return List.of(spiderMan, turningRed, theBatMan);
    }

    // Some example showings to populate our in memory database with
    private static List<Showing> getShowings(final LocalDate currentDate) {
        return List.of(
                Showing.builder()
                        .id("11").movieId("2").sequenceOfTheDay(1)
                        .showStartTime(LocalDateTime.of(currentDate, LocalTime.of(9, 0))).build(),
                Showing.builder()
                        .id("22").movieId("1").sequenceOfTheDay(2)
                        .showStartTime(LocalDateTime.of(currentDate, LocalTime.of(11, 0))).build(),
                Showing.builder()
                        .id("33").movieId("3").sequenceOfTheDay(3)
                        .showStartTime(LocalDateTime.of(currentDate, LocalTime.of(12, 50))).build(),
                Showing.builder()
                        .id("44").movieId("2").sequenceOfTheDay(4)
                        .showStartTime(LocalDateTime.of(currentDate, LocalTime.of(14, 30))).build(),
                Showing.builder()
                        .id("55").movieId("1").sequenceOfTheDay(5)
                        .showStartTime(LocalDateTime.of(currentDate, LocalTime.of(16, 10))).build(),
                Showing.builder()
                        .id("66").movieId("3").sequenceOfTheDay(6)
                        .showStartTime(LocalDateTime.of(currentDate, LocalTime.of(17, 50))).build(),
                Showing.builder()
                        .id("77").movieId("2").sequenceOfTheDay(7)
                        .showStartTime(LocalDateTime.of(currentDate, LocalTime.of(19, 30))).build(),
                Showing.builder()
                        .id("88").movieId("1").sequenceOfTheDay(8)
                        .showStartTime(LocalDateTime.of(currentDate, LocalTime.of(21, 10))).build(),
                Showing.builder()
                        .id("99").movieId("3").sequenceOfTheDay(9)
                        .showStartTime(LocalDateTime.of(currentDate, LocalTime.of(23, 0))).build()
        );
    }
}
