package com.caplock.booking.entity.object;

import com.caplock.booking.entity.StatusWaitListEnum;

import java.time.LocalDateTime;

public class WaitListEntry {
    private StatusWaitListEnum status;
    private String positionInQ;
    private LocalDateTime timestamp;
    private long eventId;
    private long userId;

    public WaitListEntry(StatusWaitListEnum status, String positionInQ, LocalDateTime timestamp, long eventId, long userId) {
        this.status = status;
        this.positionInQ = positionInQ;
        this.timestamp = timestamp;
        this.eventId = eventId;
        this.userId = userId;
    }

    public StatusWaitListEnum getStatus() {
        return status;
    }

    public void setStatus(StatusWaitListEnum status) {
        this.status = status;
    }

    public String getPositionInQ() {
        return positionInQ;
    }

    public void setPositionInQ(String positionInQ) {
        this.positionInQ = positionInQ;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
