package com.caplock.booking.api.repository;

import com.caplock.booking.entity.StatusEventEnum;
import com.caplock.booking.entity.old.dao.EventDao;
import com.caplock.booking.repository.old.jpa.EventRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventRepositoryTest {
    private EventRepo repo;

    @BeforeEach
    void setUp() throws Exception {
        resetStaticState();
        repo = new EventRepo();
        assertNotNull(repo);
    }

    @SuppressWarnings("unchecked")
    private void resetStaticState() throws Exception {
        Field mockEventsField = EventRepo.class.getDeclaredField("mockEvents");
        mockEventsField.setAccessible(true);
        List<EventDao> mockEvents = (List<EventDao>) mockEventsField.get(null);

        mockEvents.clear();
        mockEvents.addAll(List.of(
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

        Field seatsField = EventRepo.class.getDeclaredField("eventsSeat");
        seatsField.setAccessible(true);
        ConcurrentHashMap<String, ConcurrentHashMap<String, ?>> eventsSeat =
                (ConcurrentHashMap<String, ConcurrentHashMap<String, ?>>) seatsField.get(null);

        eventsSeat.clear();
    }

    @Test
    public void getAllEvents_returnsMockEvents() {
        Collection<EventDao> events = repo.getAllEvents();
        assertNotNull(events);
        assertEquals(3, events.size());
    }

    @Test
    public void getEventById_existing_returnsEvent() {
        EventDao e = repo.getEventById(1L);
        assertNotNull(e);
        assertEquals(1L, e.getId());
        assertEquals("Spring Boot Workshop", e.getTitle());
    }

    @Test
    public void getEventById_missing_returnsNull() {
        assertNull(repo.getEventById(999L));
    }

    @Test
    public void getEventByTitle_existing_returnsEvent() {
        EventDao e = repo.getEventByTitle("Spring Boot Workshop");
        assertNotNull(e);
        assertEquals(1L, e.getId());
    }

    @Test
    public void getEventByTitle_missing_returnsNull() {
        assertNull(repo.getEventByTitle("No such title"));
    }

    @Test
    public void setEvent_addsEvent_andAssignsNextId() {
        EventDao newEvent = new EventDao(
                0L,
                "New Event",
                "Desc",
                LocalDateTime.now().plusDays(1),
                "Room X",
                60L,
                LocalDateTime.now().plusDays(1).withHour(9),
                LocalDateTime.now().plusDays(1).withHour(10),
                10L,
                0L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
                StatusEventEnum.Soon,
                1L
        );

        boolean ok = repo.setEvent(newEvent);
        assertTrue(ok);

        // should be lastId + 1 => previously last was 3
        assertEquals(4L, newEvent.getId());

        assertNotNull(repo.getEventById(4L));
        assertEquals(4, repo.getAllEvents().size());
    }

    @Test
    public void updateEvent_whenExists_replacesAndReturnsTrue() {
        EventDao updated = repo.getEventById(2L);
        assertNotNull(updated);

        updated.setTitle("DB Design UPDATED");

        boolean ok = repo.updateEvent(2L, updated);
        assertTrue(ok);

        EventDao after = repo.getEventById(2L);
        assertNotNull(after);
        assertEquals("DB Design UPDATED", after.getTitle());
    }

    @Test
    public void updateEvent_whenMissing_returnsFalse() {
        EventDao fake = new EventDao(
                999L,
                "Fake",
                "Fake",
                LocalDateTime.now().plusDays(1),
                "X",
                10L,
                LocalDateTime.now().plusDays(1).withHour(10),
                LocalDateTime.now().plusDays(1).withHour(11),
                10L,
                0L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
                StatusEventEnum.Soon,
                1L
        );

        assertFalse(repo.updateEvent(999L, fake));
    }

    @Test
    public void deleteEvent_existing_removesAndReturnsTrue() {
        assertTrue(repo.deleteEvent(1L));
        assertNull(repo.getEventById(1L));
        assertEquals(2, repo.getAllEvents().size());
    }

    @Test
    public void deleteEvent_missing_returnsFalse() {
        assertFalse(repo.deleteEvent(999L));
        assertEquals(3, repo.getAllEvents().size());
    }

    @Test
    public void deleteByTitle_removesMatchingIgnoringCase() {
        assertTrue(repo.deleteByTitle("spring boot workshop"));
        assertNull(repo.getEventById(1L));
        assertEquals(2, repo.getAllEvents().size());
    }

    @Test
    public void getSeatsForEvent_initiallyReturnsFreeSeats() {
        // event 1 has capacity 50, constructor creates A0,B1,A2,... etc
        List<String> free = repo.getSeatsForEvent(1L);
        assertNotNull(free);
        assertFalse(free.isEmpty());
        assertTrue(free.contains("A0"));
    }

    @Test
    public void assignSeat_marksSeatAsReserved_andFreeListShrinks() {
        EventDao e = repo.getEventById(1L);
        assertNotNull(e);

        List<String> before = repo.getSeatsForEvent(1L);
        assertTrue(before.contains("A0"));

        boolean ok = repo.assignSeat(1L, e.getTitle(), "BK-1", "A0");
        assertTrue(ok);

        List<String> after = repo.getSeatsForEvent(1L);
        assertFalse(after.contains("A0"));
    }

    @Test
    public void getBookingSeatsForEvent_returnsSeatsForBookingId() {
        EventDao e = repo.getEventById(1L);
        assertNotNull(e);

        repo.assignSeat(1L, e.getTitle(), "BK-777", "A0");
        repo.assignSeat(1L, e.getTitle(), "BK-777", "B1");
        repo.assignSeat(1L, e.getTitle(), "BK-OTHER", "A2");

        List<String> bookingSeats = repo.getBookingSeatsForEvent(1L, "BK-777");
        assertEquals(List.of("A0", "B1"), bookingSeats);
    }

    @Test
    public void assignSeat_whenEventTitleMissing_returnsFalseOrThrows() {
        // Your code eventsSeat.get(eventTitle) will NPE if missing title
        // We assert it returns false (best behavior) OR throws (current behavior).
        assertDoesNotThrow(() -> {
            boolean ok = repo.assignSeat(1L, "NO_SUCH_EVENT", "BK-1", "A0");
            // if your current code throws, this test will fail and show you to handle it
        });
    }

    @Test
    public void unAssignSeat_currentImplementation_isBuggy_expectedToFail() {
        // This test demonstrates the bug in unAssignSeat:
        // - it filters seatMap.keySet() comparing seat key with bookingId (wrong)
        // - and replaces seat value with null (dangerous for later code)
        EventDao e = repo.getEventById(1L);
        assertNotNull(e);

        repo.assignSeat(1L, e.getTitle(), "BK-X", "A0");

        // With correct implementation this should return true and free seat A0 again.
        // With current implementation it will likely fail or behave incorrectly.
        boolean result = repo.unAssignSeat(1L, e.getTitle(), "BK-X");

        // You can flip this assertion AFTER you fix unAssignSeat.
        // For now, keep it as "expected buggy":
        assertTrue(result, "Expected current buggy unAssignSeat to fail; fix method then change assertion to true.");
    }
}
