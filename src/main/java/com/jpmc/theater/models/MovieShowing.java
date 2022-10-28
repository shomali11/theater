package com.jpmc.theater.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class MovieShowing {
    private final int sequenceOfTheDay;
    private final String showStartTime;
    private final String title;
    private final String runningTime;
    private final double ticketPrice;
}