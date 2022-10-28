package com.jpmc.theater.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Showing {
    private final String id;
    private final String movieId;
    private final int sequenceOfTheDay;
    private final LocalDateTime showStartTime;
}
