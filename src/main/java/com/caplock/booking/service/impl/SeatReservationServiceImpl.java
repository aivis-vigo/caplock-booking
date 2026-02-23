package com.caplock.booking.service.impl;

import com.caplock.booking.entity.TicketType;
import com.caplock.booking.entity.dto.BookingRequestDTO;
import com.caplock.booking.entity.dto.EventTicketConfigDto;
import com.caplock.booking.event.PaymentSucceededEvent;
import com.caplock.booking.service.BookingService;
import com.caplock.booking.service.EventService;
import com.caplock.booking.service.EventTicketConfigService;
import com.caplock.booking.service.SeatReservationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RequiredArgsConstructor
@Service
public class SeatReservationServiceImpl implements SeatReservationService {
    private volatile boolean paymentSuccess = false;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final ConcurrentHashMap<Long, ConcurrentHashMap<String, SeatReserver>> eventsSeat = new ConcurrentHashMap<>();
    private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final EventService eventService;
    private final EventTicketConfigService eventTicketConfigService;
    private final AtomicReference<Thread> reservationThread = new AtomicReference<>();
    private final CountDownLatch paymentLatch = new CountDownLatch(1);

    @EventListener
    public void onPaymentSucceeded(PaymentSucceededEvent event) {
        // Set payment success flag and signal the waiting thread
        paymentSuccess = event.Success();

        // Create a new countdown latch for the next payment
        // Note: CountDownLatch cannot be reset, so this is a limitation
        // Consider using CyclicBarrier or other mechanisms for multiple payments

        Thread currentThread = reservationThread.get();
        if (currentThread != null && currentThread.isAlive()) {
            try {
                currentThread.join(100); // Wait briefly for thread to complete
            } catch (InterruptedException e) {
                log.warn("Interrupted while waiting for reservation thread to complete");
                Thread.currentThread().interrupt();
            }
        }
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
                        int sectionIndex = (int) i;
                        String sb = configDto.getTicketType().name().charAt(0) + "" + alphabet[sectionIndex]
                                + (String.valueOf(j).length() < 2 ? ("0" + String.valueOf(j)) : String.valueOf(j))
                                + (String.valueOf(k).length() < 2 ? ("0" + String.valueOf(k)) : String.valueOf(k));// may be problem with cast to int, but for 40000 sections it should be ok
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


    public Pair<Boolean, String> assignSeatsTemp(long eventId, List<Pair<String, TicketType>> seats, long bookingId) {
        var check = checker(eventId, seats);
        if (!check.getValue0()) return check;

        try {
            lock.lock();
            for (var seat : seats)
                Objects.requireNonNull(getSeatMapOrInit(eventId)).replace(seat.getValue0(), new SeatReserver(eventId, bookingId, seat.getValue1()));
        } catch (Exception e) {
            log.error("Error during temporary seat assignment: {}", e.getMessage());
        } finally {
            lock.unlock();
        }


        return waitPaymentOrReturn(eventId, seats, bookingId);

    }

    private @NonNull Pair<Boolean, String> waitPaymentOrReturn(long eventId, List<Pair<String, TicketType>> seats, long bookingId) {
        Thread thread = new Thread(() -> {
            try {
                // Wait for payment notification or timeout (60 seconds)
                boolean paymentNotified = paymentLatch.await(60, TimeUnit.SECONDS);

                if (!paymentNotified) {
                    log.info("Payment timeout, clearing reservation for bookingId: {}", bookingId);
                    clearReservationOfSeats(eventId, bookingId);
                } else if (paymentSuccess) {
                    // Payment succeeded, finalize seats
                    var result = assignSeats(eventId, seats, bookingId);
                    log.info("Payment finalized: {} - {}", result.getValue0(), result.getValue1());
                } else {
                    // Payment failed, clear reservation
                    log.info("Payment failed, clearing reservation for bookingId: {}", bookingId);
                    clearReservationOfSeats(eventId, bookingId);
                }
            } catch (InterruptedException e) {
                log.error("Payment wait interrupted for bookingId: {}", bookingId);
                clearReservationOfSeats(eventId, bookingId);
                Thread.currentThread().interrupt();
            }
        });

        reservationThread.set(thread);
        thread.start();
        return Pair.with(true, "Temp reservation has been successfully assigned");
    }


    public Pair<Boolean, String> assignSeatsTemp(BookingRequestDTO.TicketSelectionDTO seatConf, long bookingId) {
        var ticketConf = eventTicketConfigService.getById(seatConf.getTicketConfigId()).orElseThrow(); // not proper exception
        var listOfSeats = List.of(Pair.with(seatConf.getSeat(), ticketConf.getTicketType()));
        return assignSeatsTemp(ticketConf.getEventId(), listOfSeats, bookingId);
    }


    private Pair<Boolean, String> assignSeats(long eventId, List<Pair<String, TicketType>> seats, long bookingId) {
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
        if (!invalid.toString().isBlank()) return Pair.with(false, "Invalid seat(s): " + invalid);
        if (!reserved.toString().isBlank()) return Pair.with(false, "Some seat is reserved: " + reserved);
        return Pair.with(true, "OK");
    }

    @Override
    public boolean clearReservationOfSeats(long eventId, long bookingId) {

        // TODO: Implement logic to clear temporary reservations for the given bookingId and eventId.
        eventsSeat.get(eventId).replaceAll((key, seat) ->
                seat.bookingId() == bookingId
                        ? new SeatReserver(-1, -1, seat.seatType())
                        : seat
        );

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
