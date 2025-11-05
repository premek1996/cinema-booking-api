package com.example.cinemabooking.hall.web;

import com.example.cinemabooking.hall.dto.CreateCinemaHallRequest;
import com.example.cinemabooking.hall.dto.UpdateCinemaHallRequest;
import com.example.cinemabooking.hall.entity.CinemaHall;
import com.example.cinemabooking.hall.repository.CinemaHallRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        // given – no cinema halls in DB
        // when
        mockMvc.perform(get("/api/halls"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("should return list of cinema halls when halls exist")
    void shouldReturnListOfCinemaHallsWhenExists() throws Exception {
        // given
        cinemaHallRepository.save(sampleCinemaHall);
        // when
        mockMvc.perform(get("/api/halls"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hall A"))
                .andExpect(jsonPath("$[0].rows").value(5))
                .andExpect(jsonPath("$[0].seatsPerRow").value(10));
    }

    // --------------------------------------------------
    // GET /api/halls/{id}
    // --------------------------------------------------

    @Test
    @DisplayName("should return cinema hall when found by id")
    void shouldReturnCinemaHallById() throws Exception {
        // given
        CinemaHall saved = cinemaHallRepository.save(sampleCinemaHall);
        // when
        mockMvc.perform(get("/api/halls/" + saved.getId()))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hall A"))
                .andExpect(jsonPath("$.rows").value(5))
                .andExpect(jsonPath("$.seatsPerRow").value(10));
    }

    @Test
    @DisplayName("should return 404 when cinema hall not found by id")
    void shouldReturn404WhenCinemaHallNotFound() throws Exception {
        // given – no cinema halls in DB
        // when
        mockMvc.perform(get("/api/halls/99"))
                // then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages[0]").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // --------------------------------------------------
    // POST /api/halls
    // --------------------------------------------------

    @Test
    @DisplayName("should create cinema hall when valid data")
    void shouldCreateCinemaHallWhenValidData() throws Exception {
        // given
        CreateCinemaHallRequest request = CreateCinemaHallRequest.builder()
                .name("Hall B")
                .rows(8)
                .seatsPerRow(12)
                .build();
        String json = objectMapper.writeValueAsString(request);
        // when
        mockMvc.perform(post("/api/halls").contentType(MediaType.APPLICATION_JSON).content(json))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Hall B"))
                .andExpect(jsonPath("$.rows").value(8))
                .andExpect(jsonPath("$.seatsPerRow").value(12));
        assertThat(cinemaHallRepository.findByName("Hall B")).isPresent();
    }

    @Test
    @DisplayName("should return 400 when request invalid")
    void shouldReturn400WhenRequestInvalid() throws Exception {
        // given
        CreateCinemaHallRequest request = CreateCinemaHallRequest.builder()
                .name("")
                .rows(-8)
                .seatsPerRow(0)
                .build();
        String json = objectMapper.writeValueAsString(request);
        // when
        mockMvc.perform(post("/api/halls").contentType(MediaType.APPLICATION_JSON).content(json))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("should return 409 when cinema hall with same name exists")
    void shouldReturn409WhenCinemaHallWithSameNameExists() throws Exception {
        // given
        cinemaHallRepository.save(sampleCinemaHall);
        CreateCinemaHallRequest request = CreateCinemaHallRequest.builder()
                .name(sampleCinemaHall.getName())
                .rows(10)
                .seatsPerRow(5)
                .build();
        String json = objectMapper.writeValueAsString(request);
        // when
        mockMvc.perform(post("/api/halls").contentType(MediaType.APPLICATION_JSON).content(json))
                // then
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messages[0]").exists())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // --------------------------------------------------
    // PUT /api/halls/{id}
    // --------------------------------------------------

    @Test
    @DisplayName("should update cinema hall when exists and valid data")
    void shouldUpdateCinemaHallWhenExistsAndValidData() throws Exception {
        // given
        CinemaHall saved = cinemaHallRepository.save(sampleCinemaHall);
        UpdateCinemaHallRequest request = UpdateCinemaHallRequest.builder()
                .name("Updated Hall")
                .rows(10)
                .seatsPerRow(12)
                .build();
        String json = objectMapper.writeValueAsString(request);
        // when
        mockMvc.perform(put("/api/halls/" + saved.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Hall"))
                .andExpect(jsonPath("$.rows").value(10))
                .andExpect(jsonPath("$.seatsPerRow").value(12));
    }

    @Test
    @DisplayName("should return 404 when updating non-existent cinema hall")
    void shouldReturn404WhenUpdatingNonExistentCinemaHall() throws Exception {
        // given
        UpdateCinemaHallRequest request = UpdateCinemaHallRequest.builder()
                .name("Updated Hall")
                .rows(10)
                .seatsPerRow(12)
                .build();
        String json = objectMapper.writeValueAsString(request);
        // when
        mockMvc.perform(put("/api/halls/99").contentType(MediaType.APPLICATION_JSON).content(json))
                // then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages[0]").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("should return 409 when updating cinema hall to duplicate name")
    void shouldReturn409WhenUpdatingCinemaHallToDuplicateName() throws Exception {
        // given
        CinemaHall cinemaHallWitDuplicatedName = CinemaHall.builder()
                .name("Hall B")
                .rows(5)
                .seatsPerRow(10)
                .build();
        cinemaHallRepository.save(cinemaHallWitDuplicatedName);
        CinemaHall saved = cinemaHallRepository.save(sampleCinemaHall);
        UpdateCinemaHallRequest request = UpdateCinemaHallRequest.builder()
                .name(cinemaHallWitDuplicatedName.getName())
                .rows(10)
                .seatsPerRow(12)
                .build();
        String json = objectMapper.writeValueAsString(request);
        // when
        mockMvc.perform(put("/api/halls/" + saved.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                // then
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messages[0]").exists())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // --------------------------------------------------
    // DELETE /api/halls/{id}
    // --------------------------------------------------

    @Test
    @DisplayName("should delete cinema hall when exists")
    void shouldDeleteCinemaHallWhenExists() throws Exception {
        // given
        CinemaHall saved = cinemaHallRepository.save(sampleCinemaHall);
        // when
        mockMvc.perform(delete("/api/halls/" + saved.getId()))
                // then
                .andExpect(status().isNoContent());
        assertThat(cinemaHallRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("should return 404 when deleting non-existent cinema hall")
    void shouldReturn404WhenDeletingNonExistentCinemaHall() throws Exception {
        // given – no cinema halls in DB
        // when
        mockMvc.perform(delete("/api/halls/99"))
                // then
                .andExpect(status().isNotFound());
    }

}
