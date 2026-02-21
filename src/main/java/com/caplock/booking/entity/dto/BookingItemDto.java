package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookingItemDto {
    private Long id;
    private Long bookingId;
    private Long eventTicketConfigId;
    private TicketType ticketType;
    private Integer quantity;
    private BigDecimal pricePerSeat;
    private BigDecimal subtotal;
}
