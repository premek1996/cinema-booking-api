package com.example.cinemabooking.movie.web;

import com.example.cinemabooking.movie.dto.CreateMovieRequest;
import com.example.cinemabooking.movie.entity.AgeRating;
import com.example.cinemabooking.movie.entity.Movie;
import com.example.cinemabooking.movie.repository.MovieRepository;
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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    private Movie sampleMovie;

    @BeforeEach
    void setUp() {
        sampleMovie = Movie.builder()
                .title("Inception")
                .description("Dream within a dream")
                .genre("Sci-Fi")
                .durationMinutes(148)
                .releaseDate(LocalDate.of(2010, 7, 16))
                .ageRating(AgeRating.AGE_12)
                .build();
    }

    // ----------------------------------------
    // GET /api/movies
    // ----------------------------------------

    @Test
    @DisplayName("should return empty list when no movies exist")
    void shouldReturnEmptyListWhenNoMoviesExist() throws Exception {
        // given – no movies in DB
        // when
        mockMvc.perform(get("/api/movies"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("should return list of movies when movies exist")
    void shouldReturnListOfMovies() throws Exception {
        // given
        movieRepository.save(sampleMovie);
        // when
        mockMvc.perform(get("/api/movies"))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"))
                .andExpect(jsonPath("$[0].genre").value("Sci-Fi"));
    }

    // ----------------------------------------
    // GET /api/movies/{id}
    // ----------------------------------------

    @Test
    @DisplayName("should return movie when found by id")
    void shouldReturnMovieById() throws Exception {
        // given
        Movie saved = movieRepository.save(sampleMovie);
        //when
        mockMvc.perform(get("/api/movies/" + saved.getId()))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.genre").value("Sci-Fi"));
    }

    @Test
    @DisplayName("should return 404 when movie not found by id")
    void shouldReturn404WhenMovieNotFound() throws Exception {
        // given – no movies in DB
        //when
        mockMvc.perform(get("/api/movies/99"))
                //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages[0]").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // ----------------------------------------
    // POST /api/movies
    // ----------------------------------------

    @Test
    @DisplayName("should create movie when valid data")
    void shouldCreateMovieWhenValidData() throws Exception {
        //given
        CreateMovieRequest createMovieRequest = CreateMovieRequest.builder()
                .title("Tenet")
                .description("Time inversion")
                .genre("Sci-Fi")
                .durationMinutes(150)
                .releaseDate(LocalDate.of(2020, 8, 26))
                .ageRating(AgeRating.AGE_12)
                .build();
        String json = objectMapper.writeValueAsString(createMovieRequest);
        //when
        mockMvc.perform(post("/api/movies")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Tenet"))
                .andExpect(jsonPath("$.genre").value("Sci-Fi"));
        assertThat(movieRepository.findByTitle("Tenet")).isPresent();
    }

    @Test
    @DisplayName("should return 400 when request invalid")
    void shouldReturn400WhenRequestInvalid() throws Exception {
        //given
        CreateMovieRequest createMovieRequest = CreateMovieRequest.builder()
                .title("")
                .description("")
                .genre("Sci-Fi")
                .durationMinutes(-90)
                .releaseDate(LocalDate.of(2020, 8, 26))
                .ageRating(AgeRating.AGE_12)
                .build();
        String json = objectMapper.writeValueAsString(createMovieRequest);
        //when
        mockMvc.perform(post("/api/movies")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("should return 409 when movie with same title exists")
    void shouldReturn409WhenMovieAlreadyExists() throws Exception {
        //given
        movieRepository.save(sampleMovie);
        CreateMovieRequest createMovieRequest = CreateMovieRequest.builder()
                .title("Inception")
                .description("Duplicate")
                .genre("Sci-Fi")
                .durationMinutes(148)
                .releaseDate(LocalDate.of(2010, 7, 16))
                .ageRating(AgeRating.AGE_12)
                .build();
        String json = objectMapper.writeValueAsString(createMovieRequest);
        //when
        mockMvc.perform(post("/api/movies")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messages[0]").exists())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.timestamp").exists());
        assertThat(movieRepository.findAll()).hasSize(1);
    }

    // ----------------------------------------
    // DELETE /api/movies/{id}
    // ----------------------------------------

    @Test
    @DisplayName("should delete movie when exists")
    void shouldDeleteMovieWhenExists() throws Exception {
        //given
        Movie saved = movieRepository.save(sampleMovie);
        //when
        mockMvc.perform(delete("/api/movies/" + saved.getId()))
                //then
                .andExpect(status().isNoContent());
        assertThat(movieRepository.findByTitle(sampleMovie.getTitle())).isEmpty();
    }

    @Test
    @DisplayName("should return 404 when deleting nonexistent movie")
    void shouldReturn404WhenDeletingNonexistentMovie() throws Exception {
        // given – no movies in DB
        //when
        mockMvc.perform(delete("/api/movies/1"))
                //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages[0]").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").exists());
    }

}