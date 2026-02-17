package com.caplock.booking.service;

import com.caplock.booking.entity.dao.BookingDao;
import com.caplock.booking.entity.dto.WaitListEntryDto;

import java.util.Collection;

public interface IWaitListEntryService {
    Collection<WaitListEntryDto> getAllWaitList();

    Collection<WaitListEntryDto> getAllWaitListByUser(long userId);

    boolean moveToBooking(WaitListEntryDto waitListEntryDto, BookingDao bookingDao);

    boolean setWaitListToUser(long userId, WaitListEntryDto waitListEntryDto);
}
