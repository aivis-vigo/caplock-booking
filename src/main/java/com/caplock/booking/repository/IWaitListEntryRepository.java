package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.*;
import com.caplock.booking.entity.dto.WaitListEntryDto;

import java.util.Collection;

public interface IWaitListEntryRepository {
    Collection<WaitListEntryDao> getAllWaitList();

    Collection<WaitListEntryDao> getAllWaitListByUser(long userId);

    boolean moveToBooking(WaitListEntryDao waitListEntryDao, BookingDao bookingDao);

    boolean setWaitListToUser(long userId, WaitListEntryDao waitListEntryDao);
}
