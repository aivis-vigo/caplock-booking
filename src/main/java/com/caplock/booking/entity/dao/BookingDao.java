package com.caplock.booking.entity.dao;

import com.caplock.booking.entity.StatusBookingEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookingDao {
    private String id;
    private StatusBookingEnum status;
    private int qty;
    private LocalDateTime createdAt;
    private LocalDateTime canceledAt;
    private long eventId;
    private long userId;

}
