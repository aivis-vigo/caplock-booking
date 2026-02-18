package com.caplock.booking.entity.dao;

import com.caplock.booking.entity.StatusEventEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EventDao {
    private long id;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private long duration; // in minutes
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long capacity;
    private long bookedSeats;
    private LocalDateTime bookingDeadline;
    private LocalDateTime bookingStartTime;
    private LocalDateTime createdAt;
    private long createdBy;
    private StatusEventEnum status;
    private long categoryId;

}
