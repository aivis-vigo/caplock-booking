package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.StatusTicketEnum;
import com.caplock.booking.entity.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TicketDto {
    private Long id;
    private Long bookingId;
    private Long bookingItemId;
    private Long eventId;
    private TicketType ticketType;
    private String ticketCode;
    private String seat;
    private String holderName;
    private String holderEmail;
    private String discountCode;
    private StatusTicketEnum status;
    private LocalDateTime issuedAt;
    private LocalDateTime scannedAt;
    private String qrCodePath;
}
