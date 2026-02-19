package com.caplock.booking.service;

import com.caplock.booking.entity.dao.BookingDao;
import com.caplock.booking.entity.dto.BookingDetailsDto;
import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.BookingFormDto;
import com.caplock.booking.entity.dto.EventDto;
import com.caplock.booking.repository.IBookingRepository;
import com.caplock.booking.util.Mapper;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private IBookingRepository bookingRepo;
    @Autowired
    private IEventService eventService;
    @Autowired
    private ISeatReservationService seatReservationService;

    @Override
    public BookingFormDto getBookingFormById(String id, long userId) {
        BookingDao bookingDao = bookingRepo.getBookingById(id);
        if (bookingDao == null) return null;

        // Map bookingDao -> BookingDto (flat -> flat)
        BookingDto bookingDto = Mapper.combine(BookingDto.class, bookingDao);

        // Fetch/map event separately using eventId from booking
        var eventDao = eventService.getEventById(bookingDao.getEventId());
        var eventDto = (eventDao == null) ? null : Mapper.combine(EventDto.class, eventDao);

        var form = Mapper.combine(BookingFormDto.class, eventDto, bookingDto);
        assert eventDto != null;
        form.setSeats(eventService.getBookingSeatsForEvent(eventDto.getId(), bookingDao.getId()));
        return form;
    }

    @Override
    public BookingDetailsDto getDetails(String id) {
        BookingDao bookingDao = bookingRepo.getBookingById(id);
        if (bookingDao == null) return null;

        // Map bookingDao -> BookingDto (flat -> flat)
        BookingDto bookingDto = Mapper.combine(BookingDto.class, bookingDao);

        // Fetch/map event separately using eventId from booking
        var eventDao = eventService.getEventById(bookingDao.getEventId());
        var eventDto = (eventDao == null) ? null : Mapper.combine(EventDto.class, eventDao);

        return new BookingDetailsDto(bookingDto, eventDto);
    }

    @Override
    public BookingDto getBookingById(String id) {
        var dao = bookingRepo.getBookingById(id);
        return Mapper.combine(BookingDto.class, dao, eventService.getEventById(dao.getEventId()));
    }

    @Override
    public Collection<BookingDto> getAllUserBookings(long userId) {
        return bookingRepo.getAllUserBookings(userId).stream()
                .map(dao -> {
                    var event = eventService.getEventById(dao.getEventId());

                    BookingDto dto = Mapper.combine(BookingDto.class, dao, event);
                    dto.setSeats(eventService.getBookingSeatsForEvent(event.getId(), dao.getId()));
                    return dto;
                })
                .toList();
    }

    @Override
    public Pair<Boolean, String> setNewBooking(BookingFormDto bookingFormDto) {
        bookingFormDto.setBookingId(bookingRepo.genBookingId());
        var val = seatReservationService.assignSeats(bookingFormDto.getBookingId(), bookingFormDto.getEventId(), bookingFormDto.getSeats());
        if (!val.getValue0()) return val;
        BookingDao dao = Mapper.splitOne(bookingFormDto, BookingDao.class,   Map.of("bookingId", "id"));
        boolean success = bookingRepo.setNewBooking(dao);
        return Pair.with(success, success ? "Success" : "Error");
    }

    @Override
    public boolean cancelBooking(String bookingId) {
        var b= bookingRepo.getBookingById(bookingId);
        assert b != null;
        var val = seatReservationService.clearReservationOfSeats(b.getId(), b.getEventId());
        if (!val) return val;
        return bookingRepo.cancelBooking(bookingId);
    }
}
