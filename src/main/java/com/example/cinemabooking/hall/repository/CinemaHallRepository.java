package com.example.cinemabooking.hall.repository;

import com.example.cinemabooking.hall.entity.CinemaHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CinemaHallRepository extends JpaRepository<CinemaHall, Long> {

    @Query("SELECT h FROM CinemaHall h WHERE h.name = :name")
    Optional<CinemaHall> findByName(@Param("name") String name);

}
