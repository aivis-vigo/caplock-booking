package com.caplock.booking.service;

import com.caplock.booking.entity.dao.TicketEntity;
import com.caplock.booking.entity.dto.CreateTicketDTO;
import com.caplock.booking.entity.dto.Response;
import com.caplock.booking.entity.dto.TicketDto;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Response<List<TicketDto>> findAll();
    Response<List<TicketDto>> findByHolderName(String holderName);
    Response<TicketDto> create(CreateTicketDTO newTicket);
    Response<?> update(Long id, TicketEntity updatedTicket);
    Response<?> deleteById(Long id);
}
