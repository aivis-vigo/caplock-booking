package com.caplock.booking.entity.dto;

import java.time.LocalDateTime;

public class EventDetailsDto {
    private EventDto eventDto;
    private String description;
    private long capacity;
    private long bookedSeats;
    private LocalDateTime bookingDeadline;
    private LocalDateTime bookingStartTime;

    public EventDetailsDto(EventDto eventDto, String description, long capacity, long bookedSeats, LocalDateTime bookingDeadline, LocalDateTime bookingStartTime) {
        this.eventDto = eventDto;
        this.description = description;
        this.capacity = capacity;
        this.bookedSeats = bookedSeats;
        this.bookingDeadline = bookingDeadline;
        this.bookingStartTime = bookingStartTime;
    }

    public EventDto getEventDto() {
        return eventDto;
    }

    public void setEventDto(EventDto eventDto) {
        this.eventDto = eventDto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
