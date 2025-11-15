package com.example.cinemabooking.showtime.entity;

import com.example.cinemabooking.hall.entity.CinemaHall;
import com.example.cinemabooking.movie.entity.Movie;
import com.example.cinemabooking.reservation.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "show_times")
@Getter
@Setter
public class ShowTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private CinemaHall cinemaHall;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "showTime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    public boolean overlapsWith(LocalDateTime startTime, LocalDateTime endTime) {
        return this.startTime.isBefore(endTime) && this.endTime.isAfter(startTime);
    }

}
