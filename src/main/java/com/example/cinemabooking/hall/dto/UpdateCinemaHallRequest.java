package com.example.cinemabooking.hall.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateCinemaHallRequest {

    @NotBlank
    String name;

    @Min(1)
    int rows;

    @Min(1)
    int seatsPerRow;

}
