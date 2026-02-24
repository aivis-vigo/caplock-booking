package com.caplock.booking.service.impl;

import com.caplock.booking.entity.StatusTicketEnum;
import com.caplock.booking.entity.dto.CreateTicketDTO;
import com.caplock.booking.entity.dto.Response;
import com.caplock.booking.entity.dto.TicketDto;

import com.caplock.booking.entity.dao.TicketEntity;
import com.caplock.booking.repository.TicketRepository;
import com.caplock.booking.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final QrService qrService;

    @Override
    public List<TicketDto> findAll() {
        List<TicketDto> tickets = ticketRepository.findAll()
                .stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                .toList();

        log.info("Fetching all tickets, count: {}", (long) tickets.size());

        return tickets;
    }

    @Override
    public List<TicketDto> findByHolderName(String holderName) {
        List<TicketDto> tickets = ticketRepository.findByHolderName(holderName)
                .stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                .toList();

        log.info("Fetching tickets for holder: {}, count: {}", holderName, (long) tickets.size());

        return tickets;
    }

    @Override
    public TicketDto create(CreateTicketDTO newTicket) {
        log.info("Creating ticket for holder: {}", newTicket.getHolderName());

        TicketEntity fullTicketInfo = new TicketEntity();
        fullTicketInfo.setTicketType(newTicket.getTicketType());
        fullTicketInfo.setTicketCode(newTicket.getEvent());
        fullTicketInfo.setSeat(newTicket.getSeat());
        fullTicketInfo.setHolderName(newTicket.getHolderName());
        fullTicketInfo.setHolderEmail(newTicket.getHolderEmail());
        fullTicketInfo.setDiscountCode(newTicket.getDiscountCode());
        fullTicketInfo.setIssuedAt(LocalDateTime.now());
        fullTicketInfo.setStatus(StatusTicketEnum.Issued);

        TicketEntity savedTicket = ticketRepository.save(fullTicketInfo);

        try {
            String qrPath = qrService.generateAndSave(savedTicket.getTicketNumber(), savedTicket.getTicketNumber());
            savedTicket.setQrCode(qrPath);
            ticketRepository.save(savedTicket);
            log.info("QR code saved to: {}", qrPath);
        } catch (Exception e) {
            log.error("Failed to generate QR code for ticket: {}", savedTicket.getTicketNumber(), e);
        }

        log.info("Ticket created with id: {}", savedTicket.getId());

        TicketDto ticketDTO = modelMapper.map(savedTicket, TicketDto.class);

        return ticketDTO;
    }

    @Override
    public Optional<TicketDto> getById(Long id) {
        return ticketRepository.findById(id).map(ticket -> modelMapper.map(ticket, TicketDto.class));
    }

    @Override
    public TicketDto update(Long id, TicketDto updatedTicket) {
        TicketEntity ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));

        updatedTicket.setId(ticket.getId());
        var ticketDao = ticketRepository.save(modelMapper.map(updatedTicket, TicketEntity.class));

        log.info("Updated ticket with id: {}", id);

        return modelMapper.map(ticketDao, TicketDto.class);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new EntityNotFoundException("Ticket not found with id: " + id);
        }

        ticketRepository.deleteById(id);

        log.info("Deleted ticket with id: {}", id);

        return true;
    }

}
