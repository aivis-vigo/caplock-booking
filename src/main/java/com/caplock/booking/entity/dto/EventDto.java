package com.caplock.booking.entity.dto;

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
public class EventDto {
    private long id;
    private String title;
    private LocalDateTime eventDate;
    private String location;
    private long duration; // in minutes
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private StatusEventEnum status;
    private String category;
}
