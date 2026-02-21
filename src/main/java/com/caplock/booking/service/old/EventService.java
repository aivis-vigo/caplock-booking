package com.caplock.booking.service.old;

import com.caplock.booking.entity.StatusEventEnum;
import com.caplock.booking.entity.old.dao.EventDao;
import com.caplock.booking.entity.old.dto.EventDetailsDto;
import com.caplock.booking.entity.old.dto.EventDto;
import com.caplock.booking.repository.old.jpa.IEventRepository;
import com.caplock.booking.util.old.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class EventService implements IEventService {

    @Autowired
    private IEventRepository eventRepo;

//    @Autowired
//    private ICategoryService categoryService;

    @Override
    public EventDetailsDto getDetails(long id) {
        var dao = eventRepo.getEventById(id);
        var dto = Mapper.combine(EventDetailsDto.class, dao/**,categoryService.getCategoryByName(name)*/);
        dto.setEventDto(Mapper.combine(EventDto.class, dao));
        return dto;
    }

    @Override
    public Collection<EventDto> getAllEvents() {
        return eventRepo.getAllEvents().stream()
                .map(dao -> Mapper.combine(EventDto.class, dao/**,categoryService.getCategoryByName(name)*/))
                .toList();
    }

    @Override
    public EventDto getEventById(long id) {
        var dao = eventRepo.getEventById(id);
        return Mapper.combine(EventDto.class, dao/**,categoryService.getCategoryByName(name)*/);
    }

    @Override
    public boolean setEvent(EventDetailsDto dto) {
        var a = eventRepo.setEvent(Mapper.combine(EventDao.class, dto));
        return a;
    }

    @Override
    public boolean updateEvent(long id, EventDetailsDto dto) {
        dto.getEventDto().setId(id);
        var a = Mapper.splitOne(dto, EventDao.class, Map.of());

        return eventRepo.updateEvent(id, a);
    }

    @Override
    public boolean deleteEvent(long id) {
        return eventRepo.deleteEvent(id);
    }


    @Override
    public Collection<EventDto> getEventsByStatus(StatusEventEnum status) {
        return eventRepo.getEventsByStatus(status).stream()
                .map(dao -> Mapper.combine(EventDto.class, dao/**,categoryService.getCategoryByName(name)*/))
                .toList();
    }

    @Override
    public EventDto getEventByTitle(String title) {
        return null;
    }

    @Override
    public Collection<EventDto> getEventByCategory(String cat) {
        return List.of();
    }

    @Override
    public Collection<EventDto> getEventsByDate(LocalDate date) {
        return List.of();
    }

    @Override
    public Collection<EventDto> getEventsByLocation(String location) {
        return List.of();
    }

    @Override
    public boolean deleteByTitle(String title) {
        return false;
    }

    @Override
    public boolean unassignSeat(long eventId, String bookId) {
        return eventRepo.unAssignSeat(eventId, getEventById(eventId).getTitle(), bookId);
    }

    @Override
    public boolean assignSeat(long eventId, String bookingId, String seat) {

        return eventRepo.assignSeat(eventId, getEventById(eventId).getTitle(), bookingId, seat);
    }

    @Override
    public List<String> getSeatsFreeForEvent(long eventId) {
        return eventRepo.getSeatsForEvent(eventId);
    }

    @Override
    public List<String> getBookingSeatsForEvent(long eventId, String bookId) {
        return eventRepo.getBookingSeatsForEvent(eventId, bookId);
    }

}