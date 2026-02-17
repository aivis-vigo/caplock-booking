package com.caplock.booking.service;

import com.caplock.booking.entity.dto.BookingDto;

import java.util.Collection;

public interface IBookingService {
    BookingDto getBookingById(long id);

    Collection<BookingDto> getAllUserBookings(long userId);

    boolean setNewBooking(BookingDto booking);

    boolean checkBookingExists(BookingDto booking);

    boolean cancelBooking(long bookingId);
}
