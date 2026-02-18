package com.caplock.booking.entity.object;

import com.caplock.booking.entity.StatusEventEnum;
import jdk.jfr.Timespan;

import java.time.LocalDateTime;

public class Event {
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
    private String category;
   // private Category category;


    public Event(long id, String title, String description, LocalDateTime eventDate, String location, long duration, LocalDateTime startTime, LocalDateTime endTime, long capacity, long bookedSeats, LocalDateTime bookingDeadline, LocalDateTime bookingStartTime, LocalDateTime createdAt, long createdBy, StatusEventEnum status, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.bookedSeats = bookedSeats;
        this.bookingDeadline = bookingDeadline;
        this.bookingStartTime = bookingStartTime;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public long getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(long bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public LocalDateTime getBookingDeadline() {
        return bookingDeadline;
    }

    public void setBookingDeadline(LocalDateTime bookingDeadline) {
        this.bookingDeadline = bookingDeadline;
    }

    public LocalDateTime getBookingStartTime() {
        return bookingStartTime;
    }

    public void setBookingStartTime(LocalDateTime bookingStartTime) {
        this.bookingStartTime = bookingStartTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
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
