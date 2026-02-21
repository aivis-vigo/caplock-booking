package com.caplock.booking.service.old;

import com.caplock.booking.entity.old.dto.BookingDetailsDto;
import com.caplock.booking.entity.old.dto.BookingDto;
import com.caplock.booking.entity.old.dto.BookingFormDto;
import org.javatuples.Pair;

import java.util.Collection;

public interface IBookingService {
    BookingFormDto getBookingFormById(String id, long userId);

    BookingDetailsDto getDetails(String id);

    BookingDto getBookingById(String id);

    Collection<BookingDto> getAllUserBookings(long userId);

    Pair<Boolean, String> setNewBooking(BookingFormDto booking);

    boolean cancelBooking(String bookingId);
}
