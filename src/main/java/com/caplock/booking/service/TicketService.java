package com.caplock.booking.service;

import com.caplock.booking.entity.dto.TicketDto;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    TicketDto create(TicketDto dto);

    Optional<TicketDto> getById(Long id);

    List<TicketDto> getAll();

    TicketDto update(Long id, TicketDto dto);

    void delete(Long id);
}
