package com.jpmc.theater.services;

import com.jpmc.theater.dao.CustomerDao;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReservationServiceTests {

    private static final MovieDao movieDao = new MovieDao();
    private static final ShowingDao showingDao = new ShowingDao();
    private static final CustomerDao customerDao = new CustomerDao();
    private static final MovieService movieService = new MovieService(movieDao);
    private static final ShowingService showingService = new ShowingService(showingDao);
    private static final CustomerService customerService = new CustomerService(customerDao);
    private static final ReservationService reservationService = new ReservationService(movieService, customerService, showingService);

    @BeforeAll
    static void setup() {
        customerService.addCustomer(getCustomer());

        for (final Movie movie: getMovies()) {
            movieService.addMovie(movie);
        }

        final List<Showing> showings = getShowings();
        for (final Showing showing: showings) {
            showingService.addShowing(showing);
        }
    }

    @Test
    void testReserve1stShow() {
        final Reservation reservation = reservationService.reserve("Raed-11", "11", 1);

        assertEquals("Raed-11", reservation.getCustomerId());
        assertEquals("11", reservation.getShowingId());
        assertEquals("2", reservation.getMovieId());
        assertEquals(1, reservation.getAudienceCount());

        // Original movie ticket price was $11
        // The fee is $8 after the discount ($3 for being first show).
        // No other discounts were considered.
        assertEquals(8, reservation.getTotalFee());
    }

    @Test
    void testReserve2ndShow() {
        final Reservation reservation = reservationService.reserve("Raed-11", "22", 1);

        assertEquals("Raed-11", reservation.getCustomerId());
        assertEquals("22", reservation.getShowingId());
        assertEquals("1", reservation.getMovieId());
        assertEquals(1, reservation.getAudienceCount());

        // Original movie ticket price was $12.5
        // The fee is $9.375 after the discount (%25 for being between 11 AM and 4 PM).
        // The 20% discount for being special and $2 2nd showing discount were considered.
        // But the time based discount was the maximum
        assertEquals(9.375, reservation.getTotalFee());
    }

    @Test
    void testReserve3rdShow() {
        final Reservation reservation = reservationService.reserve("Raed-11", "33", 1);

        assertEquals("Raed-11", reservation.getCustomerId());
        assertEquals("33", reservation.getShowingId());
        assertEquals("3", reservation.getMovieId());
        assertEquals(1, reservation.getAudienceCount());

        // Original movie ticket price was $9
        // The fee is $6.75 after the discount (%25 for being between 11 AM and 4 PM).
        // No other discounts were considered
        assertEquals(6.75, reservation.getTotalFee());
    }

    @Test
    void testReserve4thShow() {
        final Reservation reservation = reservationService.reserve("Raed-11", "44", 1);

        assertEquals("Raed-11", reservation.getCustomerId());
        assertEquals("44", reservation.getShowingId());
        assertEquals("2", reservation.getMovieId());
        assertEquals(1, reservation.getAudienceCount());

        // Original movie ticket price was $11
        // The fee is $8.25 after the discount (%25 for being between 11 AM and 4 PM).
        // No other discounts were considered
        assertEquals(8.25, reservation.getTotalFee());
    }

    @Test
    void testReserve5thShow() {
        final Reservation reservation = reservationService.reserve("Raed-11", "55", 1);

        assertEquals("Raed-11", reservation.getCustomerId());
        assertEquals("55", reservation.getShowingId());
        assertEquals("1", reservation.getMovieId());
        assertEquals(1, reservation.getAudienceCount());

        // Original movie ticket price was $12.5
        // The fee is $10 after the discount (%20 for being special).
        // No other discounts were considered
        assertEquals(10, reservation.getTotalFee());
    }

    @Test
    void testReserve6thShow() {
        final Reservation reservation = reservationService.reserve("Raed-11", "66", 1);

        assertEquals("Raed-11", reservation.getCustomerId());
        assertEquals("66", reservation.getShowingId());
        assertEquals("3", reservation.getMovieId());
        assertEquals(1, reservation.getAudienceCount());

        // Original movie ticket price was $9
        // No discounts were applied
        assertEquals(9, reservation.getTotalFee());
    }

    @Test
    void testReserve7thShow() {
        final Reservation reservation = reservationService.reserve("Raed-11", "77", 1);

        assertEquals("Raed-11", reservation.getCustomerId());
        assertEquals("77", reservation.getShowingId());
        assertEquals("2", reservation.getMovieId());
        assertEquals(1, reservation.getAudienceCount());

        // Original movie ticket price was $11
        // The fee is $10 after the discount ($1 for being 7th show).
        // No other discounts were considered
        assertEquals(10, reservation.getTotalFee());
    }

    @Test
    void testReserve8thShow() {
        final Reservation reservation = reservationService.reserve("Raed-11", "88", 1);

        assertEquals("Raed-11", reservation.getCustomerId());
        assertEquals("88", reservation.getShowingId());
        assertEquals("1", reservation.getMovieId());
        assertEquals(1, reservation.getAudienceCount());

        // Original movie ticket price was $12.5
        // The fee is $10 after the discount (%20 for being special).
        // No other discounts were considered
        assertEquals(10, reservation.getTotalFee());
    }

    @Test
    void testReserve9thShow() {
        final Reservation reservation = reservationService.reserve("Raed-11", "99", 1);

        assertEquals("Raed-11", reservation.getCustomerId());
        assertEquals("99", reservation.getShowingId());
        assertEquals("3", reservation.getMovieId());
        assertEquals(1, reservation.getAudienceCount());

        // Original movie ticket price was $9
        // No discounts were applied
        assertEquals(9, reservation.getTotalFee());
    }

    @Test
    void testReserveMultipleTickets1stShow() {
        final Reservation reservation = reservationService.reserve("Raed-11", "11", 3);

        assertEquals("Raed-11", reservation.getCustomerId());
        assertEquals("11", reservation.getShowingId());
        assertEquals("2", reservation.getMovieId());
        assertEquals(3, reservation.getAudienceCount());

        // Original movie ticket price was $11
        // The fee is $8 after the discount ($3 for being first show).
        // No other discounts were considered.
        assertEquals(24.0, reservation.getTotalFee());
    }

    @Test
    void testReserveMultipleTickets2ndShow() {
        final Reservation reservation = reservationService.reserve("Raed-11", "22", 4);

        assertEquals("Raed-11", reservation.getCustomerId());
        assertEquals("22", reservation.getShowingId());
        assertEquals("1", reservation.getMovieId());
        assertEquals(4, reservation.getAudienceCount());

        // Original movie ticket price was $12.5
        // The fee is $9.375 after the discount (%25 for being between 11 AM and 4 PM).
        // The 20% discount for being special and $2 2nd showing discount were considered.
        // But the time based discount was the maximum
        assertEquals(37.5, reservation.getTotalFee());
    }

    @Test
    void testReserveNonExistentShow() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.reserve("Raed-11", "unknown", 1);
        });
    }

    private static Customer getCustomer() {
        return Customer.builder().id("Raed-11").name("Raed Shomali").build();
    }

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

    private static List<Showing> getShowings() {
        final LocalDateProvider provider = LocalDateProvider.singleton();
        final LocalDate currentDate = provider.currentDate();
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
