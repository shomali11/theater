package com.jpmc.theater.services;

import com.google.inject.Singleton;
import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.Reservation;
import com.jpmc.theater.models.Showing;

import javax.inject.Inject;

@Singleton
public class ReservationService {

    private final MovieService movieService;
    private final CustomerService customerService;
    private final ShowingService showingService;

    @Inject
    public ReservationService(
            final MovieService movieService,
            final CustomerService customerService,
            final ShowingService showingService) {
        this.movieService = movieService;
        this.customerService = customerService;
        this.showingService = showingService;
    }

    public Reservation reserve(final String customerId, final String showingId, final int howManyTickets) {
        customerService.getCustomer(customerId);
        final Showing showing = showingService.getShowing(showingId);
        final Movie movie = movieService.getMovie(showing.getMovieId());
        final double totalFee = calculateFee(showing, movie, howManyTickets);
        return Reservation.builder()
                .customerId(customerId)
                .showingId(showingId)
                .movieId(movie.getId())
                .audienceCount(howManyTickets)
                .totalFee(totalFee)
                .build();
    }

    private double calculateFee(final Showing showing, final Movie movie, final int audienceCount) {
        return calculateTicketPrice(showing, movie) * audienceCount;
    }

    private double calculateTicketPrice(final Showing showing, final Movie movie) {
        return movie.getTicketPrice() - getDiscount(movie, showing);
    }

    private double getDiscount(final Movie movie, final Showing showing) {
        double specialDiscount = 0;
        if (movie.isSpecialCode()) {
            specialDiscount = movie.getTicketPrice() * 0.2;  // 20% discount for special movie
        }

        double timeDiscount = 0;
        if (showing.getShowStartTime().getHour() >= 11 && showing.getShowStartTime().getHour() <= 15) {
            timeDiscount = movie.getTicketPrice() * 0.25; // 25% discount for 11 AM to 4 PM (Not including 4 PM)
        }

        double sequenceDiscount = 0;
        final int showSequence = showing.getSequenceOfTheDay();
        if (showSequence == 1) {
            sequenceDiscount = 3; // $3 discount for 1st show
        } else if (showSequence == 2) {
            sequenceDiscount = 2; // $2 discount for 2nd show
        } else if (showSequence == 7) {
            sequenceDiscount = 1; // $1 discount for 7th show
        }

        // biggest discount wins
        return Math.max(specialDiscount, Math.max(timeDiscount, sequenceDiscount));
    }
}
