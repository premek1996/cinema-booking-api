package com.example.cinemabooking.showtime.web;

import com.example.cinemabooking.hall.entity.CinemaHall;
import com.example.cinemabooking.hall.repository.CinemaHallRepository;
import com.example.cinemabooking.movie.entity.AgeRating;
import com.example.cinemabooking.movie.entity.Movie;
import com.example.cinemabooking.movie.repository.MovieRepository;
import com.example.cinemabooking.showtime.entity.ShowTime;
import com.example.cinemabooking.showtime.repository.ShowTimeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ShowTimeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShowTimeRepository showTimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    private Movie movie;
    private CinemaHall hall;
    private ShowTime showtime;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        movie = Movie.builder()
                .title("Inception")
                .description("Dreams")
                .genre("Sci-Fi")
                .durationMinutes(148)
                .releaseDate(LocalDate.of(2010, 7, 16))
                .ageRating(AgeRating.AGE_12)
                .build();
        movieRepository.save(movie);

        hall = CinemaHall.builder()
                .name("Hall A")
                .rows(5)
                .seatsPerRow(10)
                .build();
        cinemaHallRepository.save(hall);

        showtime = ShowTime.builder()
                .movie(movie)
                .cinemaHall(hall)
                .startTime(now.plusHours(1))
                .endTime(now.plusHours(3))
                .price(BigDecimal.TEN)
                .build();
        showTimeRepository.save(showtime);
    }


    // --------------------------------------------------------------------
    // GET /api/showtimes
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should return empty list when no showtimes exist")
    void shouldReturnEmptyListWhenNoShowTimesExist() throws Exception {
        // TODO: clear DB, perform GET, assert empty JSON array
    }

    @Test
    @DisplayName("should return list of showtimes when they exist")
    void shouldReturnListOfShowTimes() throws Exception {
        // TODO: perform GET, assert fields
    }


    // --------------------------------------------------------------------
    // GET /api/showtimes/{id}
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should return showtime when found by id")
    void shouldReturnShowTimeById() throws Exception {
        // TODO: perform GET with saved id
    }

    @Test
    @DisplayName("should return 404 when showtime not found by id")
    void shouldReturn404WhenShowTimeNotFound() throws Exception {
        // TODO: GET nonexistent id, assert 404 + response body
    }


    // --------------------------------------------------------------------
    // GET /api/showtimes/movie/{movieId}
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should return showtimes by movie id")
    void shouldReturnShowTimesByMovie() throws Exception {
        // TODO
    }


    // --------------------------------------------------------------------
    // GET /api/showtimes/hall/{hallId}
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should return showtimes by hall id")
    void shouldReturnShowTimesByHall() throws Exception {
        // TODO
    }


    // --------------------------------------------------------------------
    // GET /api/showtimes/date/{date}
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should return showtimes by date")
    void shouldReturnShowTimesByDate() throws Exception {
        // TODO
    }


    // --------------------------------------------------------------------
    // POST /api/showtimes
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should create showtime when valid data")
    void shouldCreateShowTime() throws Exception {
        // TODO: build CreateShowTimeRequest, POST, assert created
    }

    @Test
    @DisplayName("should return 400 when request invalid")
    void shouldReturn400WhenInvalidCreateRequest() throws Exception {
        // TODO: invalid CreateShowTimeRequest (past date, negative price)
    }

    @Test
    @DisplayName("should return 400 when endTime before startTime (validation)")
    void shouldReturn400ForInvalidTimeRange() throws Exception {
        // TODO
    }

    @Test
    @DisplayName("should return 409 when showtime conflicts with existing")
    void shouldReturn409ForTimeConflict() throws Exception {
        // TODO: create second showtime overlapping with existing
    }


    // --------------------------------------------------------------------
    // PUT /api/showtimes/{id}
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should update showtime when valid data")
    void shouldUpdateShowTime() throws Exception {
        // TODO: build UpdateShowTimeRequest and call PUT
    }

    @Test
    @DisplayName("should return 404 when updating nonexistent showtime")
    void shouldReturn404WhenUpdatingNonexistent() throws Exception {
        // TODO
    }

    @Test
    @DisplayName("should return 400 when updating with invalid time range")
    void shouldReturn400WhenUpdatingInvalidTimeRange() throws Exception {
        // TODO
    }

    @Test
    @DisplayName("should return 409 when updating to conflicting time")
    void shouldReturn409WhenUpdatingConflict() throws Exception {
        // TODO
    }


    // --------------------------------------------------------------------
    // DELETE /api/showtimes/{id}
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should delete showtime when exists")
    void shouldDeleteShowTime() throws Exception {
        // TODO: DELETE saved id, assert status 204
    }

    @Test
    @DisplayName("should return 404 when deleting nonexistent showtime")
    void shouldReturn404WhenDeletingNonexistent() throws Exception {
        // TODO
    }

}