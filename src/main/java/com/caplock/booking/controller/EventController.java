package com.caplock.booking.controller;

import com.caplock.booking.entity.dto.EventDetailsDto;
import com.caplock.booking.entity.dto.EventDto;
import com.caplock.booking.entity.dto.EventTicketConfigDto;
import com.caplock.booking.entity.dto.Response;
import com.caplock.booking.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/{eventId}")
    public ResponseEntity<Response<EventDetailsDto>> getById(@PathVariable Long eventId) {
        return eventService.getEventDetailsByEventId(eventId)
                .map(eventDetails -> ResponseEntity.ok(
                        Response.<EventDetailsDto>builder()
                                .statusCode(200)
                                .message("OK")
                                .data(eventDetails)
                                .build()
                ))
                .orElseGet(() -> ResponseEntity.status(404).body(
                        Response.<EventDetailsDto>builder()
                                .statusCode(404)
                                .message("Event not found")
                                .build()
                ));
    }

    @GetMapping
    public List<EventDto> getAll() {
        return eventService.getAll();
    }

    @PostMapping
    public ResponseEntity<EventDto> create(@RequestBody EventDto dto) {
        return Optional.ofNullable(eventService.create(dto))
                .map(event -> ResponseEntity.status(HttpStatus.CREATED).body(event))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> update(@PathVariable Long id, @RequestBody EventDto dto) {
        return ResponseEntity.ok(eventService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
