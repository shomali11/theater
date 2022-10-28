package com.jpmc.theater.services;

import com.google.inject.Singleton;
import com.jpmc.theater.dao.MovieDao;
import com.jpmc.theater.models.Movie;

import javax.inject.Inject;

@Singleton
public class MovieService {

    private final MovieDao movieDao;

    @Inject
    public MovieService(final MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public void addMovie(final Movie movie) {
        movieDao.addMovie(movie);
    }

    public Movie getMovie(final String id) {
        return movieDao.getMovie(id);
    }
}
