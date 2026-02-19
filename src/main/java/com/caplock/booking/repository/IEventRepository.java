package com.caplock.booking.repository;

import com.caplock.booking.entity.StatusEventEnum;
import com.caplock.booking.entity.dao.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface IEventRepository {
    Collection<EventDao> getAllEvents();

    EventDao getEventByTitle(String title);

    Collection<EventDao> getEventByCategory(String cat);

    EventDao getEventById(long id);

    Collection<EventDao> getEventsByDate(LocalDate date);

    Collection<EventDao> getEventsByLocation(String location);

    Collection<EventDao> getEventsByStatus(StatusEventEnum status);

    boolean setEvent(EventDao eventDao);

    boolean updateEvent(long id, EventDao eventDao);

    boolean deleteEvent(long id);

    boolean deleteByTitle(String title);

    boolean unAssignSeat(long eventId, String eventTitle, String bookId);

    boolean assignSeat(long eventId, String eventTitle, String bookingId, String seat);

    List<String> getSeatsForEvent(long eventId);

    List<String> getBookingSeatsForEvent(long eventId, String bookId);
}
