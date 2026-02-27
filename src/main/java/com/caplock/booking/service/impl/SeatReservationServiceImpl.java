package com.caplock.booking.service.impl;

import com.caplock.booking.entity.TicketType;
import com.caplock.booking.entity.dao.EventSeatsEntity;
import com.caplock.booking.entity.dto.EventTicketConfigDto;
import com.caplock.booking.entity.dto.TicketSelectionDTO;
import com.caplock.booking.event.paymentSucceededEvent;
import com.caplock.booking.repository.EventSeatRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RequiredArgsConstructor
@Service
public class SeatReservationServiceImpl implements SeatReservationService {
    private static final long PAYMENT_TIMEOUT_SECONDS = 60;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final ConcurrentHashMap<Long, ConcurrentHashMap<String, SeatReserver>> eventsSeat = new ConcurrentHashMap<>();
    private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final EventService eventService;
    private final BookingService bookingService;
    private final EventSeatRepository eventSeatRepository;
    private final EventTicketConfigService eventTicketConfigService;
    private final ConcurrentHashMap<Long, ScheduledFuture<?>> paymentTimeouts = new ConcurrentHashMap<>();
    private final ScheduledExecutorService reservationScheduler = Executors.newScheduledThreadPool(2);

    @EventListener
    public void onPaymentSucceeded(paymentSucceededEvent event) {
        var timeout = paymentTimeouts.remove(event.bookingId());
        if (timeout != null) {
            timeout.cancel(false);
        }

        if (event.success()) {

            var eventId = bookingService.getById(event.bookingId()).orElseThrow().getEventId();
            var seatMap = eventsSeat.get(eventId);
            List<Pair<String, TicketType>> seats = seatMap.entrySet().stream()
                    .filter(e -> e.getValue().bookingId() == event.bookingId()
                            && e.getValue().eventId() == eventId)
                    .map(e -> Pair.with(e.getKey(), e.getValue().seatType()))
                    .toList();
            assignSeats(eventId, seats, event.bookingId());

            for (var seat : seats) {
                eventSeatRepository.save(
                        new EventSeatsEntity(null, eventId, seat.getValue0(), seat.getValue1(), event.bookingId(), LocalDateTime.now()));
            }
            log.info("Payment succeeded for bookingId: {}", event.bookingId());
        } else {
            log.info("Payment failed, clearing reservation for bookingId: {}", event.bookingId());
            clearReservationOfSeatsByBookingId(event.bookingId());
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
            var seatMap = Objects.requireNonNull(getSeatMapOrInit(eventId));
            for (var seat : seats) {
                seatMap.replace(seat.getValue0(), new SeatReserver(eventId, bookingId, seat.getValue1()));
            }
        } catch (Exception e) {
            log.error("Error during temporary seat assignment: {}", e.getMessage());
        } finally {
            lock.unlock();
        }


        return waitPaymentOrReturn(eventId, bookingId);

    }

    private @NonNull Pair<Boolean, String> waitPaymentOrReturn(long eventId, long bookingId) {
        var timeout = reservationScheduler.schedule(
                () -> onPaymentTimeout(bookingId),
                PAYMENT_TIMEOUT_SECONDS,
                TimeUnit.SECONDS
        );
        var previous = paymentTimeouts.put(bookingId, timeout);
        if (previous != null) {
            previous.cancel(false);
            log.warn("Overwriting payment timeout for bookingId: {}", bookingId);
        }

        return Pair.with(true, "Temp reservation has been successfully assigned");
    }

    private void onPaymentTimeout(long bookingId) {
        var removed = paymentTimeouts.remove(bookingId);
        if (removed == null) return;

        log.info("Payment timeout, clearing reservation for bookingId: {}", bookingId);
        clearReservationOfSeatsByBookingId(bookingId);
    }


    public Pair<Boolean, String> assignSeatsTemp(TicketSelectionDTO seatConf, long bookingId) {
        var ticketConf = eventTicketConfigService.getById(seatConf.getTicketConfigId()).orElseThrow(); // not proper exception
        var listOfSeats = List.of(Pair.with(seatConf.getSeat(), ticketConf.getTicketType()));
        return assignSeatsTemp(ticketConf.getEventId(), listOfSeats, bookingId);
    }


    private Pair<Boolean, String> assignSeats(long eventId, List<Pair<String, TicketType>> seats, long bookingId) {
        var check = checker(eventId, seats);
        if (!check.getValue0()) return check;

        for (var seat : seats)
            Objects.requireNonNull(getSeatMapOrInit(eventId)).replace(seat.getValue0(), new SeatReserver(eventId, bookingId, seat.getValue1()));

        return Pair.with(true, "Reservation has been successfully assigned");
    }

    private Pair<Boolean, String> checker(long eventId, List<Pair<String, TicketType>> seats) {
        if (eventService.getById(eventId).isEmpty())
            return Pair.with(false, "Event not found");

        var eventConfigs = eventService.getEventConfByEventId(eventId);
        for (var conf : eventConfigs) {
            var seatsOfType = seats.stream().filter(seat -> seat.getValue1() == conf.getTicketType()).toList();

            if (conf.getTicketType().equals(seatsOfType.stream().findFirst().map(Pair::getValue1).orElse(null))
                    && (conf.getAvailableSeats() == 0
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
                .filter(e -> e.getValue().bookingId() == -1 && e.getValue().eventId() == -1)
                .map(e -> Pair.with(e.getKey(), e.getValue().seatType()))
                .toList();
    }

    private void clearReservationOfSeatsByBookingId(long bookingId) {
        eventsSeat.forEach((eventId, seatMap) ->
                seatMap.replaceAll((key, seat) ->
                        seat.bookingId() == bookingId
                                ? new SeatReserver(-1, -1, seat.seatType())
                                : seat
                )
        );

        eventSeatRepository.deleteById(bookingId);
    }


}
