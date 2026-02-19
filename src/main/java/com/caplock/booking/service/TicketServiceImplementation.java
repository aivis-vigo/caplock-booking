package com.caplock.booking.service;

import com.caplock.booking.entity.dto.CreateTicketDTO;
import com.caplock.booking.entity.dto.Response;
import com.caplock.booking.entity.dto.TicketDTO;

import com.caplock.booking.entity.object.Ticket;
import com.caplock.booking.repository.jpa.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImplementation implements TicketService {

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final QrService qrService;

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
    public Response<TicketDTO> create(CreateTicketDTO newTicket) {
        log.info("Creating ticket for holder: {}", newTicket.getHolderName());

        Ticket fullTicketInfo = new Ticket(
                null,
                null,
                null,
                newTicket.getTicketType(),
                newTicket.getEvent(),
                newTicket.getSection(),
                newTicket.getRow(),
                newTicket.getSeatNumber(),
                newTicket.getHolderName(),
                newTicket.getHolderEmail(),
                null,
                null,
                "USD",
                newTicket.getDiscountCode(),
                LocalDateTime.now(),
                null,
                "PENDING",
                null,
                null,
                null
        );

        Ticket savedTicket = ticketRepository.save(fullTicketInfo);

        try {
            String qrPath = qrService.generateAndSave(savedTicket.getTicketNumber(), savedTicket.getTicketNumber());
            savedTicket.setQrCode(qrPath);
            ticketRepository.save(savedTicket);
            log.info("QR code saved to: {}", qrPath);
        } catch (Exception e) {
            log.error("Failed to generate QR code for ticket: {}", savedTicket.getTicketNumber(), e);
        }

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
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));

        updatedTicket.setId(ticket.getId());
        ticketRepository.save(updatedTicket);

        log.info("Updated ticket with id: {}", id);

        return Response.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Ticket updated successfully")
                .data(null)
                .build();
    }

    @Override
    public Response<?> deleteById(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new EntityNotFoundException("Ticket not found with id: " + id);
        }

        ticketRepository.deleteById(id);

        log.info("Deleted ticket with id: {}", id);

        return Response.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Ticket deleted successfully")
                .data(null)
                .build();
    }

}
