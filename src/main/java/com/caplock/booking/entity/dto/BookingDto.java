package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.StatusBookingEnum;

public class BookingDto {
    private String id;
    private StatusBookingEnum status;
    private int qty;
    private long eventId;
    private String eventTitle;
    private String eventDescription;
    private String eventLocation;
    private long userId;

    public BookingDto(String id, StatusBookingEnum status, int qty, long eventId, String eventTitle, String eventDescription, String eventLocation, long userId) {
        this.id = id;
        this.status = status;
        this.qty = qty;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StatusBookingEnum getStatus() {
        return status;
    }

    public void setStatus(StatusBookingEnum status) {
        this.status = status;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
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

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
