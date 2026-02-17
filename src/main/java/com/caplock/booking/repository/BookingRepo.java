package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.BookingDao;

import java.util.Collection;
import java.util.List;

public class BookingRepo implements IBookingRepository{
    @Override
    public BookingDao getBookingById(long id) {
        return null;
    }

    @Override
    public Collection<BookingDao> getAllUserBookings(long userId) {
        return List.of();
    }

    @Override
    public boolean setNewBooking(BookingDao booking) {
        return false;
    }

    @Override
    public boolean checkBookingExists(BookingDao booking) {
        return false;
    }

    @Override
    public boolean cancelBooking(long bookingId) {
        return false;
    }
}
