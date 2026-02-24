package com.caplock.booking.service;

import com.caplock.booking.entity.dto.CreateTicketDTO;
import com.caplock.booking.entity.dto.TicketDto;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    TicketDto create(CreateTicketDTO dto);
    Optional<TicketDto> getById(Long id);
    List<TicketDto> findAll();
    TicketDto update(Long id, TicketDto dto);
    boolean deleteById(Long id);
    List<TicketDto> findByHolderName(String holderName);
}
