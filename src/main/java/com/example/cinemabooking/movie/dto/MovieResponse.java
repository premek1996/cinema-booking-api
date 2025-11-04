package com.example.cinemabooking.movie.dto;


import com.example.cinemabooking.movie.entity.AgeRating;
import com.example.cinemabooking.movie.entity.Movie;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class MovieResponse {

    Long id;
    String title;
    String description;
    String genre;
    int durationMinutes;
    LocalDate releaseDate;
    AgeRating ageRating;

    public static MovieResponse of(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .genre(movie.getGenre())
                .durationMinutes(movie.getDurationMinutes())
                .releaseDate(movie.getReleaseDate())
                .ageRating(movie.getAgeRating())
                .build();
    }

}
