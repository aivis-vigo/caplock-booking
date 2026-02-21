package com.caplock.booking.repository.old.jpa;

import com.caplock.booking.entity.old.dao.BookingDao;
import com.caplock.booking.entity.old.dao.WaitListEntryDao;

import java.util.Collection;

public interface IWaitListEntryRepository {
    Collection<WaitListEntryDao> getAllWaitList();

    Collection<WaitListEntryDao> getAllWaitListByUser(long userId);

    boolean moveToBooking(WaitListEntryDao waitListEntryDao, BookingDao bookingDao);

    boolean setWaitListToUser(long userId, WaitListEntryDao waitListEntryDao);
}
