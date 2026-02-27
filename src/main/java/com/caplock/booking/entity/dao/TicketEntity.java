package com.caplock.booking.entity.dao;

import com.caplock.booking.entity.StatusTicketEnum;
import com.caplock.booking.entity.TicketType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @Column(unique = true)
    private String ticketNumber = UUID.randomUUID().toString();

    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "booking_item_id")
    private Long bookingItemId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "ticket_code")
    private String ticketCode;

    @Column(name = "seat")
    private String seat;

    @Column(name = "holder_name")
    private String holderName;

    @Column(name = "holder_email")
    private String holderEmail;

    @Column(name = "discount_code")
    private String discountCode;

    @Enumerated(EnumType.STRING)
    private StatusTicketEnum status = StatusTicketEnum.Issued;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt = LocalDateTime.now();

    @Column(name = "scanned_at")
    private LocalDateTime scannedAt;

    @Column(name = "qr_code_path")
    private String qrCodePath;
}
