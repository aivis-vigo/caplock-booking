package com.caplock.booking.service;

import com.caplock.booking.entity.StatusEventEnum;
import com.caplock.booking.entity.dto.*;

import java.time.LocalDate;
import java.util.Collection;

public interface IEventService {

    EventDetailsDto getDetails(long id);

    Collection<EventDto> getAllEvents();

    EventDto getEventByTitle(String title);

    Collection<EventDto> getEventByCategory(String cat);

    EventDto getEventById(long id);

    Collection<EventDto> getEventsByDate(LocalDate date);

    Collection<EventDto> getEventsByLocation(String location);

    Collection<EventDto> getEventsByStatus(StatusEventEnum status);

    boolean setEvent(EventDto dto);

    boolean updateEvent(long id, EventDto dto);

    boolean deleteEvent(long id);

    boolean deleteByTitle(String title);

}
