package com.caplock.booking.controller;

import com.caplock.booking.entity.StatusPaymentEnum;
import com.caplock.booking.entity.dto.*;
import com.caplock.booking.service.*;
//import com.caplock.booking.service.impl.FlowServiceImpl;
import com.caplock.booking.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
//    private final FlowServiceImpl flowServiceImpl;
    private final UserService userService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;
    private final TicketService ticketService;
    private final BookingItemService bookingItemService;
    private final BookingService bookingService;

    @GetMapping("/{eventId}")
    public ResponseEntity<Response<EventDetailsDto>> getById(@PathVariable Long eventId) {
/*        var b = bookingService.getById(1L).get();
        b.setId(null);
        var bi= bookingItemService.getAllByBookingId(1L);
        bi = bi.stream()
                .map(x -> {x.setId(null); x.setSelectedSeats(List.of("B0001", "B0010")); return x;})
                .toList();
        var r = flowServiceImpl.processBookingFlow(2L,
                eventService.getEventDetailsByEventId(1L).get(),
                b,
                bi,
                new PaymentDto(1L,
                                1L,
                                BigDecimal.valueOf(100L),
                                StatusPaymentEnum.Paid,
                                "S",
                                "S",
                                "S",
                                null,
                                null)); // Example flow processing, replace with actual parameters as needed
                                */

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
