package com.example.cinemabooking.showtime.service.exception;

import java.time.LocalDateTime;

public class ShowTimeInvalidTimeRangeException extends RuntimeException {
    public ShowTimeInvalidTimeRangeException(LocalDateTime startTime, LocalDateTime endTime) {
        super("Show time endTime (" + endTime + ") must be after startTime (" + startTime + ").");
    }
}
