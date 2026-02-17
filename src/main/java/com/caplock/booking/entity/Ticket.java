package com.caplock.booking.entity;

import com.caplock.booking.TicketType;
import jakarta.persistence.*;
import jdk.jfr.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tickets")
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    @Column(unique = true)
    private String ticketNumber;

    private String qrCode;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    // TODO: make it work
    @ManyToOne
    @JoinColumn(name = "id")
    private Event event;

    private String section;
    private String row;
    private String seatNumber;

    private String holderName;
    private String holderEmail;

    // TODO: make it work
    @ManyToOne
    @JoinColumn(name = "id")
    private Booking order;

    private BigDecimal price;
    private String currency;
    private String discountCode;
    private LocalDateTime purchasedAt;

    // TODO: make it work
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    // TODO: make it work
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime eventTakesPlaceOn;
    private LocalDateTime entryAt;
    private LocalDateTime checkedInAt;

}
