package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.*;

import java.util.Collection;

public interface IBookingRepository {
    BookingDao getBookingById(long id);

    Collection<BookingDao> getAllUserBookings(long userId);

    boolean setNewBooking(BookingDao booking);

    boolean checkBookingExists(BookingDao booking);

    boolean cancelBooking(String bookingId);
}
