package com.jpmc.theater.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Schedule {
    private final String date;
    private final List<MovieShowing> movieShowingList;
}