package com.example.cinemabooking.showtime.web;


import com.example.cinemabooking.showtime.dto.CreateShowTimeRequest;
import com.example.cinemabooking.showtime.dto.ShowTimeResponse;
import com.example.cinemabooking.showtime.dto.UpdateShowTimeRequest;
import com.example.cinemabooking.showtime.service.ShowTimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
public class ShowTimeController {

    private final ShowTimeService showTimeService;

    @GetMapping
    public List<ShowTimeResponse> getAllShowTimes() {
        return showTimeService.getAllShowTimes();
    }

    @GetMapping("/{id}")
    public ShowTimeResponse getShowTimeById(@PathVariable Long id) {
        return showTimeService.getShowTimeById(id);
    }

    @GetMapping("/movie/{movieId}")
    public List<ShowTimeResponse> getShowTimesByMovie(@PathVariable Long movieId) {
        return showTimeService.getShowTimesByMovie(movieId);
    }

    @GetMapping("/hall/{cinemaHallId}")
    public List<ShowTimeResponse> getShowTimesByCinemaHall(@PathVariable Long cinemaHallId) {
        return showTimeService.getShowTimesByCinemaHall(cinemaHallId);
    }

    @GetMapping("/date/{date}")
    public List<ShowTimeResponse> getShowTimesByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return showTimeService.getShowTimesByDate(date);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShowTimeResponse createShowTime(@Valid @RequestBody CreateShowTimeRequest request) {
        return showTimeService.createShowTime(request);
    }

    @PutMapping("/{id}")
    public ShowTimeResponse updateShowTime(@PathVariable Long id, @Valid @RequestBody UpdateShowTimeRequest request) {
        return showTimeService.updateShowTime(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShowTime(@PathVariable Long id) {
        showTimeService.deleteShowTime(id);
    }

}
