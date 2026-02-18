package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.StatusBookingEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookingDto {
    private String id;
    private StatusBookingEnum status;
    private int qty;
    private long eventId;
    private String eventTitle;
    private String eventDescription;
    private String eventLocation;
    private long userId;
}
