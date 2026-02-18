package com.caplock.booking.service;

import com.caplock.booking.entity.dao.BookingDao;
import com.caplock.booking.entity.dto.BookingDetailsDto;
import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.EventDto;
import com.caplock.booking.repository.IBookingRepository;
import com.caplock.booking.repository.IEventRepository;
import com.caplock.booking.util.Mapper;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private IBookingRepository bookingRepo;
    @Autowired
    private IEventService eventService;

    @Override
    public BookingDetailsDto getDetails(long id) {
        BookingDao bookingDao = bookingRepo.getBookingById(id);
        if (bookingDao == null) return null;

        // Map bookingDao -> BookingDto (flat -> flat)
        BookingDto bookingDto = Mapper.combine(BookingDto.class,bookingDao);

        // Fetch/map event separately using eventId from booking
        var eventDao = eventService.getEventById(bookingDao.getEventId());
        var eventDto = (eventDao == null) ? null : Mapper.combine(EventDto.class, eventDao);

        return new BookingDetailsDto(bookingDto, eventDto);
    }

    @Override
    public BookingDto getBookingById(long id) {
        var dao = bookingRepo.getBookingById(id);
        return Mapper.combine(BookingDto.class,dao, eventService.getEventById(dao.getEventId()));
    }

    @Override
    public Collection<BookingDto> getAllUserBookings(long userId) {
        var list= bookingRepo.getAllUserBookings(userId).stream()
                .map(dao -> Mapper.combine(BookingDto.class, dao, eventService.getEventById(dao.getEventId())))
                .toList();
        return list;
    }

    @Override
    public Pair<Boolean, String> setNewBooking(BookingDto bookingDto) {
        // refactor in different method in eventService for checking seats
        var eventDet = eventService.getDetails(bookingDto.getEventId());

        if (eventDet == null) return Pair.with(false, "Event not found");

        if (checkBookingExists(bookingDto)) return Pair.with(false, "Booking already exists");

        if (eventDet.getBookedSeats() + bookingDto.getQty() > eventDet.getCapacity()) {
            return Pair.with(false, "Booking full");
        }

        BookingDao dao =Mapper.splitOne(bookingDto, BookingDao.class);
        boolean success = bookingRepo.setNewBooking(dao);
        return Pair.with(success, success ? "Success" : "Error");
    }

    public boolean checkBookingExists(BookingDto booking) {
        return bookingRepo.checkBookingExists(Mapper.splitOne(booking, BookingDao.class));
    }

    @Override
    public boolean cancelBooking(String bookingId) {
        return bookingRepo.cancelBooking(bookingId);
    }
}
