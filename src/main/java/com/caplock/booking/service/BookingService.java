package com.caplock.booking.service;

import com.caplock.booking.entity.dao.BookingDao;
import com.caplock.booking.entity.dto.*;
import com.caplock.booking.repository.IBookingRepository;
import com.caplock.booking.util.Mapper;
import jakarta.annotation.Nonnull;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        BookingDto bookingDto = Mapper.combine(BookingDto.class, bookingDao, Map.of("bookingId", "id"));

        // Fetch/map event separately using eventId from booking
        var eventDao = eventService.getEventById(bookingDao.getEventId());
        var eventDto = (eventDao == null) ? null : Mapper.combine(EventDto.class, eventDao, Map.of("bookingId", "id"));

        var form = Mapper.combine(BookingFormDto.class, eventDto, bookingDto, Map.of("bookingId", "id"));
        assert eventDto != null;
        List<Seat> seatAggregate = splitSeatAggregate(eventDto, bookingDao);
        form.setSeats(seatAggregate);
        form.setBookingId(id);
        return form;
    }

    public static Seat parseSeatCode(String code) {
        if (code == null) throw new IllegalArgumentException("Seat code is null");
        code = code.trim();

        // A12 -> section=A, row=1, seat=2
        if (!code.matches("[A-Za-z]\\d{2}")) {
            throw new IllegalArgumentException("Bad seat code: " + code);
        }

        String section = code.substring(0, 1);
        String row = code.substring(1, 2);
        String seatNumber = code.substring(2, 3);
        return new Seat(section, row, seatNumber);
    }


    @Nonnull
    private List<Seat> splitSeatAggregate(EventDto eventDto, BookingDao bookingDao) {
        List<Seat> seatAggregate = eventService
                .getBookingSeatsForEvent(eventDto.getId(), bookingDao.getId())
                .stream()
                .map(BookingService::parseSeatCode)
                .toList();
        return seatAggregate;
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

        return new BookingDetailsDto(bookingDto, eventDto, null);
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
                    dto.setSeats(splitSeatAggregate(event, dao));
                    return dto;
                })
                .toList();
    }

    @Override
    public Pair<Boolean, String> setNewBooking(BookingFormDto bookingFormDto) {

        bookingFormDto.setBookingId(bookingRepo.genBookingId());
        var val = seatReservationService.assignSeats(bookingFormDto.getBookingId(), bookingFormDto.getEventId(), bookingFormDto.getSeats().stream().map(x-> new Seat(x.getSection(),x.getRow(),x.getSeatNumber()).toString()).toList());
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
