package com.example.cinemabooking.hall.web;

import com.example.cinemabooking.hall.entity.CinemaHall;
import com.example.cinemabooking.hall.repository.CinemaHallRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CinemaHallControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    private CinemaHall sampleCinemaHall;

    @BeforeEach
    void setUp() {
        sampleCinemaHall = CinemaHall.builder()
                .name("Hall A")
                .rows(5)
                .seatsPerRow(10)
                .build();
    }

    // --------------------------------------------------
    // GET /api/halls
    // --------------------------------------------------

    @Test
    @DisplayName("should return empty list when no cinema halls exist")
    void shouldReturnEmptyListWhenNoCinemaHallsExist() throws Exception {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("should return list of cinema halls when halls exist")
    void shouldReturnListOfCinemaHallsWhenExists() throws Exception {
        // given

        // when

        // then
    }

    // --------------------------------------------------
    // GET /api/halls/{id}
    // --------------------------------------------------

    @Test
    @DisplayName("should return cinema hall when found by id")
    void shouldReturnCinemaHallById() throws Exception {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("should return 404 when cinema hall not found by id")
    void shouldReturn404WhenCinemaHallNotFound() throws Exception {
        // given

        // when

        // then
    }

    // --------------------------------------------------
    // POST /api/halls
    // --------------------------------------------------

    @Test
    @DisplayName("should create cinema hall when valid data")
    void shouldCreateCinemaHallWhenValidData() throws Exception {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("should return 400 when request invalid")
    void shouldReturn400WhenRequestInvalid() throws Exception {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("should return 409 when cinema hall with same name exists")
    void shouldReturn409WhenCinemaHallWithSameNameExists() throws Exception {
        // given

        // when

        // then
    }

    // --------------------------------------------------
    // PUT /api/halls/{id}
    // --------------------------------------------------

    @Test
    @DisplayName("should update cinema hall when exists and valid data")
    void shouldUpdateCinemaHallWhenExistsAndValidData() throws Exception {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("should return 404 when updating non-existent cinema hall")
    void shouldReturn404WhenUpdatingNonExistentCinemaHall() throws Exception {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("should return 409 when updating cinema hall to duplicate name")
    void shouldReturn409WhenUpdatingCinemaHallToDuplicateName() throws Exception {
        // given

        // when

        // then
    }

    // --------------------------------------------------
    // DELETE /api/halls/{id}
    // --------------------------------------------------

    @Test
    @DisplayName("should delete cinema hall when exists")
    void shouldDeleteCinemaHallWhenExists() throws Exception {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("should return 404 when deleting non-existent cinema hall")
    void shouldReturn404WhenDeletingNonExistentCinemaHall() throws Exception {
        // given

        // when

        // then
    }

}
