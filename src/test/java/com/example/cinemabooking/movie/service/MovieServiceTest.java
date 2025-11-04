package com.example.cinemabooking.movie.service;

import com.example.cinemabooking.movie.dto.CreateMovieRequest;
import com.example.cinemabooking.movie.dto.MovieResponse;
import com.example.cinemabooking.movie.entity.AgeRating;
import com.example.cinemabooking.movie.entity.Movie;
import com.example.cinemabooking.movie.repository.MovieRepository;
import com.example.cinemabooking.movie.service.exception.MovieAlreadyExistsException;
import com.example.cinemabooking.movie.service.exception.MovieNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie sampleMovie;

    @BeforeEach
    void setUp() {
        sampleMovie = Movie.builder()
                .id(1L)
                .title("Inception")
                .genre("Sci-Fi")
                .durationMinutes(148)
                .description("Dream within a dream")
                .ageRating(AgeRating.AGE_12)
                .releaseDate(LocalDate.of(2005, 10, 12))
                .build();
    }

    @Test
    @DisplayName("should return list of movies when getAllMovies() is called")
    void shouldReturnAllMovies() {
        //given
        given(movieRepository.findAll()).willReturn(List.of(sampleMovie));
        //when
        List<MovieResponse> result = movieService.getAllMovies();
        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo("Inception");
        verify(movieRepository).findAll();
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("should return empty list when no movies exist")
    void shouldReturnEmptyListWhenNoMoviesExist() {
        //given
        given(movieRepository.findAll()).willReturn(List.of());
        //when
        List<MovieResponse> result = movieService.getAllMovies();
        //then
        assertThat(result).isEmpty();
        verify(movieRepository).findAll();
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("should return movie when found by id")
    void shouldReturnMovieById() {
        //given
        given(movieRepository.findById(1L)).willReturn(Optional.of(sampleMovie));
        //when
        MovieResponse result = movieService.getMovieById(1L);
        //then
        assertThat(result.getTitle()).isEqualTo("Inception");
        verify(movieRepository).findById(1L);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("should throw MovieNotFoundException when movie does not exist")
    void shouldThrowExceptionWhenMovieNotFound() {
        //given
        given(movieRepository.findById(1L)).willReturn(Optional.empty());
        //when + then
        assertThatThrownBy(() -> movieService.getMovieById(1L))
                .isInstanceOf(MovieNotFoundException.class);
        verify(movieRepository).findById(1L);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("should save movie when title not exists")
    void shouldSaveNewMovie() {
        // given
        CreateMovieRequest createMovieRequest = mock(CreateMovieRequest.class);
        given(createMovieRequest.getTitle()).willReturn(sampleMovie.getTitle());
        given(movieRepository.findByTitle(sampleMovie.getTitle())).willReturn(Optional.empty());
        given(createMovieRequest.toMovie()).willReturn(sampleMovie);
        given(movieRepository.save(sampleMovie)).willReturn(sampleMovie);
        // when
        MovieResponse result = movieService.createMovie(createMovieRequest);
        // then
        assertThat(result.getTitle()).isEqualTo(sampleMovie.getTitle());
        verify(movieRepository).findByTitle(sampleMovie.getTitle());
        verify(movieRepository).save(sampleMovie);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("should throw exception when movie already exists")
    void shouldThrowExceptionWhenMovieAlreadyExists() {
        // given
        CreateMovieRequest createMovieRequest = mock(CreateMovieRequest.class);
        given(createMovieRequest.getTitle()).willReturn(sampleMovie.getTitle());
        given(movieRepository.findByTitle(sampleMovie.getTitle())).willReturn(Optional.of(sampleMovie));
        // when + then
        assertThatThrownBy(() -> movieService.createMovie(createMovieRequest))
                .isInstanceOf(MovieAlreadyExistsException.class);
        verify(movieRepository).findByTitle(sampleMovie.getTitle());
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("should delete movie when exists")
    void shouldDeleteMovie() {
        // given
        given(movieRepository.findById(1L)).willReturn(Optional.of(sampleMovie));
        // when
        movieService.deleteMovie(1L);
        // then
        verify(movieRepository).findById(1L);
        verify(movieRepository).delete(sampleMovie);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("should throw MovieNotFoundException when deleting nonexistent movie")
    void shouldThrowWhenDeletingNonexistentMovie() {
        // given
        given(movieRepository.findById(1L)).willReturn(Optional.empty());
        // when + then
        assertThatThrownBy(() -> movieService.deleteMovie(1L))
                .isInstanceOf(MovieNotFoundException.class);
        verify(movieRepository).findById(1L);
        verifyNoMoreInteractions(movieRepository);
    }

}

