package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dto.TicketDto;
import com.caplock.booking.entity.dao.TicketEntity;
import com.caplock.booking.repository.TicketRepository;
import com.caplock.booking.service.TicketService;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Override
    public TicketDto create(TicketDto dto) {
        TicketEntity saved = ticketRepository.save(Mapper.toEntity(dto));
        return Mapper.toDto(saved);
    }

    @Override
    public Optional<TicketDto> getById(Long id) {
        return ticketRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public List<TicketDto> getAll() {
        return ticketRepository.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public TicketDto update(Long id, TicketDto dto) {
        TicketEntity entity = Mapper.toEntity(dto);
        entity.setId(id);
        return Mapper.toDto(ticketRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }
}
