package com.caplock.booking.service.impl;

import com.caplock.booking.entity.TicketType;
import com.caplock.booking.entity.dto.EventTicketConfigDto;
import com.caplock.booking.event.PaymentSucceededEvent;
import com.caplock.booking.service.EventService;
import com.caplock.booking.service.SeatReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RequiredArgsConstructor
@Service
public class SeatReservationServiceImpl implements SeatReservationService {
    private volatile boolean paymentSuccess = false;
    private volatile boolean notification = false;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final ConcurrentHashMap<Long, ConcurrentHashMap<String, SeatReserver>> eventsSeat = new ConcurrentHashMap<>();
    private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final EventService eventService;
    Thread thread = new Thread();

    @EventListener
    public void onPaymentSucceeded(PaymentSucceededEvent event) throws InterruptedException {
        // TODO: Load seats for the booking and finalize reservation.
        paymentSuccess = true;
        notification = true;
        thread.join();
    }

    public static void populateSeatsForEvent(List<EventTicketConfigDto> configDtos) {
        eventsSeat.put(configDtos.getFirst().getEventId(), buildSeatMap(configDtos));
    }

    private static ConcurrentHashMap<String, SeatReserver> buildSeatMap(List<EventTicketConfigDto> configDtos) {
        var seatMap = new ConcurrentHashMap<String, SeatReserver>();
        for (EventTicketConfigDto configDto : configDtos) {
            for (long i = 0; i < configDto.getNumOfSections(); i++) {
                for (long j = 0; j < configDto.getNumOfRows(); j++) {
                    for (long k = 0; k < configDto.getNumSeatsPerRow(); k++) {
                        String sb = alphabet[(int) i] +
                                (String.valueOf(j).length() < 2 ? ("0" + String.valueOf(j)) : String.valueOf(j)) +
                                (String.valueOf(k).length() < 2 ? ("0" + String.valueOf(k)) : String.valueOf(k)); // may be problem with cast to int, but for 40000 sections it should be ok
                        seatMap.put(sb, new SeatReserver(-1, -1, configDto.getTicketType()));
                    }
                }
            }
        }
        return seatMap;
    }

    private ConcurrentHashMap<String, SeatReserver> getSeatMapOrInit(long eventId) {
        var existing = eventsSeat.get(eventId);
        if (existing != null) return existing;

        var config = eventService.getEventConfByEventId(eventId);
        if (config == null) return null;

        var created = buildSeatMap(config);
        var prev = eventsSeat.putIfAbsent(eventId, created);
        return prev != null ? prev : created;
    }


    /*
     * private
     */
    public Pair<Boolean, String> assignSeatsTemp(long eventId, List<Pair<String, TicketType>> seats, long bookingId) {
        var check = checker(eventId, seats);
        if (!check.getValue0()) return check;

        try {
            lock.lock();
            for (var seat : seats)
                Objects.requireNonNull(getSeatMapOrInit(eventId)).put(seat.getValue0(), new SeatReserver(eventId, bookingId, seat.getValue1()));
        } catch (Exception e) {
            log.error("Error during temporary seat assignment: {}", e.getMessage());
        } finally {
            lock.unlock();
        }

        Thread thread = new Thread(() -> {
            while (!notification) Thread.onSpinWait();
            var result = paymentSuccess ? assignSeats(eventId, seats, bookingId) : Pair.with(false, "Payment failed, reservation cleared");
            if (!paymentSuccess) clearReservationOfSeats(eventId, bookingId);
            log.info("{} {}", result.getValue0(), result.getValue1());

        });
        thread.start();
        return Pair.with(true, "Temp reservation has been successfully assigned");

    }


    public Pair<Boolean, String> assignSeats(long eventId, List<Pair<String, TicketType>> seats, long bookingId) {
        var check = checker(eventId, seats);
        if (!check.getValue0()) return check;

        for (var seat : seats)
            Objects.requireNonNull(getSeatMapOrInit(eventId)).put(seat.getValue0(), new SeatReserver(eventId, bookingId, seat.getValue1()));

        return Pair.with(true, "Reservation has been successfully assigned");
    }

    private Pair<Boolean, String> checker(long eventId, List<Pair<String, TicketType>> seats) {
        if (eventService.getById(eventId).isEmpty())
            return Pair.with(false, "Event not found");

        var eventConfigs = eventService.getEventConfByEventId(eventId);
        for (var conf : eventConfigs) {
            var seatsOfType = seats.stream().filter(seat -> seat.getValue1() == conf.getTicketType()).toList();

            if (conf.getTicketType().equals(seatsOfType.stream().findFirst().map(Pair::getValue1).orElse(null))
                    & (conf.getAvailableSeats() == 0
                    || conf.getAvailableSeats() < seatsOfType.size()))
                return Pair.with(false, "Booking full, only " + conf.getAvailableSeats() + " can be only added");
        }
        var seatMap = getSeatMapOrInit(eventId);
        if (seatMap == null)
            return Pair.with(false, "Seats not configured for this event");

        StringBuilder reserved = new StringBuilder();
        StringBuilder invalid = new StringBuilder();

        for (var seatId : seatMap.keySet()) {
            SeatReserver seat = seatMap.get(seatId);
            if (seat == null) {
                invalid.append(seatId).append(" ");
                continue;
            }
            if ((seat.bookingId() != -1 && seat.eventId() != -1) && seats.contains(Pair.with(seatId, seat.seatType())))
                reserved.append(seatId).append(" ");
        }
        if (!invalid.isEmpty()) return Pair.with(false, "Invalid seat(s): " + invalid);
        if (!reserved.isEmpty()) return Pair.with(false, "Some seat is reserved: " + reserved);
        return Pair.with(true, "OK");
    }

    @Override
    public boolean clearReservationOfSeats(long eventId, long bookingId) {

        // TODO: Implement logic to clear temporary reservations for the given bookingId and eventId.
        return false;
    }

    @Override
    public List<Pair<String, TicketType>> getFreeSeatsForEvent(long eventId) {
        if (eventService.getById(eventId).isEmpty()) return List.of();

        var seatMap = getSeatMapOrInit(eventId);
        if (seatMap == null) return List.of();

        return seatMap.entrySet().stream()
                .filter(e -> e.getValue().bookingId() == -1)
                .map(e -> Pair.with(e.getKey(), e.getValue().seatType()))
                .toList();
    }


}
