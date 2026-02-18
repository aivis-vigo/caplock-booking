package com.caplock.booking.service;

import com.caplock.booking.entity.dto.BookingDetailsDto;
import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.EventDetailsDto;
import org.javatuples.Pair;

import java.util.Collection;

public interface IBookingService {
    BookingDetailsDto getDetails(long id);

    BookingDto getBookingById(long id);

    Collection<BookingDto> getAllUserBookings(long userId);

    Pair<Boolean, String> setNewBooking(BookingDto booking);

    boolean cancelBooking(String bookingId);
}
