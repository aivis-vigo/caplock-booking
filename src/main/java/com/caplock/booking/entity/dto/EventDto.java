package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.StatusEventEnum;

import java.time.LocalDateTime;

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

    public EventDto(long id, String title, LocalDateTime eventDate, String location, long duration, LocalDateTime startTime, LocalDateTime endTime, StatusEventEnum status, String category) {
        this.id = id;
        this.title = title;
        this.eventDate = eventDate;
        this.location = location;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public StatusEventEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEventEnum status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
