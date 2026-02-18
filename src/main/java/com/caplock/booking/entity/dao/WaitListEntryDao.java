package com.caplock.booking.entity.dao;

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
public class WaitListEntryDao {
    private long id;
    private StatusWaitListEnum status;
    private String positionInQ;
    private LocalDateTime timestamp;
    private long eventId;
    private long userId;


}
