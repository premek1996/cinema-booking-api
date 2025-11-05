package com.example.cinemabooking.showtime.repository;

import com.example.cinemabooking.showtime.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
}
