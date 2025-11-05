package com.example.cinemabooking.movie.repository;

import com.example.cinemabooking.movie.entity.AgeRating;
import com.example.cinemabooking.movie.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    private Movie sampleMovie;

    @BeforeEach
    void setUp() {
        sampleMovie = Movie.builder()
                .title("Inception")
                .description("Dream within a dream")
                .genre("Sci-Fi")
                .durationMinutes(148)
                .releaseDate(LocalDate.of(2010, 7, 16))
                .ageRating(AgeRating.AGE_12)
                .build();
    }

    @Test
    @DisplayName("should find movie by title when exists")
    void shouldFindMovieByTitleWhenExists() {
        // given
        movieRepository.save(sampleMovie);
        // when
        Optional<Movie> result = movieRepository.findByTitle(sampleMovie.getTitle());
        // then
        assertThat(result).isPresent();
        assertThat(result.get().getGenre()).isEqualTo("Sci-Fi");
        assertThat(result.get().getDurationMinutes()).isEqualTo(148);
    }

    @Test
    @DisplayName("should return empty when movie with given title does not exist")
    void shouldReturnEmptyWhenMovieNotFoundByTitle() {
        // given
        movieRepository.save(sampleMovie);
        // when
        Optional<Movie> result = movieRepository.findByTitle("Missing title");
        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should not allow saving two movies with the same title")
    void shouldNotAllowSavingTwoMoviesWithSameTitle() {
        // given
        movieRepository.save(sampleMovie);

        Movie duplicate = Movie.builder()
                .title("Inception")
                .description("Duplicate entry")
                .genre("Action")
                .durationMinutes(120)
                .releaseDate(LocalDate.now())
                .ageRating(AgeRating.AGE_18)
                .build();
        // when + then
        assertThatThrownBy(() -> movieRepository.saveAndFlush(duplicate))
                .isInstanceOf(Exception.class);
    }

}