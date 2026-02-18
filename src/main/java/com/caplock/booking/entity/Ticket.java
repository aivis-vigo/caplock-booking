package com.caplock.booking.entity;

import com.caplock.booking.TicketType;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticketNumber;

    private String qrCode;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    // TODO: make it work
//    @ManyToOne
//    @JoinColumn(name = "id")
    private String event;

    private String section;
    @Column(name = "seat_row")
    private String row;
    private String seatNumber;

    private String holderName;
    private String holderEmail;

    // TODO: make it work - Booking
//    @ManyToOne
//    @JoinColumn(name = "id")
    @Column(name = "ticket_order")
    private String order;

    private BigDecimal price;
    private String currency;
    private String discountCode;
    private LocalDateTime purchasedAt;

    // TODO: make it work - Payment or PaymentMethod
//    @Enumerated(EnumType.STRING)
    private String paymentMethod;

    // TODO: make it work - TicketStatus
//    @Enumerated(EnumType.STRING)
    private String status;

    private LocalDateTime eventTakesPlaceOn;
    private LocalDateTime entryAt;
    private LocalDateTime checkedInAt;

}
