package com.example.cinemabooking.showtime.service.exception;

public class ShowTimeNotFoundException extends RuntimeException {
    public ShowTimeNotFoundException(Long id) {
        super("Show time with id " + id + " not found.");
    }
}
