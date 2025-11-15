package com.example.cinemabooking.hall.repository;

import com.example.cinemabooking.TestFixtures;
import com.example.cinemabooking.hall.entity.CinemaHall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CinemaHallRepositoryTest {

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    private CinemaHall cinemaHall;

    @BeforeEach
    void setUp() {
        cinemaHall = TestFixtures.cinemaHall();
    }

    @Test
    @DisplayName("should find cinema hall by name")
    void shouldFindCinemaHallByName() {
        // given
        cinemaHallRepository.save(cinemaHall);
        // when
        Optional<CinemaHall> result = cinemaHallRepository.findByName(cinemaHall.getName());
        // then
        assertThat(result).isPresent();
        assertThat(result.get().getRows()).isEqualTo(5);
        assertThat(result.get().getSeatsPerRow()).isEqualTo(10);
    }

    @Test
    @DisplayName("should return empty when hall not found by name")
    void shouldReturnEmptyWhenNotFound() {
        // given
        cinemaHallRepository.save(cinemaHall);
        // when
        Optional<CinemaHall> result = cinemaHallRepository.findByName("Hall B");
        // then
        assertThat(result).isEmpty();
    }

}