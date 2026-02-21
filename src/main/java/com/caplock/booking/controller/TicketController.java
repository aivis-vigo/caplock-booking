package com.caplock.booking.controller;

import com.caplock.booking.entity.dto.TicketDto;
import com.caplock.booking.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDto> create(@RequestBody TicketDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getById(@PathVariable Long id) {
        return ticketService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<TicketDto> getAll() {
        return ticketService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> update(@PathVariable Long id, @RequestBody TicketDto dto) {
        return ResponseEntity.ok(ticketService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
