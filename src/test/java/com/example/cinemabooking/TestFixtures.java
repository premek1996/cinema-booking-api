package com.example.cinemabooking;

import com.example.cinemabooking.hall.entity.CinemaHall;
import com.example.cinemabooking.movie.entity.AgeRating;
import com.example.cinemabooking.movie.entity.Movie;
import com.example.cinemabooking.showtime.entity.ShowTime;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestFixtures {

    public static CinemaHall cinemaHall() {
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setName("Hall A");
        cinemaHall.setRows(5);
        cinemaHall.setSeatsPerRow(10);
        return cinemaHall;
    }

    public static CinemaHall cinemaHallWithId() {
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setId(1L);
        cinemaHall.setName("Hall A");
        cinemaHall.setRows(5);
        cinemaHall.setSeatsPerRow(10);
        return cinemaHall;
    }

    public static Movie movie() {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setDescription("Dreams");
        movie.setGenre("Sci-Fi");
        movie.setDurationMinutes(148);
        movie.setReleaseDate(LocalDate.of(2010, 7, 16));
        movie.setAgeRating(AgeRating.AGE_12);
        return movie;
    }

    public static Movie movieWithId() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setDescription("Dreams");
        movie.setGenre("Sci-Fi");
        movie.setDurationMinutes(148);
        movie.setReleaseDate(LocalDate.of(2010, 7, 16));
        movie.setAgeRating(AgeRating.AGE_12);
        return movie;
    }

    public static Movie movieWithDuplicatedTitle() {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setDescription("Duplicate entry");
        movie.setGenre("Action");
        movie.setDurationMinutes(120);
        movie.setReleaseDate(LocalDate.of(2012, 7, 16));
        movie.setAgeRating(AgeRating.AGE_18);
        return movie;
    }

    public static ShowTime showTimeWithoutId(LocalDateTime now, Movie movie, CinemaHall cinemaHall) {
        ShowTime showTime = new ShowTime();
        showTime.setMovie(movie);
        showTime.setCinemaHall(cinemaHall);
        showTime.setStartTime(now);
        showTime.setEndTime(now.plusHours(3));
        showTime.setPrice(BigDecimal.TEN);
        return showTime;
    }

    public static ShowTime showTime(LocalDateTime now, Movie movie, CinemaHall cinemaHall) {
        ShowTime showTime = new ShowTime();
        showTime.setId(100L);
        showTime.setMovie(movie);
        showTime.setCinemaHall(cinemaHall);
        showTime.setStartTime(now);
        showTime.setEndTime(now.plusHours(3));
        showTime.setPrice(BigDecimal.TEN);
        return showTime;
    }

    public static ShowTime conflictingShowTime(LocalDateTime now, Movie movie, CinemaHall cinemaHall) {
        ShowTime showTime = new ShowTime();
        showTime.setId(99L);
        showTime.setMovie(movie);
        showTime.setCinemaHall(cinemaHall);
        showTime.setStartTime(now.plusHours(2));
        showTime.setEndTime(now.plusHours(3));
        showTime.setPrice(BigDecimal.TEN);
        return showTime;
    }

}
