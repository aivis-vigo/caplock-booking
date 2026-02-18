package com.caplock.booking.service;

import com.caplock.booking.dto.Response;
import com.caplock.booking.dto.TicketDTO;

import com.caplock.booking.entity.Ticket;
import com.caplock.booking.repository.jpa.TicketRepository;
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
    public Response<List<TicketDTO>> findAll() {
        List<TicketDTO> tickets = ticketRepository.findAll()
                .stream()
                .map(ticket -> modelMapper.map(ticket, TicketDTO.class))
                .toList();

        return Response.<List<TicketDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success")
                .data(tickets)
                .build();
    }

    @Override
    public Response<List<TicketDTO>> findByHolderName(String holderName) {
        List<TicketDTO> tickets = ticketRepository.findByHolderName(holderName)
                .stream()
                .map(ticket -> modelMapper.map(ticket, TicketDTO.class))
                .toList();

        return Response.<List<TicketDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success")
                .data(tickets)
                .build();
    }

    @Override
    public Response<TicketDTO> create(Ticket newTicket) {
        Ticket savedTicket = ticketRepository.save(newTicket);
        TicketDTO ticketDTO = modelMapper.map(savedTicket, TicketDTO.class);

        return Response.<TicketDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Ticket created successfully")
                .data(ticketDTO)
                .build();
    }

}
