package com.example.cinemabooking.showtime.web;

import com.example.cinemabooking.TestFixtures;
import com.example.cinemabooking.hall.entity.CinemaHall;
import com.example.cinemabooking.hall.repository.CinemaHallRepository;
import com.example.cinemabooking.movie.entity.Movie;
import com.example.cinemabooking.movie.repository.MovieRepository;
import com.example.cinemabooking.showtime.dto.CreateShowTimeRequest;
import com.example.cinemabooking.showtime.entity.ShowTime;
import com.example.cinemabooking.showtime.repository.ShowTimeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private CinemaHall cinemaHall;
    private ShowTime showTime;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        movie = TestFixtures.movie();
        cinemaHall = TestFixtures.cinemaHall();
        showTime = TestFixtures.showTimeWithoutId(now, movie, cinemaHall);
    }


    // --------------------------------------------------------------------
    // GET /api/showtimes
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should return empty list when no showtimes exist")
    void shouldReturnEmptyListWhenNoShowTimesExist() throws Exception {
        //given - empty db
        //when
        mockMvc.perform(get("/api/showtimes"))
                //then
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("should return list of showtimes when they exist")
    void shouldReturnListOfShowTimes() throws Exception {
        //given
        movieRepository.save(movie);
        cinemaHallRepository.save(cinemaHall);
        showTimeRepository.save(showTime);
        //when
        mockMvc.perform(get("/api/showtimes"))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieTitle").value("Inception"))
                .andExpect(jsonPath("$[0].cinemaHallName").value("Hall A"))
                .andExpect(jsonPath("$[0].price").value(10));
    }


    // --------------------------------------------------------------------
    // GET /api/showtimes/{id}
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should return showtime when found by id")
    void shouldReturnShowTimeById() throws Exception {
        //given
        movieRepository.save(movie);
        cinemaHallRepository.save(cinemaHall);
        long showTimeId = showTimeRepository.save(showTime).getId();
        //when
        mockMvc.perform(get("/api/showtimes/" + showTimeId))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieTitle").value("Inception"))
                .andExpect(jsonPath("$.cinemaHallName").value("Hall A"))
                .andExpect(jsonPath("$.price").value(10));
    }

    @Test
    @DisplayName("should return 404 when showtime not found by id")
    void shouldReturn404WhenShowTimeNotFound() throws Exception {
        //given - empty db
        //when
        mockMvc.perform(get("/api/showtimes/999"))
                //then
                .andExpect(status().isNotFound());
    }


    // --------------------------------------------------------------------
    // GET /api/showtimes/movie/{movieId}
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should return showtimes by movie id")
    void shouldReturnShowTimesByMovie() throws Exception {
        //given
        long movieId = movieRepository.save(movie).getId();
        cinemaHallRepository.save(cinemaHall);
        showTimeRepository.save(showTime);
        //when
        mockMvc.perform(get("/api/showtimes/movie/" + movieId))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieTitle").value("Inception"))
                .andExpect(jsonPath("$[0].cinemaHallName").value("Hall A"))
                .andExpect(jsonPath("$[0].price").value(10));
    }


    // --------------------------------------------------------------------
    // GET /api/showtimes/hall/{hallId}
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should return showtimes by hall id")
    void shouldReturnShowTimesByHall() throws Exception {
        //given
        movieRepository.save(movie);
        long cinemaHallId = cinemaHallRepository.save(cinemaHall).getId();
        showTimeRepository.save(showTime);
        //when
        mockMvc.perform(get("/api/showtimes/hall/" + cinemaHallId))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieTitle").value("Inception"))
                .andExpect(jsonPath("$[0].cinemaHallName").value("Hall A"))
                .andExpect(jsonPath("$[0].price").value(10));
    }


    // --------------------------------------------------------------------
    // GET /api/showtimes/date/{date}
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should return showtimes by date")
    void shouldReturnShowTimesByDate() throws Exception {
        //given
        movieRepository.save(movie);
        cinemaHallRepository.save(cinemaHall);
        showTimeRepository.save(showTime);
        //when
        mockMvc.perform(get("/api/showtimes/date/" + now.toLocalDate().format(DateTimeFormatter.ISO_DATE)))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieTitle").value("Inception"))
                .andExpect(jsonPath("$[0].cinemaHallName").value("Hall A"))
                .andExpect(jsonPath("$[0].price").value(10));
    }


    // --------------------------------------------------------------------
    // POST /api/showtimes
    // --------------------------------------------------------------------

    @Test
    @DisplayName("should create showtime when valid data")
    void shouldCreateShowTime() throws Exception {
        //given
        long movieId = movieRepository.save(movie).getId();
        long cinemaHallId = cinemaHallRepository.save(cinemaHall).getId();
        CreateShowTimeRequest createShowTimeRequest = CreateShowTimeRequest.builder()
                .movieId(movieId)
                .cinemaHallId(cinemaHallId)
                .startTime(now.plusHours(1))
                .endTime(now.plusHours(3))
                .price(BigDecimal.TEN)
                .build();
        String json = objectMapper.writeValueAsString(createShowTimeRequest);
        //when
        mockMvc.perform(post("/api/showtimes").content(json).contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movieTitle").value("Inception"))
                .andExpect(jsonPath("$.cinemaHallName").value("Hall A"))
                .andExpect(jsonPath("$.price").value(10));
    }

    @Test
    @DisplayName("should return 400 when request invalid")
    void shouldReturn400WhenInvalidCreateRequest() throws Exception {
        //given
        long movieId = movieRepository.save(movie).getId();
        long cinemaHallId = cinemaHallRepository.save(cinemaHall).getId();
        CreateShowTimeRequest createShowTimeRequest = CreateShowTimeRequest.builder()
                .movieId(movieId)
                .cinemaHallId(cinemaHallId)
                .startTime(now.minusHours(1))
                .endTime(now.plusHours(3))
                .price(BigDecimal.valueOf(-5))
                .build();
        String json = objectMapper.writeValueAsString(createShowTimeRequest);
        //when
        mockMvc.perform(post("/api/showtimes").content(json).contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when endTime before startTime (validation)")
    void shouldReturn400ForInvalidTimeRange() throws Exception {
        //given
        long movieId = movieRepository.save(movie).getId();
        long cinemaHallId = cinemaHallRepository.save(cinemaHall).getId();
        CreateShowTimeRequest createShowTimeRequest = CreateShowTimeRequest.builder()
                .movieId(movieId)
                .cinemaHallId(cinemaHallId)
                .startTime(now.plusHours(3))
                .endTime(now.plusHours(1))
                .price(BigDecimal.TEN)
                .build();
        String json = objectMapper.writeValueAsString(createShowTimeRequest);
        //when
        mockMvc.perform(post("/api/showtimes").content(json).contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 409 when showtime conflicts with existing")
    void shouldReturn409ForTimeConflict() throws Exception {
        // given
        movie = movieRepository.save(movie);
        cinemaHall = cinemaHallRepository.save(cinemaHall);

        showTime.setCinemaHall(cinemaHall);
        showTime.setMovie(movie);
        cinemaHall.addShowTime(showTime);

        showTimeRepository.save(showTime);

        CreateShowTimeRequest createShowTimeRequest = CreateShowTimeRequest.builder()
                .movieId(movie.getId())
                .cinemaHallId(cinemaHall.getId())
                .startTime(now.plusHours(2))
                .endTime(now.plusHours(3))
                .price(BigDecimal.TEN)
                .build();
        String json = objectMapper.writeValueAsString(createShowTimeRequest);
        //when
        mockMvc.perform(post("/api/showtimes").content(json).contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isConflict());
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