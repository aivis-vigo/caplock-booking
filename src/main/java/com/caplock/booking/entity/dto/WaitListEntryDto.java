package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.StatusWaitListEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WaitListEntryDto {
    private StatusWaitListEnum status;
    private String positionInQ;
    private long eventId;
    private String eventTitle;
    private LocalDateTime eventDate;
    private String userName;
    private long userId;


}
