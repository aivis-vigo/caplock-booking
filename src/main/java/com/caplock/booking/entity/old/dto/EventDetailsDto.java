package com.caplock.booking.entity.old.dto;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EventDetailsDto {
    private EventDto eventDto;
    private String description;
    private long capacity;
    private long bookedSeats;
    private LocalDateTime bookingDeadline;
    private LocalDateTime bookingStartTime;

    }
