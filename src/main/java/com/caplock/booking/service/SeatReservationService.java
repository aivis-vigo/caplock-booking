package com.caplock.booking.service;

import com.caplock.booking.entity.TicketType;
import com.caplock.booking.entity.dto.EventTicketConfigDto;
import com.caplock.booking.event.PaymentSucceededEvent;
import com.caplock.booking.service.impl.SeatReservationServiceImpl;
import org.javatuples.Pair;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface SeatReservationService {

    static void populateSeatsForEvent(EventTicketConfigDto configDto) {

    }

    @EventListener
    void onPaymentSucceeded(PaymentSucceededEvent event) throws InterruptedException;

    /*
     * private
     */
    Pair<Boolean, String> assignSeatsTemp(long eventId, List<Pair<String, TicketType>> seats, long bookingId);

    boolean clearReservationOfSeats(long eventId, long bookingId);


    List<Pair<String, TicketType>> getFreeSeatsForEvent(long eventId);

    record SeatReserver(long eventId, long bookingId, TicketType seatType) {
    }
}
