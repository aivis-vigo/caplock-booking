package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.TicketType;
import com.caplock.booking.entity.dto.EventDto;
import com.caplock.booking.entity.dto.EventTicketConfigDto;
import lombok.*;
import org.javatuples.Pair;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailsDto {
    private EventDto event;
    private List<EventTicketConfigDto> ticketConfig;
    private List<Pair<String, TicketType>> freeSeats;
}