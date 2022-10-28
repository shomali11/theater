package com.jpmc.theater.dao;

import com.google.inject.Singleton;
import com.jpmc.theater.models.Movie;

import java.util.Map;
import java.util.TreeMap;

@Singleton
public class MovieDao {

    // This DAO (Data Access Object) Layer pretends to be interacting with a database
    // Our map here is going to represent a movie database table/store
    private final Map<String, Movie> movieMap = new TreeMap<>();

    // Here we are adding a movie to our store using the ID as a primary key
    public void addMovie(final Movie movie) {
        final String id = movie.getId();
        if (movieMap.containsKey(id)) {
            throw new IllegalArgumentException("a movie already exists for given id " + id);
        }
        movieMap.put(movie.getId(), movie);
    }

    // Here we are retrieving a movie from our store using the ID
    public Movie getMovie(final String id) {
        if (!movieMap.containsKey(id)) {
            throw new IllegalArgumentException("not able to find any movie for given id " + id);
        }
        return movieMap.get(id);
    }
}
