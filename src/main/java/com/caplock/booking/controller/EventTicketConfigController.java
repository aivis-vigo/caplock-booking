package com.caplock.booking.controller;

import com.caplock.booking.entity.dto.EventTicketConfigDto;
import com.caplock.booking.service.EventTicketConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-ticket-configs")
@RequiredArgsConstructor
public class EventTicketConfigController {
    private final EventTicketConfigService eventTicketConfigService;

    @PostMapping
    public ResponseEntity<EventTicketConfigDto> create(@RequestBody EventTicketConfigDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventTicketConfigService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventTicketConfigDto> getById(@PathVariable Long id) {
        return eventTicketConfigService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<EventTicketConfigDto> getAll() {
        return eventTicketConfigService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventTicketConfigDto> update(@PathVariable Long id, @RequestBody EventTicketConfigDto dto) {
        return ResponseEntity.ok(eventTicketConfigService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventTicketConfigService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
