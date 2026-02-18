package com.caplock.booking.repository;

import com.caplock.booking.entity.StatusEventEnum;
import com.caplock.booking.entity.dao.*;

import java.time.LocalDate;
import java.util.Collection;

public interface IEventRepository {
    Collection<EventDao> getAllEvents();

    EventDao getEventByTitle(String title);

    Collection<EventDao> getEventByCategory(String cat);

    EventDao getEventById(long id);

    Collection<EventDao> getEventsByDate(LocalDate date);

    Collection<EventDao> getEventsByLocation(String location);

    Collection<EventDao> getEventsByStatus(StatusEventEnum status);

    boolean setEvent(EventDao eventDao);

    boolean updateEvent(long id,EventDao eventDao);

    boolean deleteEvent(long id);

    boolean deleteByTitle(String title);
}
