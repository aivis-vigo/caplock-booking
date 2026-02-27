package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dto.*;
import com.caplock.booking.entity.dao.*;
import com.caplock.booking.repository.EventRepository;
import com.caplock.booking.service.*;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventTicketConfigService eventTicketConfigService;
    private final ObjectProvider<SeatReservationService> seatReservationServiceProvider;
    private final ModelMapper modelMapper;

    @Override
    public EventDto create(EventDto dto) {

        EventEntity saved = eventRepository.save(Mapper.toEntity(dto));
        return Mapper.toDto(saved);
    }

    @Override
    public Optional<EventDto> getById(Long id) {
        return eventRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public List<EventDto> getAll() {
        return eventRepository.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public EventDto update(Long id, EventDto dto) {
        EventEntity entity = Mapper.toEntity(dto);
        entity.setId(id);
        return Mapper.toDto(eventRepository.save(entity));
    }

    @Override
    public EventTicketConfigDto updateEventConfig(Long eventId, EventTicketConfigDto dto) {
        return eventTicketConfigService.update(dto.getId(), dto);
    }

    @Override
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Optional<EventDetailsDto> getEventDetailsByEventId(Long eventId) {
        return eventRepository.findById(eventId)
                .flatMap(event -> {
                    var eventDto = modelMapper.map(event, EventDto.class);
                    var eventTicketConfigDto = eventTicketConfigService.getByEventId(eventId);
                    var freeSeats = seatReservationServiceProvider.getObject().getFreeSeatsForEvent(eventId);
                    return Optional.of(new EventDetailsDto(eventDto, eventTicketConfigDto, freeSeats));
                });
    }

    @Override
    public List<EventTicketConfigDto> getEventConfByEventId(Long eventId) {
        return eventTicketConfigService.getByEventId(eventId);
    }
}
