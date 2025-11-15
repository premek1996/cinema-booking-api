package com.example.cinemabooking.hall.entity;

import com.example.cinemabooking.showtime.entity.ShowTime;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cinema_halls")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CinemaHall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int rows;

    @Column(nullable = false)
    private int seatsPerRow;

    @OneToMany(mappedBy = "cinemaHall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;

    @OneToMany(mappedBy = "cinemaHall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShowTime> showTimes;

    public List<Seat> getSeats() {
        return seats != null ? seats : new ArrayList<>();
    }

    public void addSeat(Seat seat) {
        if (seats == null) {
            seats = new ArrayList<>();
        }
        seats.add(seat);
        seat.setCinemaHall(this);
    }

    public void clearSeats() {
        if (seats != null) {
            seats.clear();
        }
    }

    public void addShowTime(ShowTime showTime) {
        if (showTimes == null) {
            showTimes = new ArrayList<>();
        }
        showTimes.add(showTime);
        showTime.setCinemaHall(this);
    }

    public boolean isOccupiedDuring(LocalDateTime startTime, LocalDateTime endTime, Long currentShowTimeId) {
        return showTimes != null && showTimes.stream()
                .anyMatch(showTime -> !showTime.getId().equals(currentShowTimeId) && showTime.overlapsWith(startTime, endTime));
    }

}
