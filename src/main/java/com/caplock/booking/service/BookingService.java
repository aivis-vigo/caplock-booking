package com.caplock.booking.service;

import com.caplock.booking.entity.dto.BookingDto;

import java.util.Collection;
import java.util.List;

public class BookingService implements IBookingService{
    @Override
    public BookingDto getBookingById(long id) {
        return null;
    }

    @Override
    public Collection<BookingDto> getAllUserBookings(long userId) {
        return List.of();
    }

    @Override
    public boolean setNewBooking(BookingDto booking) {
        return false;
    }

    @Override
    public boolean checkBookingExists(BookingDto booking) {
        return false;
    }

    @Override
    public boolean cancelBooking(long bookingId) {
        return false;
    }
}
