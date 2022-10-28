package com.jpmc.theater.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Movie {
    private String id;
    private String title;
    private String description;
    private Duration runningTime;
    private double ticketPrice;
    private MovieCode code;

    public boolean isSpecialCode() {
        return code == MovieCode.SPECIAL;
    }
}