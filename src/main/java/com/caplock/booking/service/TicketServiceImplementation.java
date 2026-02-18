package com.caplock.booking.service;

import com.caplock.booking.dto.Response;
import com.caplock.booking.dto.TicketDTO;

import com.caplock.booking.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImplementation implements TicketService {

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response<List<TicketDTO>> findByHolderName(String holderName) {
        List<TicketDTO> tickets = ticketRepository.findByHolderName(holderName)
                .stream()
                .map(ticket -> modelMapper.map(ticket, TicketDTO.class))
                .toList();

        return Response.<List<TicketDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success!")
                .data(tickets)
                .build();
    }

}
