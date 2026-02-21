package com.caplock.booking.service.old;

import com.caplock.booking.entity.StatusEventEnum;
import com.caplock.booking.entity.old.dto.EventDetailsDto;
import com.caplock.booking.entity.old.dto.EventDto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface IEventService {

    EventDetailsDto getDetails(long id);

    Collection<EventDto> getAllEvents();

    EventDto getEventByTitle(String title);

    Collection<EventDto> getEventByCategory(String cat);

    EventDto getEventById(long id);

    Collection<EventDto> getEventsByDate(LocalDate date);

    Collection<EventDto> getEventsByLocation(String location);

    Collection<EventDto> getEventsByStatus(StatusEventEnum status);

    boolean setEvent(EventDetailsDto dto);

    boolean updateEvent(long id, EventDetailsDto dto);

    boolean deleteEvent(long id);

    boolean deleteByTitle(String title);

    boolean unassignSeat(long eventId, String bookId);

    boolean assignSeat(long eventId, String bookingId, String seat);

    List<String> getSeatsFreeForEvent(long eventId);

    List<String> getBookingSeatsForEvent(long eventId, String bookId);
}

