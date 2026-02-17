package com.caplock.booking.repository;

import com.caplock.booking.entity.StatusEventEnum;
import com.caplock.booking.entity.dao.EventDao;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class EventRepo implements IEventRepository {
    @Override
    public Collection<EventDao> getAllEvents() {
        return List.of();
    }

    @Override
    public EventDao getEventByTitle(String title) {
        return null;
    }

    @Override
    public Collection<EventDao> getEventByCategory(String cat) {
        return List.of();
    }

    @Override
    public EventDao getEventById(long id) {
        return null;
    }

    @Override
    public Collection<EventDao> getEventsByDate(LocalDate date) {
        return List.of();
    }

    @Override
    public Collection<EventDao> getEventsByLocation(String location) {
        return List.of();
    }

    @Override
    public Collection<EventDao> getEventsByStatus(StatusEventEnum status) {
        return List.of();
    }

    @Override
    public boolean setEvent() {
        return false;
    }

    @Override
    public boolean updateEvent() {
        return false;
    }

    @Override
    public boolean deleteEvent() {
        return false;
    }

    @Override
    public boolean deleteByTitle(String title) {
        return false;
    }
}
