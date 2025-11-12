package com.example.cinemabooking.showtime.service;

import com.example.cinemabooking.hall.entity.CinemaHall;
import com.example.cinemabooking.hall.service.CinemaHallService;
import com.example.cinemabooking.movie.entity.Movie;
import com.example.cinemabooking.movie.service.MovieService;
import com.example.cinemabooking.showtime.dto.CreateShowTimeRequest;
import com.example.cinemabooking.showtime.dto.ShowTimeResponse;
import com.example.cinemabooking.showtime.entity.ShowTime;
import com.example.cinemabooking.showtime.repository.ShowTimeRepository;
import com.example.cinemabooking.showtime.service.exception.ShowTimeConflictException;
import com.example.cinemabooking.showtime.service.exception.ShowTimeInvalidTimeRangeException;
import com.example.cinemabooking.showtime.service.exception.ShowTimeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return showTimeRepository.findById(id).map(ShowTimeResponse::of).orElseThrow(() -> new ShowTimeNotFoundException(id));
    }

    @Transactional
    public ShowTimeResponse createShowTime(CreateShowTimeRequest request) {
        Movie movie = movieService.getMovieOrThrow(request.getMovieId());
        CinemaHall cinemaHall = cinemaHallService.getCinemaHallOrThrow(request.getCinemaHallId());
        validateEndTimeAfterStartTime(request.getStartTime(), request.getEndTime());
        validateNoTimeConflict(cinemaHall, request.getStartTime(), request.getEndTime());
        ShowTime showTime = request.toShowTime(movie, cinemaHall);
        return ShowTimeResponse.of(showTimeRepository.save(showTime));
    }

    private void validateEndTimeAfterStartTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new ShowTimeInvalidTimeRangeException(startTime, endTime);
        }
    }

    private void validateNoTimeConflict(CinemaHall cinemaHall, LocalDateTime startTime, LocalDateTime endTime) {
        if (cinemaHall.isOccupiedDuring(startTime, endTime)) {
            throw new ShowTimeConflictException(cinemaHall.getName(), startTime, endTime);
        }
    }

}
