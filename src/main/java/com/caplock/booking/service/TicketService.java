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

//package com.caplock.booking.service.impl;
//
//import com.caplock.booking.entity.dto.CreateTicketDTO;
//import com.caplock.booking.entity.dto.Response;
//import com.caplock.booking.entity.dto.TicketDTO;
//import com.caplock.booking.entity.old.object.Ticket;
//
//import java.util.List;
//
//public interface TicketService {
//
//    Response<List<TicketDTO>> findAll();
//    Response<List<TicketDTO>> findByHolderName(String holderName);
//    Response<TicketDTO> create(CreateTicketDTO newTicket);
//    Response<?> update(Long id, Ticket updatedTicket);
//    Response<?> deleteById(Long id);
//
//}
