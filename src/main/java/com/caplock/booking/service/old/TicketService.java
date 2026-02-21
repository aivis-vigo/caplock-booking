package com.caplock.booking.service.old;

import com.caplock.booking.entity.old.dto.CreateTicketDTO;
import com.caplock.booking.entity.old.dto.Response;
import com.caplock.booking.entity.old.dto.TicketDTO;
import com.caplock.booking.entity.old.object.Ticket;

import java.util.List;

public interface TicketService {

    Response<List<TicketDTO>> findAll();
    Response<List<TicketDTO>> findByHolderName(String holderName);
    Response<TicketDTO> create(CreateTicketDTO newTicket);
    Response<?> update(Long id, Ticket updatedTicket);
    Response<?> deleteById(Long id);

}
