package com.caplock.booking.entity.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingEventRowDto {
    BookingDto bookingDto;
    EventDto eventDto;
    String userEmail;
}
