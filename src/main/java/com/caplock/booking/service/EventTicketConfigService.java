package com.caplock.booking.service;

import com.caplock.booking.entity.dto.EventTicketConfigDto;

import java.util.List;
import java.util.Optional;

public interface EventTicketConfigService {
    List<EventTicketConfigDto> create(List<EventTicketConfigDto> dtos);

    Optional<EventTicketConfigDto> getById(Long id);

    List<EventTicketConfigDto> getAll();

    EventTicketConfigDto update(Long id, EventTicketConfigDto dto);

    void delete(Long id);

    public List<EventTicketConfigDto> getByEventId(Long id);
}
