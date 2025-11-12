package com.example.cinemabooking.showtime.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class UpdateShowTimeRequest {

    @NotNull
    Long movieId;

    @NotNull
    Long cinemaHallId;

    @NotNull
    @Future
    LocalDateTime startTime;

    @NotNull
    @Future
    LocalDateTime endTime;

    @NotNull
    @Positive
    BigDecimal price;

}
