package com.example.cinemabooking.showtime.dto;

import com.example.cinemabooking.hall.entity.CinemaHall;
import com.example.cinemabooking.movie.entity.Movie;
import com.example.cinemabooking.showtime.entity.ShowTime;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class CreateShowTimeRequest {

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

    public ShowTime toShowTime(Movie movie, CinemaHall cinemaHall) {
        return ShowTime.builder()
                .movie(movie)
                .cinemaHall(cinemaHall)
                .startTime(startTime)
                .endTime(endTime)
                .price(price)
                .build();
    }

}
