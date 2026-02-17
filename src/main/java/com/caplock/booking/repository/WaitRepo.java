package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.BookingDao;
import com.caplock.booking.entity.dao.WaitListEntryDao;

import java.util.Collection;
import java.util.List;

public class WaitRepo implements IWaitListEntryRepository {
    @Override
    public Collection<WaitListEntryDao> getAllWaitList() {
        return List.of();
    }

    @Override
    public Collection<WaitListEntryDao> getAllWaitListByUser(long userId) {
        return List.of();
    }

    @Override
    public boolean moveToBooking(WaitListEntryDao waitListEntryDao, BookingDao bookingDao) {
        return false;
    }

    @Override
    public boolean setWaitListToUser(long userId, WaitListEntryDao waitListEntryDao) {
        return false;
    }
}
