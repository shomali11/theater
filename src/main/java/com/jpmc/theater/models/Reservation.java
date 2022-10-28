package com.jpmc.theater.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Reservation {
    private final String customerId;
    private final String showingId;
    private final String movieId;
    private final int audienceCount;
    private final double totalFee;
}