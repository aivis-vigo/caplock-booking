package com.caplock.booking.entity.dao;

import com.caplock.booking.entity.TicketType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_items")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookingItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "event_ticket_config_id")
    private Long eventTicketConfigId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type")
    private TicketType ticketType;

    private Integer quantity;

    @Column(name = "price_per_seat")
    private BigDecimal pricePerSeat;

    private BigDecimal subtotal;
}
