package com.caplock.booking.controller;

import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getById(@PathVariable Long id) {
        return bookingService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<BookingDto> getAll() {
        return bookingService.getAll();
    }

//    @PostMapping
//    public ResponseEntity<BookingDto> create(@RequestBody BookingDto dto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.create(dto, new ArrayList<>()).getValue0().get()); // Optional check needed here
//    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDto> update(@PathVariable Long id, @RequestBody BookingDto dto) {
        return ResponseEntity.ok(bookingService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
