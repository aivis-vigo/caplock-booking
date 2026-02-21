package com.caplock.booking.service;

import com.caplock.booking.entity.dto.EventTicketConfigDto;

import java.util.List;
import java.util.Optional;

public interface EventTicketConfigService {
    EventTicketConfigDto create(EventTicketConfigDto dto);

    Optional<EventTicketConfigDto> getById(Long id);

    List<EventTicketConfigDto> getAll();

    EventTicketConfigDto update(Long id, EventTicketConfigDto dto);

    void delete(Long id);
}
