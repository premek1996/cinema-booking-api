package com.example.cinemabooking.movie.service;

import com.example.cinemabooking.TestFixtures;
import com.example.cinemabooking.movie.dto.CreateMovieRequest;
import com.example.cinemabooking.movie.dto.MovieResponse;
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

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = TestFixtures.movieWithId();
    }

    @Test
    @DisplayName("should return list of movies when getAllMovies() is called")
    void shouldReturnAllMovies() {
        //given
        given(movieRepository.findAll()).willReturn(List.of(movie));
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
        given(movieRepository.findById(1L)).willReturn(Optional.of(movie));
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
        given(createMovieRequest.getTitle()).willReturn(movie.getTitle());
        given(movieRepository.findByTitle(movie.getTitle())).willReturn(Optional.empty());
        given(createMovieRequest.toMovie()).willReturn(movie);
        given(movieRepository.save(movie)).willReturn(movie);
        // when
        MovieResponse result = movieService.createMovie(createMovieRequest);
        // then
        assertThat(result.getTitle()).isEqualTo(movie.getTitle());
        verify(movieRepository).findByTitle(movie.getTitle());
        verify(movieRepository).save(movie);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("should throw exception when movie already exists")
    void shouldThrowExceptionWhenMovieAlreadyExists() {
        // given
        CreateMovieRequest createMovieRequest = mock(CreateMovieRequest.class);
        given(createMovieRequest.getTitle()).willReturn(movie.getTitle());
        given(movieRepository.findByTitle(movie.getTitle())).willReturn(Optional.of(movie));
        // when + then
        assertThatThrownBy(() -> movieService.createMovie(createMovieRequest))
                .isInstanceOf(MovieAlreadyExistsException.class);
        verify(movieRepository).findByTitle(movie.getTitle());
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("should delete movie when exists")
    void shouldDeleteMovie() {
        // given
        given(movieRepository.findById(1L)).willReturn(Optional.of(movie));
        // when
        movieService.deleteMovie(1L);
        // then
        verify(movieRepository).findById(1L);
        verify(movieRepository).delete(movie);
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

