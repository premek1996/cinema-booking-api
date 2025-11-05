package com.example.cinemabooking.hall.entity;

import com.example.cinemabooking.showtime.entity.ShowTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cinema_halls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"seats", "showTimes"})
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
    @Builder.Default
    private List<Seat> seats = new ArrayList<>();

    @OneToMany(mappedBy = "cinemaHall", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ShowTime> showTimes = new ArrayList<>();

}
