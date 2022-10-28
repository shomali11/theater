package com.jpmc.theater.printers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import com.jpmc.theater.models.MovieShowing;
import com.jpmc.theater.models.Schedule;

@Singleton
public class SchedulePrinter {

    public void printJsonSchedule(final Schedule schedule) {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final JsonElement jsonElement = JsonParser.parseString(gson.toJson(schedule));
        System.out.println();
        System.out.println("===================================================");
        System.out.println(gson.toJson(jsonElement));
        System.out.println("===================================================");
        System.out.println();
    }

    public void printTextSchedule(final Schedule schedule) {
        System.out.println();
        System.out.println(schedule.getDate());
        System.out.println("===================================================");
        for (final MovieShowing movieShowing: schedule.getMovieShowingList()) {
            System.out.println(
                    movieShowing.getSequenceOfTheDay() + ": " +
                            movieShowing.getShowStartTime() + " " +
                            movieShowing.getTitle() + " " +
                            "(" + movieShowing.getRunningTime() + ") " +
                            "$" + movieShowing.getTicketPrice());
        }
        System.out.println("===================================================");
        System.out.println();
    }
}
