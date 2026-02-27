package com.caplock.booking.entity.dao;

import com.caplock.booking.entity.TicketType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(
        name = "event_seats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "seat_number"})
)
public class EventSeatsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private TicketType seatType;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "confirmed_at", nullable = false)
    private LocalDateTime confirmedAt;
}
