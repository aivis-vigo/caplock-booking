package com.caplock.booking.service;

import com.caplock.booking.entity.StatusEventEnum;
import com.caplock.booking.entity.dto.EventDto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class EventService implements IEventService{
    @Override
    public Collection<EventDto> getAllEvents() {
        return List.of();
    }

    @Override
    public EventDto getEventByTitle(String title) {
        return null;
    }

    @Override
    public Collection<EventDto> getEventByCategory(String cat) {
        return List.of();
    }

    @Override
    public EventDto getEventById(long id) {
        return null;
    }

    @Override
    public Collection<EventDto> getEventsByDate(LocalDate date) {
        return List.of();
    }

    @Override
    public Collection<EventDto> getEventsByLocation(String location) {
        return List.of();
    }

    @Override
    public Collection<EventDto> getEventsByStatus(StatusEventEnum status) {
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
