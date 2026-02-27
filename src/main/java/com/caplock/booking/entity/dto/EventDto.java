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
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime bookingOpenAt;
    private LocalDateTime bookingDeadline;
    private LocalDateTime createdAt;
    private Long createdBy;
    private StatusEventEnum status;
    private String category;
}
