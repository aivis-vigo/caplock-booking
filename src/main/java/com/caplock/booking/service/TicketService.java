package com.caplock.booking.service;

import com.caplock.booking.dto.Response;
import com.caplock.booking.dto.TicketDTO;
import com.caplock.booking.entity.Ticket;

import java.util.List;

public interface TicketService {

    Response<List<TicketDTO>> findAll();
    Response<List<TicketDTO>> findByHolderName(String holderName);
    Response<TicketDTO> create(Ticket newTicket);
    Response<?> update(Long id, Ticket updatedTicket);
    Response<?> deleteById(Long id);

}
