package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dto.EventTicketConfigDto;
import com.caplock.booking.event.PaymentSucceededEvent;
import com.caplock.booking.service.EventService;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
@Service
public class SeatReservationServiceImpl {
    private volatile boolean paymentSuccess = false;
    private volatile boolean notification = false;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final ConcurrentHashMap<Long, ConcurrentHashMap<String, SeatReserver>> eventsSeat = new ConcurrentHashMap<>();
    private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final EventService eventService;

    private record SeatReserver(long eventId, String bookingId) {
    }

    @EventListener
    public void onPaymentSucceeded(PaymentSucceededEvent event) {
        // TODO: Load seats for the booking and finalize reservation.
        paymentSuccess = true;
        notification = true;
    }

    public static void populateSeatsForEvent(EventTicketConfigDto configDto) {
        var seatMap = new ConcurrentHashMap<String, SeatReserver>();
        for (long i = 0; i < configDto.getNumOfSections(); i++) {
            for (long j = 0; j < configDto.getNumOfRows(); j++) {
                for (long k = 0; k < configDto.getNumSeatsPerRow(); k++) {
                    String sb = alphabet[(int) i] + String.valueOf(j) + String.valueOf(k); // may be problem with cast to int, but for 40000 sections it should be ok
                    seatMap.put(sb, new SeatReserver(-1, null));
                }
            }
        }
        eventsSeat.put(configDto.getId(), seatMap);
    }


    /*
     * private
     */
    public Pair<Boolean, String> assignSeatsTemp(long eventId, List<String> seatNums, String bookingId) {
        var check = checker(eventId, seatNums);
        if (!check.getValue0()) return check;

        for (var seat : seatNums)
            eventsSeat.get(eventId).put(seat, new SeatReserver(eventId, bookingId));

        return Pair.with(true, "Temp reservation has been successfully assigned");

        // Thread wait till event of payment is completed, if payment is successful, seats will be reserved, otherwise they will be released
        // while (!notification)Thread.onSpinWait();
        // if (payment successful) return assignSeats(eventId, seatNums, bookingId);

    }


    public Pair<Boolean, String> assignSeats(long eventId, List<String> seatNums, String bookingId) {
        var check = checker(eventId, seatNums);
        if (!check.getValue0()) return check;

        for (var seat : seatNums)
            eventsSeat.get(eventId).put(seat, new SeatReserver(eventId, bookingId));

        return Pair.with(true, "Reservation has been successfully assigned");
    }

    private Pair<Boolean, String> checker(long eventId, List<String> seatNums) {
        if (eventService.getById(eventId).isEmpty())
            return Pair.with(false, "Event not found");

        var eventConfig = eventService.getEventTicketConfigByEventId(eventId);
        if (eventConfig != null && (eventConfig.getAvailableSeats() == 0 || eventConfig.getAvailableSeats() < seatNums.size()))
            return Pair.with(false, "Booking full, only " + eventConfig.getAvailableSeats() + " can be only added");

        StringBuilder sb = new StringBuilder();

        for (var seatId : seatNums) {
            if (eventsSeat.get(eventId).get(seatId).bookingId != null & eventsSeat.get(eventId).get(seatId).eventId != -1)
                sb.append(seatId).append(" ");
        }
        if (!sb.isEmpty()) return Pair.with(false, "Some seat is reserved: " + sb);
        return Pair.with(true, "OK");
    }


    public boolean clearReservationOfSeats() {
        boolean fail = false;

        return !fail;
    }

}
