package com.caplock.booking.repository;

import com.caplock.booking.entity.StatusBookingEnum;
import com.caplock.booking.entity.StatusWaitListEnum;
import com.caplock.booking.entity.dao.BookingDao;
import com.caplock.booking.entity.dao.WaitListEntryDao;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Repository
public class WaitRepo implements IWaitListEntryRepository {
    private static long idCounter = 1;
    private static final List<WaitListEntryDao> mockWaitList = new ArrayList<>(List.of(
            new WaitListEntryDao(4, StatusWaitListEnum.Pending, "1", LocalDateTime.now(), 2L, 100L)
    ));

    @Override
    public Collection<WaitListEntryDao> getAllWaitList() {
        return new ArrayList<>(mockWaitList);
    }

    @Override
    public Collection<WaitListEntryDao> getAllWaitListByUser(long userId) {
        return mockWaitList.stream()
                .filter(entry -> entry.getUserId() == userId)
                .toList();
    }

    @Override
    public boolean setWaitListToUser(long userId, WaitListEntryDao waitListEntryDao) {
        waitListEntryDao.setUserId(userId);
        waitListEntryDao.setId(idCounter++);
        waitListEntryDao.setTimestamp(LocalDateTime.now());
        long currentQueueSize = mockWaitList.stream()
                .filter(e -> e.getEventId() == waitListEntryDao.getEventId())
                .count();
        waitListEntryDao.setPositionInQ("POS-" + (currentQueueSize + 1));

        return mockWaitList.add(waitListEntryDao);
    }

    @Override
    public boolean moveToBooking(WaitListEntryDao waitListEntryDao, BookingDao bookingDao) {
        return mockWaitList.removeIf(entry -> entry.getId() == waitListEntryDao.getId());
    }
}
