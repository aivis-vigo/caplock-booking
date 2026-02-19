package com.caplock.booking.service;


import com.caplock.booking.entity.dao.*;
import com.caplock.booking.entity.dto.*;
import com.caplock.booking.entity.object.*;
import com.caplock.booking.repository.IWaitListEntryRepository;
import com.caplock.booking.util.Mapper;
import com.caplock.booking.util.UserDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class WaitListEntryService implements IWaitListEntryService {

    @Autowired
    private IWaitListEntryRepository waitRepo;
    private final UserService userService = new UserService(new UserDTOMapper());



    @Autowired
    private IEventService eventService;

    @Override
    public Collection<WaitListEntryDto> getAllWaitList() {

        return  waitRepo.getAllWaitList().stream()
                .map(dao -> Mapper.combine(
                        WaitListEntryDto.class,
                        dao,
                        eventService.getEventById(dao.getEventId()) ,
                        userService.getUserById((int) dao.getUserId())))
                 .toList();


    }

    @Override
    public Collection<WaitListEntryDto> getAllWaitListByUser(long userId) {
        return waitRepo.getAllWaitListByUser(userId).stream()
                .map(dao -> Mapper.combine(
                        WaitListEntryDto.class,
                        dao,
                        eventService.getEventById(dao.getEventId()) ,
                        userService.getUserById((int) dao.getUserId())))
                .toList();
    }

    @Override
    public WaitListEntryDto getAllWaitListById(long id) {
        return null;
    }

    @Override
    public boolean setWaitListToUser(long userId, WaitListEntryDto waitListEntryDto) {
        WaitListEntryDao dao = Mapper.splitOne(waitListEntryDto, WaitListEntryDao.class, Map.of());
        return waitRepo.setWaitListToUser(userId, dao);
    }

    @Override
    public boolean moveToBooking(WaitListEntryDto waitDto, BookingDto BookingDto) {
        WaitListEntryDao waitListEntryDao = Mapper.splitOne(waitDto, WaitListEntryDao.class, Map.of());
        BookingDao bookingDao =Mapper.splitOne(BookingDto, BookingDao.class, Map.of());
        return waitRepo.moveToBooking(waitListEntryDao, bookingDao);
    }
}
