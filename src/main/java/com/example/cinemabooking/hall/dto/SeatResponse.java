package com.example.cinemabooking.hall.dto;

import com.example.cinemabooking.hall.entity.Seat;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SeatResponse {

    int rowNumber;
    int seatNumber;

    public static SeatResponse of(Seat seat) {
        return SeatResponse.builder()
                .rowNumber(seat.getRowNumber())
                .seatNumber(seat.getSeatNumber())
                .build();
    }

}
