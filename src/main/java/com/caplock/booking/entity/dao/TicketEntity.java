package com.caplock.booking.entity.dao;

import com.caplock.booking.entity.StatusTicketEnum;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "booking_item_id")
    private Long bookingItemId;

    @Column(name = "event_id")
    private Long eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type")
    private TicketType ticketType;

    @Column(name = "ticket_code")
    private String ticketCode;

    private String section;
    private String row;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "holder_name")
    private String holderName;

    @Column(name = "holder_email")
    private String holderEmail;

    @Column(name = "discount_code")
    private String discountCode;

    @Enumerated(EnumType.STRING)
    private StatusTicketEnum status;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "scanned_at")
    private LocalDateTime scannedAt;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;
}
