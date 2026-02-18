package com.caplock.booking.repository;

import com.caplock.booking.entity.StatusEventEnum;
import com.caplock.booking.entity.dao.EventDao;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Repository
public class EventRepo implements IEventRepository {
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
    public boolean updateEvent(long eventId,EventDao updatedEvent) {
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
}
