package com.caplock.booking.controller;

import com.caplock.booking.entity.dto.BookingItemDto;
import com.caplock.booking.service.BookingItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking-items")
@RequiredArgsConstructor
public class BookingItemController {
    private final BookingItemService bookingItemService;

    @PostMapping
    public ResponseEntity<BookingItemDto> create(@RequestBody BookingItemDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingItemService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingItemDto> getById(@PathVariable Long id) {
        return bookingItemService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<BookingItemDto> getAll() {
        return bookingItemService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingItemDto> update(@PathVariable Long id, @RequestBody BookingItemDto dto) {
        return ResponseEntity.ok(bookingItemService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
