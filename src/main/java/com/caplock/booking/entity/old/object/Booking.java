package com.caplock.booking.entity.old.object;

import com.caplock.booking.entity.StatusBookingEnum;

import java.time.LocalDateTime;

public class Booking {
    private String id;
    private StatusBookingEnum status;
    private int qty;
    private LocalDateTime createdAt;
    private LocalDateTime canceledAt;
    private long eventId;
    private long userId;

    public Booking(String id, StatusBookingEnum status, int qty, LocalDateTime createdAt, LocalDateTime canceledAt, long eventId, long userId) {
        this.id = id;
        this.status = status;
        this.qty = qty;
        this.createdAt = createdAt;
        this.canceledAt = canceledAt;
        this.eventId = eventId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
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
