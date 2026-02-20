package com.caplock.booking.api.repository;

import com.caplock.booking.entity.StatusWaitListEnum;
import com.caplock.booking.entity.StatusBookingEnum;
import com.caplock.booking.entity.dao.BookingDao;
import com.caplock.booking.entity.dao.WaitListEntryDao;
import com.caplock.booking.repository.WaitRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class WaitRepositoryTest {

    private WaitRepo repo;

    @BeforeEach
    void setUp() throws Exception {
        resetStaticState();
        repo = new WaitRepo();
        assertNotNull(repo);
    }

    @SuppressWarnings("unchecked")
    private void resetStaticState() throws Exception {
        // reset idCounter
        Field idCounterField = WaitRepo.class.getDeclaredField("idCounter");
        idCounterField.setAccessible(true);
        idCounterField.setLong(null, 1L);

        // reset mockWaitList
        Field mockWaitListField = WaitRepo.class.getDeclaredField("mockWaitList");
        mockWaitListField.setAccessible(true);
        List<WaitListEntryDao> mockWaitList = (List<WaitListEntryDao>) mockWaitListField.get(null);

        mockWaitList.clear();
        mockWaitList.add(new WaitListEntryDao(
                4,
                StatusWaitListEnum.Pending,
                "1",
                LocalDateTime.now(),
                2L,
                100L
        ));
    }

    @Test
    void getAllWaitList_returnsCopy_notSameReference() {
        Collection<WaitListEntryDao> a = repo.getAllWaitList();
        Collection<WaitListEntryDao> b = repo.getAllWaitList();

        assertNotNull(a);
        assertEquals(1, a.size());
        assertNotSame(a, b, "Repo should return a new list copy each time");
    }

    @Test
    void getAllWaitListByUser_filtersCorrectly() {
        assertEquals(1, repo.getAllWaitListByUser(100L).size());
        assertTrue(repo.getAllWaitListByUser(999L).isEmpty());
    }

    @Test
    void setWaitListToUser_setsFieldsAndAddsEntry() {
        WaitListEntryDao newEntry = new WaitListEntryDao();
        newEntry.setStatus(StatusWaitListEnum.Pending);
        newEntry.setEventId(100L); // same event as existing mock -> queue size=1 before add

        boolean ok = repo.setWaitListToUser(55L, newEntry);
        assertTrue(ok);

        var userEntries = repo.getAllWaitListByUser(55L);
        assertEquals(1, userEntries.size());

        WaitListEntryDao saved = userEntries.iterator().next();

        assertEquals(55L, saved.getUserId(), "UserId must be overwritten by method param");
        assertEquals(1L, saved.getId(), "First assigned id should be 1 after reset");
        assertNotNull(saved.getTimestamp(), "Timestamp must be set");
        assertEquals("POS-2", saved.getPositionInQ(), "Position should be POS-(existing+1) for same event"); // TODO: check is needed
    }

    @Test
    void setWaitListToUser_positionIsPerEvent_notGlobal() {
        // add to a different event => queue size for that event is 0
        WaitListEntryDao entryOtherEvent = new WaitListEntryDao();
        entryOtherEvent.setStatus(StatusWaitListEnum.Pending);
        entryOtherEvent.setEventId(999L);

        repo.setWaitListToUser(10L, entryOtherEvent);

        WaitListEntryDao saved = repo.getAllWaitListByUser(10L).iterator().next();
        assertEquals("POS-1", saved.getPositionInQ());
    }

    @Test
    void setWaitListToUser_incrementsIdCounter() {
        WaitListEntryDao e1 = new WaitListEntryDao();
        e1.setStatus(StatusWaitListEnum.Pending);
        e1.setEventId(100L);

        WaitListEntryDao e2 = new WaitListEntryDao();
        e2.setStatus(StatusWaitListEnum.Pending);
        e2.setEventId(100L);

        repo.setWaitListToUser(1L, e1);
        repo.setWaitListToUser(1L, e2);

        var list = repo.getAllWaitListByUser(1L).stream().toList();
        assertEquals(2, list.size());

        // ids assigned: 1 then 2 (after reset)
        assertEquals(1L, list.get(0).getId());
        assertEquals(2L, list.get(1).getId());
    }

    @Test
    void moveToBooking_removesEntryById_returnsTrueWhenRemoved() {
        // first insert an entry so we know its id after assignment
        WaitListEntryDao entry = new WaitListEntryDao();
        entry.setStatus(StatusWaitListEnum.Pending);
        entry.setEventId(100L);

        repo.setWaitListToUser(77L, entry);
        WaitListEntryDao saved = repo.getAllWaitListByUser(77L).iterator().next();

        BookingDao booking = new BookingDao(
                "bk-x",
                StatusBookingEnum.Processed,
                1,
                LocalDateTime.now(),
                null,
                100L,
                77L
        );

        boolean ok = repo.moveToBooking(saved, booking);
        assertTrue(ok, "removeIf should return true when element removed");

        assertTrue(repo.getAllWaitListByUser(77L).isEmpty());
    }

    @Test
    void moveToBooking_returnsFalseWhenIdNotFound() {
        WaitListEntryDao missing = new WaitListEntryDao();
        missing.setId(9999L);

        BookingDao booking = new BookingDao(
                "bk-x",
                StatusBookingEnum.Processed,
                1,
                LocalDateTime.now(),
                null,
                100L,
                77L
        );

        assertFalse(repo.moveToBooking(missing, booking));
    }
}
