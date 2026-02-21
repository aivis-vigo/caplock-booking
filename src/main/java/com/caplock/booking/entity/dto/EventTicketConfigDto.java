package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EventTicketConfigDto {
    private Long id;
    private Long eventId;
    private TicketType ticketType;
    private BigDecimal price;
    private Long totalSeats;
    private Long availableSeats;
    private LocalDateTime saleStart;
    private LocalDateTime saleEnd;
    private Long version;
}
