package com.example.cinemabooking.reservation;

import com.example.cinemabooking.hall.entity.Seat;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reserved_seats", uniqueConstraints = @UniqueConstraint(columnNames = {"reservation_id", "seat_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"reservation", "seat"})
public class ReservedSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

}
