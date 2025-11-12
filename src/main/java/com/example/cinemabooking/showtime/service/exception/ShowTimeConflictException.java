package com.example.cinemabooking.showtime.service.exception;

import java.time.LocalDateTime;

public class ShowTimeConflictException extends RuntimeException {
    public ShowTimeConflictException(String hallName, LocalDateTime startTime, LocalDateTime endTime) {
        super("Cinema hall '" + hallName + "' is already occupied between " + startTime + " and " + endTime + ".");
    }
}
