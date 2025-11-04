package com.example.cinemabooking.hall.dto;

import com.example.cinemabooking.hall.entity.CinemaHall;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateCinemaHallRequest {

    @NotBlank
    String name;

    @Min(1)
    int rows;

    @Min(1)
    int seatsPerRow;

    public CinemaHall toCinemaHall() {
        return CinemaHall.builder()
                .name(name)
                .rows(rows)
                .seatsPerRow(seatsPerRow)
                .build();
    }

}
