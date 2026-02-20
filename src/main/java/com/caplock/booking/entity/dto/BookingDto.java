package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.StatusBookingEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookingDto {
    private String id;
    private StatusBookingEnum status;
    private long eventId;
    private String eventTitle;
    private String eventDescription;
    private String eventLocation;
    private long userId;
    private List<Seat> seats;
}
