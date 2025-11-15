package com.example.cinemabooking.showtime.service;

import com.example.cinemabooking.TestFixtures;
import com.example.cinemabooking.hall.entity.CinemaHall;
import com.example.cinemabooking.hall.service.CinemaHallService;
import com.example.cinemabooking.movie.entity.Movie;
import com.example.cinemabooking.movie.service.MovieService;
import com.example.cinemabooking.showtime.dto.CreateShowTimeRequest;
import com.example.cinemabooking.showtime.dto.ShowTimeResponse;
import com.example.cinemabooking.showtime.dto.UpdateShowTimeRequest;
import com.example.cinemabooking.showtime.entity.ShowTime;
import com.example.cinemabooking.showtime.repository.ShowTimeRepository;
import com.example.cinemabooking.showtime.service.exception.ShowTimeConflictException;
import com.example.cinemabooking.showtime.service.exception.ShowTimeInvalidTimeRangeException;
import com.example.cinemabooking.showtime.service.exception.ShowTimeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowTimeServiceTest {

    @Mock
    private ShowTimeRepository showTimeRepository;

    @Mock
    private MovieService movieService;

    @Mock
    private CinemaHallService cinemaHallService;

    @InjectMocks
    private ShowTimeService showTimeService;

    private Movie movie;
    private CinemaHall cinemaHall;
    private ShowTime showTime;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        movie = TestFixtures.movie();
        cinemaHall = TestFixtures.cinemaHallWithId();
        showTime = TestFixtures.showTime(now, movie, cinemaHall);
    }

    // ===========================================================
    // GET ALL
    // ===========================================================

    @Test
    @DisplayName("should return list of showtimes when getAllShowTimes() is called")
    void shouldReturnAllShowTimes() {
        // given
        given(showTimeRepository.findAll()).willReturn(List.of(showTime));
        // when
        List<ShowTimeResponse> result = showTimeService.getAllShowTimes();
        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getMovieTitle()).isEqualTo("Inception");
        verify(showTimeRepository).findAll();
        verifyNoMoreInteractions(showTimeRepository);
    }

    @Test
    @DisplayName("should return empty list when no showtimes exist")
    void shouldReturnEmptyListWhenNoShowTimesExist() {
        // given
        given(showTimeRepository.findAll()).willReturn(Collections.emptyList());
        // when
        List<ShowTimeResponse> result = showTimeService.getAllShowTimes();
        // then
        assertThat(result).isEmpty();
        verify(showTimeRepository).findAll();
        verifyNoMoreInteractions(showTimeRepository);
    }

    // ===========================================================
    // GET BY ID
    // ===========================================================

    @Test
    @DisplayName("should return showtime when found by id")
    void shouldReturnShowTimeById() {
        // given
        given(showTimeRepository.findById(100L)).willReturn(Optional.of(showTime));
        // when
        ShowTimeResponse result = showTimeService.getShowTimeById(100L);
        // then
        assertThat(result.getMovieTitle()).isEqualTo("Inception");
        verify(showTimeRepository).findById(100L);
        verifyNoMoreInteractions(showTimeRepository);
    }

    @Test
    @DisplayName("should throw ShowTimeNotFoundException when showtime does not exist")
    void shouldThrowExceptionWhenShowTimeNotFound() {
        // given
        given(showTimeRepository.findById(199L)).willReturn(Optional.empty());
        // when + then
        assertThatThrownBy(() -> showTimeService.getShowTimeById(199L))
                .isInstanceOf(ShowTimeNotFoundException.class);
        verify(showTimeRepository).findById(199L);
        verifyNoMoreInteractions(showTimeRepository);
    }

    // ===========================================================
    // GET BY MOVIE
    // ===========================================================

    @Test
    @DisplayName("should return showtimes for given movie id")
    void shouldReturnShowTimesByMovie() {
        // given
        given(showTimeRepository.findByMovieId(1L)).willReturn(List.of(showTime));
        //when
        List<ShowTimeResponse> result = showTimeService.getShowTimesByMovie(1L);
        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getMovieTitle()).isEqualTo("Inception");
        verify(showTimeRepository).findByMovieId(1L);
        verifyNoMoreInteractions(showTimeRepository);
    }

    // ===========================================================
    // GET BY HALL
    // ===========================================================

    @Test
    @DisplayName("should return showtimes for given hall id")
    void shouldReturnShowTimesByHall() {
        // given
        given(showTimeRepository.findByCinemaHallId(1L)).willReturn(List.of(showTime));
        //when
        List<ShowTimeResponse> result = showTimeService.getShowTimesByCinemaHall(1L);
        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getMovieTitle()).isEqualTo("Inception");
        verify(showTimeRepository).findByCinemaHallId(1L);
        verifyNoMoreInteractions(showTimeRepository);
    }

    // ===========================================================
    // GET BY DATE
    // ===========================================================

    @Test
    @DisplayName("should return showtimes for given date")
    void shouldReturnShowTimesByDate() {
        // given
        given(showTimeRepository.findByStartTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(List.of(showTime));
        // when
        List<ShowTimeResponse> result = showTimeService.getShowTimesByDate(LocalDate.now());
        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getMovieTitle()).isEqualTo("Inception");
        verify(showTimeRepository).findByStartTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class));
        verifyNoMoreInteractions(showTimeRepository);
    }

    // ===========================================================
    // CREATE
    // ===========================================================

    @Test
    @DisplayName("should create showtime when data is valid")
    void shouldCreateShowTime() {
        // given
        CreateShowTimeRequest createShowTimeRequest = CreateShowTimeRequest.builder()
                .movieId(1L)
                .cinemaHallId(1L)
                .startTime(now.plusHours(5))
                .endTime(now.plusHours(8))
                .price(BigDecimal.TEN)
                .build();
        given(movieService.getMovieOrThrow(1L)).willReturn(movie);
        given(cinemaHallService.getCinemaHallOrThrow(1L)).willReturn(cinemaHall);
        given(showTimeRepository.save(any(ShowTime.class))).willReturn(showTime);
        // when
        ShowTimeResponse result = showTimeService.createShowTime(createShowTimeRequest);
        // then
        assertThat(result.getMovieTitle()).isEqualTo("Inception");
        verify(showTimeRepository).save(any(ShowTime.class));
        verifyNoMoreInteractions(showTimeRepository);
    }

    @Test
    @DisplayName("should throw exception when endTime is before startTime during creation")
    void shouldThrowInvalidTimeRangeWhenCreating() {
        // given
        CreateShowTimeRequest createShowTimeRequest = CreateShowTimeRequest.builder()
                .movieId(1L)
                .cinemaHallId(1L)
                .startTime(now.plusHours(5))
                .endTime(now.plusHours(2))
                .price(BigDecimal.TEN)
                .build();
        given(movieService.getMovieOrThrow(1L)).willReturn(movie);
        given(cinemaHallService.getCinemaHallOrThrow(1L)).willReturn(cinemaHall);
        // when + then
        assertThatThrownBy(() -> showTimeService.createShowTime(createShowTimeRequest))
                .isInstanceOf(ShowTimeInvalidTimeRangeException.class);
        verifyNoInteractions(showTimeRepository);
    }

    @Test
    @DisplayName("should throw ShowTimeConflictException when hall already has conflicting showtime during creation")
    void shouldThrowConflictWhenCreatingShowTime() {
        // given
        CreateShowTimeRequest createShowTimeRequest = CreateShowTimeRequest.builder()
                .movieId(1L)
                .cinemaHallId(1L)
                .startTime(now.plusHours(1))
                .endTime(now.plusHours(3))
                .price(BigDecimal.TEN)
                .build();
        ShowTime conflictingShowTime = TestFixtures.conflictingShowTime(now, movie, cinemaHall);
        cinemaHall.addShowTime(conflictingShowTime);
        given(movieService.getMovieOrThrow(1L)).willReturn(movie);
        given(cinemaHallService.getCinemaHallOrThrow(1L)).willReturn(cinemaHall);
        // when + then
        assertThatThrownBy(() -> showTimeService.createShowTime(createShowTimeRequest))
                .isInstanceOf(ShowTimeConflictException.class);
        verifyNoInteractions(showTimeRepository);
    }

    // ===========================================================
    // UPDATE
    // ===========================================================

    @Test
    @DisplayName("should update showtime when data is valid")
    void shouldUpdateShowTime() {
        // given
        UpdateShowTimeRequest updateShowTimeRequest = UpdateShowTimeRequest.builder()
                .movieId(1L)
                .cinemaHallId(1L)
                .startTime(now.plusHours(2))
                .endTime(now.plusHours(4))
                .price(BigDecimal.TWO)
                .build();
        given(showTimeRepository.findById(100L)).willReturn(Optional.of(showTime));
        given(movieService.getMovieOrThrow(1L)).willReturn(movie);
        given(cinemaHallService.getCinemaHallOrThrow(1L)).willReturn(cinemaHall);
        // when
        ShowTimeResponse result = showTimeService.updateShowTime(100L, updateShowTimeRequest);
        // then
        assertThat(result.getMovieTitle()).isEqualTo("Inception");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.TWO);
        verify(showTimeRepository).findById(100L);
        verifyNoMoreInteractions(showTimeRepository);
    }

    @Test
    @DisplayName("should throw ShowTimeNotFoundException when updating nonexistent showtime")
    void shouldThrowWhenUpdatingNonexistentShowTime() {
        // given
        given(showTimeRepository.findById(100L)).willReturn(Optional.empty());
        // when + then
        assertThatThrownBy(() -> showTimeService.updateShowTime(100L, any(UpdateShowTimeRequest.class)))
                .isInstanceOf(ShowTimeNotFoundException.class);
        verify(showTimeRepository).findById(100L);
        verifyNoMoreInteractions(showTimeRepository);
    }

    @Test
    @DisplayName("should throw ShowTimeInvalidTimeRangeException when updating with invalid range")
    void shouldThrowInvalidTimeRangeWhenUpdatingShowTime() {
        // given
        UpdateShowTimeRequest updateShowTimeRequest = UpdateShowTimeRequest.builder()
                .movieId(1L)
                .cinemaHallId(1L)
                .startTime(now.plusHours(7))
                .endTime(now.plusHours(3))
                .price(BigDecimal.TWO)
                .build();
        given(showTimeRepository.findById(100L)).willReturn(Optional.of(showTime));
        given(movieService.getMovieOrThrow(1L)).willReturn(movie);
        given(cinemaHallService.getCinemaHallOrThrow(1L)).willReturn(cinemaHall);
        // when + then
        assertThatThrownBy(() -> showTimeService.updateShowTime(100L, updateShowTimeRequest))
                .isInstanceOf(ShowTimeInvalidTimeRangeException.class);
        verify(showTimeRepository).findById(100L);
        verifyNoMoreInteractions(showTimeRepository);
    }

    @Test
    @DisplayName("should throw ShowTimeConflictException when updating to conflicting time")
    void shouldThrowConflictWhenUpdatingShowTime() {
        // given
        UpdateShowTimeRequest updateShowTimeRequest = UpdateShowTimeRequest.builder()
                .movieId(1L)
                .cinemaHallId(1L)
                .startTime(now.plusHours(2))
                .endTime(now.plusHours(4))
                .price(BigDecimal.TWO)
                .build();
        ShowTime conflictingShowTime = TestFixtures.conflictingShowTime(now, movie, cinemaHall);
        cinemaHall.addShowTime(conflictingShowTime);
        given(showTimeRepository.findById(100L)).willReturn(Optional.of(showTime));
        given(movieService.getMovieOrThrow(1L)).willReturn(movie);
        given(cinemaHallService.getCinemaHallOrThrow(1L)).willReturn(cinemaHall);
        // when + then
        assertThatThrownBy(() -> showTimeService.updateShowTime(100L, updateShowTimeRequest))
                .isInstanceOf(ShowTimeConflictException.class);
        verify(showTimeRepository).findById(100L);
        verifyNoMoreInteractions(showTimeRepository);
    }

    // ===========================================================
    // DELETE
    // ===========================================================

    @Test
    @DisplayName("should delete showtime when exists")
    void shouldDeleteShowTime() {
        // given
        given(showTimeRepository.findById(100L)).willReturn(Optional.of(showTime));
        // when
        showTimeService.deleteShowTime(100L);
        // then
        verify(showTimeRepository).findById(100L);
        verify(showTimeRepository).delete(showTime);
        verifyNoMoreInteractions(showTimeRepository);
    }

    @Test
    @DisplayName("should throw ShowTimeNotFoundException when deleting nonexistent showtime")
    void shouldThrowWhenDeletingNonexistentShowTime() {
        // given
        given(showTimeRepository.findById(100L)).willReturn(Optional.empty());
        // when + then
        assertThatThrownBy(() -> showTimeService.deleteShowTime(100L))
                .isInstanceOf(ShowTimeNotFoundException.class);
        verify(showTimeRepository).findById(100L);
        verifyNoMoreInteractions(showTimeRepository);
    }

}