package com.caplock.booking.service;

import com.caplock.booking.entity.dao.BookingDao;
import com.caplock.booking.entity.dao.WaitListEntryDao;
import com.caplock.booking.entity.dto.WaitListEntryDto;

import java.util.Collection;
import java.util.List;

public class WaitListEntryService implements IWaitListEntryService
{
    @Override
    public Collection<WaitListEntryDto> getAllWaitList() {
        return List.of();
    }

    @Override
    public Collection<WaitListEntryDto> getAllWaitListByUser(long userId) {
        return List.of();
    }

    @Override
    public boolean moveToBooking(WaitListEntryDto waitListEntryDto, BookingDao bookingDao) {
        return false;
    }

    @Override
    public boolean setWaitListToUser(long userId, WaitListEntryDto waitListEntryDto) {
        return false;
    }
}
