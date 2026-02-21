package com.caplock.booking.service;

import com.caplock.booking.entity.dto.EventDto;

import java.util.List;
import java.util.Optional;

public interface EventService {
    EventDto create(EventDto dto);

    Optional<EventDto> getById(Long id);

    List<EventDto> getAll();

    EventDto update(Long id, EventDto dto);

    void delete(Long id);
}
