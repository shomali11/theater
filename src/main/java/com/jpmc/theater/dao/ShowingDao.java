package com.jpmc.theater.dao;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Singleton;
import com.jpmc.theater.models.Showing;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Singleton
public class ShowingDao {

    // This DAO (Data Access Object) Layer pretends to be interacting with a database
    // Our map here is going to represent a showing database table/store
    private final Map<String, Showing> showingMap = new TreeMap<>();
    private final Multimap<String, Integer> dateSequenceMap = HashMultimap.create();

    // Here we are adding a showing to our store using the ID as a primary key
    // We are ensuring that sequences are unique per day
    public void addShowing(final Showing showing) {
        final String id = showing.getId();
        if (showingMap.containsKey(id)) {
            throw new IllegalArgumentException("a showing already exists for given id " + id);
        }

        final String date = showing.getShowStartTime().toLocalDate().toString();
        if (dateSequenceMap.get(date).contains(showing.getSequenceOfTheDay())) {
            throw new IllegalArgumentException("a showing already exists on that day with this sequence " + id);
        }

        dateSequenceMap.put(date, showing.getSequenceOfTheDay());
        showingMap.put(showing.getId(), showing);
    }

    // Here we are retrieving a showing from our store using the ID
    public Showing getShowing(final String id) {
        if (!showingMap.containsKey(id)) {
            throw new IllegalArgumentException("not able to find any showing for given id " + id);
        }
        return showingMap.get(id);
    }

    // Here we are querying our store for the list of showing on a specific date
    public List<Showing> getShowings(final LocalDate currentDate) {
        final List<Showing> showings = new ArrayList<>();
        for (final Showing showing: showingMap.values()) {
            if (showing.getShowStartTime().toLocalDate().isEqual(currentDate)) {
                showings.add(showing);
            }
        }
        return showings;
    }
}
