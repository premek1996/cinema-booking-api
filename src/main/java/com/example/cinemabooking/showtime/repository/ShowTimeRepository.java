package com.example.cinemabooking.showtime.repository;

import com.example.cinemabooking.showtime.entity.ShowTime;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"movie", "cinemaHall"})
    List<ShowTime> findAll();

    @NonNull
    @EntityGraph(attributePaths = {"movie", "cinemaHall"})
    Optional<ShowTime> findById(@NonNull Long id);

}
