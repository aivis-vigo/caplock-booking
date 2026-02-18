package com.caplock.booking.service;


import com.caplock.booking.entity.dao.*;
import com.caplock.booking.entity.dto.*;
import com.caplock.booking.entity.object.*;
import com.caplock.booking.repository.IWaitListEntryRepository;
import com.caplock.booking.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class WaitListEntryService implements IWaitListEntryService {

    @Autowired
    private IWaitListEntryRepository waitRepo;
//    @Autowired
//    private IUserService userService;

    @Autowired
    private IEventService eventService;

    @Override
    public Collection<WaitListEntryDto> getAllWaitList() {

        return  waitRepo.getAllWaitList().stream()
                .map(dao -> Mapper.combine(
                        WaitListEntryDto.class,
                        dao,
                        eventService.getEventById(dao.getEventId()) ,
                        new User(15, "Name")))  //userService.getUserById(dao.getUserId)))
                 .toList();


    }

    @Override
    public Collection<WaitListEntryDto> getAllWaitListByUser(long userId) {
        return waitRepo.getAllWaitListByUser(userId).stream()
                .map(dao -> Mapper.combine(
                        WaitListEntryDto.class,
                        dao,
                        eventService.getEventById(dao.getEventId()) ,
                        new User(15, "Name")))
                .toList();
    }

    @Override
    public WaitListEntryDto getAllWaitListById(long id) {
        return null;
    }

    @Override
    public boolean setWaitListToUser(long userId, WaitListEntryDto waitListEntryDto) {
        WaitListEntryDao dao = Mapper.splitOne(waitListEntryDto, WaitListEntryDao.class);
        return waitRepo.setWaitListToUser(userId, dao);
    }

    @Override
    public boolean moveToBooking(WaitListEntryDto waitDto, BookingDto BookingDto) {
        WaitListEntryDao waitListEntryDao = Mapper.splitOne(waitDto, WaitListEntryDao.class);
        BookingDao bookingDao =Mapper.splitOne(BookingDto, BookingDao.class);
        return waitRepo.moveToBooking(waitListEntryDao, bookingDao);
    }
}
