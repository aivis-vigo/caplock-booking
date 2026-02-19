package com.caplock.booking.repository;

import com.caplock.booking.entity.StatusEventEnum;
import com.caplock.booking.entity.dao.BookingDao;
import com.caplock.booking.entity.dao.EventDao;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;


@Repository
public class EventRepo implements IEventRepository {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, SeatReserver>> eventsSeat = new ConcurrentHashMap<>();

    private record SeatReserver(long eventId, String bookingId) {
    }

    public EventRepo() {
        for (var mock : mockEvents) {
            var seatMap = new ConcurrentHashMap<String, SeatReserver>();
            for (int i = 0; i < mock.getCapacity(); i++) {
                seatMap.put(i % 2 == 0 ? ("A" + i) : ("B" + i), new SeatReserver(-1, null));
            }
            eventsSeat.put(mock.getTitle(), seatMap);
        }
    }

    private static final List<EventDao> mockEvents = new ArrayList<>(List.of(
            new EventDao(1L, "Spring Boot Workshop", "Deep dive into MVC", LocalDateTime.now().plusDays(5),
                    "Room 101", 120L, LocalDateTime.now().plusDays(5).withHour(10), LocalDateTime.now().plusDays(5).withHour(12),
                    50L, 10L, LocalDateTime.now().plusDays(4), LocalDateTime.now().minusDays(1),
                    LocalDateTime.now(), 1L, StatusEventEnum.Soon, 1L),

            new EventDao(2L, "Database Design", "SQL vs NoSQL", LocalDateTime.now().plusDays(10),
                    "Room 202", 90L, LocalDateTime.now().plusDays(10).withHour(14), LocalDateTime.now().plusDays(10).withHour(15),
                    30L, 30L, LocalDateTime.now().plusDays(9), LocalDateTime.now().minusDays(2),
                    LocalDateTime.now(), 1L, StatusEventEnum.Full, 1L),
            new EventDao(3L, "Database Design", "SQL vs NoSQL", LocalDateTime.now().plusDays(10),
                    "Room 202", 90L, LocalDateTime.now().plusDays(10).withHour(14), LocalDateTime.now().plusDays(10).withHour(15),
                    28L, 15L, LocalDateTime.now().plusDays(9), LocalDateTime.now().minusDays(2),
                    LocalDateTime.now(), 1L, StatusEventEnum.Full, 1L)
    ));

    @Override
    public Collection<EventDao> getAllEvents() {
        return mockEvents;
    }

    @Override
    public EventDao getEventByTitle(String title) {
        return mockEvents.stream().filter(e -> Objects.equals(e.getTitle(), title)).findFirst().orElse(null);
    }

    @Override
    public Collection<EventDao> getEventByCategory(String cat) {
        return mockEvents.stream().filter(e -> e.getCategoryId() == -1).toList();
    }

    @Override
    public EventDao getEventById(long id) {
        return mockEvents.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Collection<EventDao> getEventsByDate(LocalDate date) {
        return List.of();
    }

    @Override
    public Collection<EventDao> getEventsByLocation(String location) {
        return List.of();
    }

    @Override
    public Collection<EventDao> getEventsByStatus(StatusEventEnum status) {
        return List.of();
    }

    @Override
    public boolean setEvent(EventDao event) {
        // In a real scenario, you'd check if ID exists; here we just add
        return mockEvents.add(event);
    }

    @Override
    public boolean updateEvent(long eventId, EventDao updatedEvent) {
        for (int i = 0; i < mockEvents.size(); i++) {
            if (mockEvents.get(i).getId() == updatedEvent.getId()) {
                mockEvents.set(i, updatedEvent);
                return true;
            }
        }
        return false; // Event not found
    }

    @Override
    public boolean deleteEvent(long id) {
        return mockEvents.removeIf(event -> event.getId() == id);
    }

    @Override
    public boolean deleteByTitle(String title) {
        return mockEvents.removeIf(event -> event.getTitle().equalsIgnoreCase(title));
    }

    @Override
    public boolean assignSeat(long eventId, String eventTitle, String bookingId, String seat) {
        try {
            lock.lock();
            if (eventsSeat.get(eventTitle).put(seat, new SeatReserver(eventId, bookingId)) == null) {
                return false;
            }
        } catch (Exception ex) {
            //log
        } finally {
            lock.unlock();
        }
        return true;
    }

    @Override
    public List<String> getSeatsForEvent(long eventId) {
        var event = mockEvents.stream()
                .filter(e -> e.getId() == eventId)
                .findFirst()
                .orElse(null);

        if (event == null) return List.of();

        var seatMap = eventsSeat.get(event.getTitle());
        if (seatMap == null) return List.of();

        return seatMap.entrySet()
                .stream()
                .filter(e -> e.getValue().bookingId == null)
                .map(java.util.Map.Entry::getKey)
                .sorted()
                .toList();
    }

    @Override
    public List<String> getBookingSeatsForEvent(long eventId, String bookId) {
        var event = mockEvents.stream()
                .filter(e -> e.getId() == eventId)
                .findFirst()
                .orElse(null);

        if (event == null) return List.of();

        var seatMap = eventsSeat.get(event.getTitle());
        if (seatMap == null) return List.of();

        var a = seatMap.entrySet()
                .stream()
                .filter(e -> e.getValue().bookingId != null && e.getValue().bookingId.equals(bookId))
                .map(java.util.Map.Entry::getKey)
                .sorted()
                .toList();
        return a;
    }

    @Override
    public boolean unAssignSeat(long eventId, String eventTitle, String bookId) {
        if (eventsSeat.containsKey(eventTitle)) {
            var seatMap = (ConcurrentHashMap<String, SeatReserver>) eventsSeat.get(eventTitle);

            var reservedSeats = seatMap.keySet().stream()
                    .filter(b -> Objects.equals(b, bookId)).toList();
            try {
                assert !reservedSeats.isEmpty();
                lock.lock();
                for (var seat : reservedSeats) {
                    if (eventsSeat.get(eventTitle).containsKey(seat)) {
                        eventsSeat.get(eventTitle).replace(seat, null);

                    } else {
                        return false;
                    }
                }

            } catch (Exception ex) {
                //log
            } finally {
                lock.unlock();
            }
            return true;
        }
        return false;

    }

    private boolean syncData() {
        try {
            lock.lock();

            // sync with database
        } catch (Exception ex) {
            //log
        } finally {
            lock.unlock();
        }
        return true;
    }
}
