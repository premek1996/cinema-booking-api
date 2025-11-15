package com.example.cinemabooking.hall.dto;

import com.example.cinemabooking.hall.entity.CinemaHall;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CinemaHallResponse {

    Long id;
    String name;
    int rows;
    int seatsPerRow;
    List<SeatResponse> seats;

    public static CinemaHallResponse of(CinemaHall cinemaHall) {
        return CinemaHallResponse.builder()
                .id(cinemaHall.getId())
                .name(cinemaHall.getName())
                .rows(cinemaHall.getRows())
                .seatsPerRow(cinemaHall.getSeatsPerRow())
                .seats(getSeatsResponse(cinemaHall))
                .build();
    }

    private static List<SeatResponse> getSeatsResponse(CinemaHall cinemaHall) {
        return cinemaHall.getSeats()
                .stream()
                .map(SeatResponse::of)
                .toList();
    }

}
