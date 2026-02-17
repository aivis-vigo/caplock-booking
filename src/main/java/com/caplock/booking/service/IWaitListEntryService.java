package com.caplock.booking.service;

import com.caplock.booking.entity.dao.BookingDao;
import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.WaitListEntryDto;

import java.util.Collection;

public interface IWaitListEntryService {
    Collection<WaitListEntryDto> getAllWaitList();

    Collection<WaitListEntryDto> getAllWaitListByUser(long userId);

    WaitListEntryDto getAllWaitListById(long id);

    boolean moveToBooking(WaitListEntryDto waitListEntryDto, BookingDto bookingDto);

    boolean setWaitListToUser(long userId, WaitListEntryDto waitListEntryDto);
}
