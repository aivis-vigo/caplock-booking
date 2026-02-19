package com.caplock.booking.entity.dto;

import com.caplock.booking.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TicketDTO {
    private LocalDateTime date;
    private String eventName;
    private String section;
    private String row;
    private String seatNumber;
    private String entryAt;
    private TicketType ticketType;
    private String holderName;
    private String qrCode;
}
