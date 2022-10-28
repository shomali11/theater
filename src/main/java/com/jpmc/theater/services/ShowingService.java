package com.jpmc.theater.services;

import com.google.inject.Singleton;
import com.jpmc.theater.dao.ShowingDao;
import com.jpmc.theater.models.Showing;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Collection;

@Singleton
public class ShowingService {

    private final ShowingDao showingDao;

    @Inject
    public ShowingService(final ShowingDao showingDao) {
        this.showingDao = showingDao;
    }

    public void addShowing(final Showing showing) {
        showingDao.addShowing(showing);
    }

    public Showing getShowing(final String id) {
        return showingDao.getShowing(id);
    }

    public Collection<Showing> getShowings(final LocalDate currentDate) {
        return showingDao.getShowings(currentDate);
    }
}
