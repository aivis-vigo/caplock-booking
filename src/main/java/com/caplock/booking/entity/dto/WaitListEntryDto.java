package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.StatusWaitListEnum;

import java.time.LocalDateTime;

public class WaitListEntryDto {
    private StatusWaitListEnum status;
    private String positionInQ;
    private long eventId;
    private String eventTitle;
    private LocalDateTime eventDate;
    private String userName;
    private long userId;

    public WaitListEntryDto(StatusWaitListEnum status, String positionInQ, long eventId, String eventTitle, LocalDateTime eventDate, String userName, long userId) {
        this.status = status;
        this.positionInQ = positionInQ;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.userName = userName;
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

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
