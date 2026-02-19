package com.caplock.booking.service;

import com.caplock.booking.entity.dto.CreateTicketDTO;
import com.caplock.booking.entity.dto.Response;
import com.caplock.booking.entity.dto.TicketDTO;
import com.caplock.booking.entity.object.Ticket;

import java.util.List;

public interface TicketService {

    Response<List<TicketDTO>> findAll();
    Response<List<TicketDTO>> findByHolderName(String holderName);
    Response<TicketDTO> create(CreateTicketDTO newTicket);
    Response<?> update(Long id, Ticket updatedTicket);
    Response<?> deleteById(Long id);

}
