package com.example.cinemabooking.showtime.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowTimeService {

    private final ShowTimeRepository showTimeRepository;
    private final MovieService movieService;
    private final CinemaHallService cinemaHallService;

    @Transactional(readOnly = true)
    public List<ShowTimeResponse> getAllShowTimes() {
        return showTimeRepository.findAll().stream().map(ShowTimeResponse::of).toList();
    }

    @Transactional(readOnly = true)
    public ShowTimeResponse getShowTimeById(Long id) {
        ShowTime showTime = getShowTimeOrThrow(id);
        return ShowTimeResponse.of(showTime);
    }

    @Transactional(readOnly = true)
    public ShowTime getShowTimeOrThrow(Long id) {
        return showTimeRepository.findById(id)
                .orElseThrow(() -> new ShowTimeNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<ShowTimeResponse> getShowTimesByMovie(Long movieId) {
        return showTimeRepository.findByMovieId(movieId).stream().map(ShowTimeResponse::of).toList();
    }

    @Transactional(readOnly = true)
    public List<ShowTimeResponse> getShowTimesByCinemaHall(Long cinemaHallId) {
        return showTimeRepository.findByCinemaHallId(cinemaHallId).stream().map(ShowTimeResponse::of).toList();
    }

    @Transactional(readOnly = true)
    public List<ShowTimeResponse> getShowTimesByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return showTimeRepository.findByStartTimeBetween(startOfDay, endOfDay).stream().map(ShowTimeResponse::of).toList();
    }

    @Transactional
    public ShowTimeResponse createShowTime(CreateShowTimeRequest request) {
        Movie movie = movieService.getMovieOrThrow(request.getMovieId());
        CinemaHall cinemaHall = cinemaHallService.getCinemaHallOrThrow(request.getCinemaHallId());
        validateShowTime(cinemaHall, request.getStartTime(), request.getEndTime(), null);
        ShowTime showTime = request.toShowTime(movie, cinemaHall);
        return ShowTimeResponse.of(showTimeRepository.save(showTime));
    }

    private void validateShowTime(CinemaHall cinemaHall, LocalDateTime startTime, LocalDateTime endTime, Long currentShowTimeId) {
        validateEndTimeAfterStartTime(startTime, endTime);
        validateNoTimeConflict(cinemaHall, startTime, endTime, currentShowTimeId);
    }

    private void validateEndTimeAfterStartTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new ShowTimeInvalidTimeRangeException(startTime, endTime);
        }
    }

    private void validateNoTimeConflict(CinemaHall cinemaHall, LocalDateTime startTime, LocalDateTime endTime, Long currentShowTimeId) {
        if (cinemaHall.isOccupiedDuring(startTime, endTime, currentShowTimeId)) {
            throw new ShowTimeConflictException(cinemaHall.getName(), startTime, endTime);
        }
    }

    @Transactional
    public ShowTimeResponse updateShowTime(Long id, UpdateShowTimeRequest request) {
        ShowTime showTime = getShowTimeOrThrow(id);
        Movie movie = movieService.getMovieOrThrow(request.getMovieId());
        CinemaHall cinemaHall = cinemaHallService.getCinemaHallOrThrow(request.getCinemaHallId());
        validateShowTime(cinemaHall, request.getStartTime(), request.getEndTime(), id);
        applyUpdates(showTime, movie, cinemaHall, request);
        return ShowTimeResponse.of(showTime);
    }

    private void applyUpdates(ShowTime showTime, Movie movie, CinemaHall cinemaHall, UpdateShowTimeRequest request) {
        showTime.setMovie(movie);
        showTime.setCinemaHall(cinemaHall);
        showTime.setStartTime(request.getStartTime());
        showTime.setEndTime(request.getEndTime());
        showTime.setPrice(request.getPrice());
    }

    @Transactional
    public void deleteShowTime(Long id) {
        ShowTime showTime = getShowTimeOrThrow(id);
        showTimeRepository.delete(showTime);
    }

}
