package com.example.cinemabooking.showtime.dto;

import com.example.cinemabooking.showtime.entity.ShowTime;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class ShowTimeResponse {

    Long id;
    Long movieId;
    String movieTitle;
    Long cinemaHallId;
    String cinemaHallName;
    LocalDateTime startTime;
    LocalDateTime endTime;
    BigDecimal price;

    public static ShowTimeResponse of(ShowTime showTime) {
        return ShowTimeResponse.builder()
                .id(showTime.getId())
                .movieId(showTime.getMovie().getId())
                .movieTitle(showTime.getMovie().getTitle())
                .cinemaHallId(showTime.getCinemaHall().getId())
                .cinemaHallName(showTime.getCinemaHall().getName())
                .startTime(showTime.getStartTime())
                .endTime(showTime.getEndTime())
                .price(showTime.getPrice())
                .build();
    }

}


