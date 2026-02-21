package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dto.EventTicketConfigDto;
import com.caplock.booking.entity.dao.EventTicketConfigEntity;
import com.caplock.booking.repository.EventTicketConfigRepository;
import com.caplock.booking.service.EventTicketConfigService;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventTicketConfigServiceImpl implements EventTicketConfigService {
    private final EventTicketConfigRepository eventTicketConfigRepository;

    @Override
    public EventTicketConfigDto create(EventTicketConfigDto dto) {
        EventTicketConfigEntity saved = eventTicketConfigRepository.save(Mapper.toEntity(dto));
        return Mapper.toDto(saved);
    }

    @Override
    public Optional<EventTicketConfigDto> getById(Long id) {
        return eventTicketConfigRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public List<EventTicketConfigDto> getAll() {
        return eventTicketConfigRepository.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public EventTicketConfigDto update(Long id, EventTicketConfigDto dto) {
        EventTicketConfigEntity entity = Mapper.toEntity(dto);
        entity.setId(id);
        return Mapper.toDto(eventTicketConfigRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        eventTicketConfigRepository.deleteById(id);
    }
}
