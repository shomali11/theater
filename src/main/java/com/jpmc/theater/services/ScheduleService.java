package com.jpmc.theater.services;

import com.google.inject.Singleton;
import com.jpmc.theater.models.Movie;
import com.jpmc.theater.models.MovieShowing;
import com.jpmc.theater.models.Schedule;
import com.jpmc.theater.models.Showing;

import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
public class ScheduleService {

    private final MovieService movieService;
    private final ShowingService showingService;

    @Inject
    public ScheduleService(
            final MovieService movieService,
            final ShowingService showingService) {
        this.movieService = movieService;
        this.showingService = showingService;
    }

    public Schedule getSchedule(final LocalDate currentDate) {
        final Collection<Showing> showings = showingService.getShowings(currentDate);
        final Map<String, Movie> movieMap = getMovieMap(showings);

        final List<MovieShowing> movieShowingList = new ArrayList<>();
        for (final Showing showing: showings) {
            final Movie movie = movieMap.get(showing.getMovieId());
            movieShowingList.add(
                    MovieShowing.builder()
                            .sequenceOfTheDay(showing.getSequenceOfTheDay())
                            .showStartTime(showing.getShowStartTime().toString())
                            .title(movie.getTitle())
                            .runningTime(humanReadableFormat(movie.getRunningTime()))
                            .ticketPrice(movie.getTicketPrice())
                            .build());
        }
        return Schedule.builder().date(currentDate.toString()).movieShowingList(movieShowingList).build();
    }

    private Map<String, Movie> getMovieMap(final Collection<Showing> showings) {
        final Set<String> uniqueMovies = new HashSet<>();
        for (final Showing showing: showings) {
            uniqueMovies.add(showing.getMovieId());
        }

        final Map<String, Movie> movieMap = new HashMap<>();
        for (final String movieId: uniqueMovies) {
            movieMap.put(movieId, movieService.getMovie(movieId));
        }
        return movieMap;
    }

    private String humanReadableFormat(final Duration duration) {
        final long hour = duration.toHours();
        final long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());
        return String.format("%s hour%s %s minute%s", hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
    }

    // (s) postfix should be added to handle plural correctly
    private String handlePlural(final long value) {
        if (value == 1) {
            return "";
        } else {
            return "s";
        }
    }
}
