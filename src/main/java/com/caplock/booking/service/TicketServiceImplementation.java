package com.caplock.booking.service;

import com.caplock.booking.dto.Response;
import com.caplock.booking.dto.TicketDTO;

import com.caplock.booking.entity.Ticket;
import com.caplock.booking.repository.jpa.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

        log.info("Fetching all tickets, count: {}", (long) tickets.size());

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

        log.info("Fetching tickets for holder: {}, count: {}", holderName, (long) tickets.size());

        return Response.<List<TicketDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success")
                .data(tickets)
                .build();
    }

    @Override
    public Response<TicketDTO> create(Ticket newTicket) {
        log.info("Creating ticket for holder: {}", newTicket.getHolderName());

        Ticket savedTicket = ticketRepository.save(newTicket);

        log.info("Ticket created with id: {}", savedTicket.getId());

        TicketDTO ticketDTO = modelMapper.map(savedTicket, TicketDTO.class);

        return Response.<TicketDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Ticket created successfully")
                .data(ticketDTO)
                .build();
    }

    @Override
    public Response<?> update(Long id, Ticket updatedTicket) {
        log.info("Updating ticket with id: {}", id);

        ticketRepository.findById(id).ifPresent(ticket -> updatedTicket.setId(ticket.getId()));
        ticketRepository.save(updatedTicket);

        return Response.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Ticket updated successfully")
                .data(null)
                .build();
    }

    @Override
    public Response<?> deleteById(Long id) {
        log.info("Deleting ticket with id: {}", id);

        ticketRepository.deleteById(id);

        return Response.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Ticket deleted successfully")
                .data(null)
                .build();
    }

}
