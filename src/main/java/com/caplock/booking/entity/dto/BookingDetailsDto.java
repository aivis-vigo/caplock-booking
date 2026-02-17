package com.caplock.booking.entity.dto;

public class BookingDetailsDto {
    private BookingDto bookingDto;
    private EventDto eventDto;
    //PayInvDto


    public BookingDetailsDto(BookingDto bookingDto, EventDto eventDto) {
        this.bookingDto = bookingDto;
        this.eventDto = eventDto;
    }

    public BookingDto getBookingDto() {
        return bookingDto;
    }

    public void setBookingDto(BookingDto bookingDto) {
        this.bookingDto = bookingDto;
    }

    public EventDto getEventDto() {
        return eventDto;
    }

    public void setEventDto(EventDto eventDto) {
        this.eventDto = eventDto;
    }
}
