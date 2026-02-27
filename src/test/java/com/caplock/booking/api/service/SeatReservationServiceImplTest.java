package com.caplock.booking.api.service;

import com.caplock.booking.repository.EventSeatRepository;
import com.caplock.booking.service.BookingService;
import com.caplock.booking.service.EventService;
import com.caplock.booking.service.EventTicketConfigService;
import com.caplock.booking.service.impl.SeatReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class SeatReservationServiceImplTest {

    private EventService eventService;
    private BookingService bookingService;
    private EventSeatRepository eventSeatRepository;
    private EventTicketConfigService eventTicketConfigService;
    private SeatReservationServiceImpl service;

    @BeforeEach
    void setUp() {
        eventService = mock(EventService.class);
        bookingService = mock(BookingService.class);
        eventSeatRepository = mock(EventSeatRepository.class);
        eventTicketConfigService = mock(EventTicketConfigService.class);
        service = new SeatReservationServiceImpl(
                eventService,
                bookingService,
                eventSeatRepository,
                eventTicketConfigService
        );
    }

    @Test
    void assignSeatsTemp_eventNotFound_placeholder() {
        // TODO: implement
    }

    @Test
    void assignSeatsTemp_seatsNotConfigured_placeholder() {
        // TODO: implement
    }

    @Test
    void assignSeatsTemp_success_placeholder() {
        // TODO: implement
    }

    @Test
    void assignSeatsTemp_ticketSelection_placeholder() {
        // TODO: implement
    }

    @Test
    void onPaymentSucceeded_success_persistsSeats_placeholder() {
        // TODO: implement
    }

    @Test
    void onPaymentSucceeded_failure_clearsReservation_placeholder() {
        // TODO: implement
    }

    @Test
    void clearReservationOfSeats_releasesHeldSeats_placeholder() {
        // TODO: implement
    }

    @Test
    void getFreeSeatsForEvent_eventNotFound_placeholder() {
        // TODO: implement
    }
}
