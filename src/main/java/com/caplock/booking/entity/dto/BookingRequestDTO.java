package com.caplock.booking.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequestDTO {
    private Long eventId;
    private List<TicketSelectionDTO> tickets;

    @Data
    public static class TicketSelectionDTO {
        private Long ticketConfigId;
        private String seat;
    }
}
