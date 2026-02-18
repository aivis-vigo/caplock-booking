package com.caplock.booking.service;

import com.caplock.booking.dto.Response;
import com.caplock.booking.dto.TicketDTO;

public interface TicketService {

    Response<TicketDTO> findByHolderName(String holderName);

}
