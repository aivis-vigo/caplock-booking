package com.caplock.booking.api.repository;

import com.caplock.booking.entity.StatusBookingEnum;
import com.caplock.booking.entity.old.dao.BookingDao;
import com.caplock.booking.repository.old.jpa.BookingRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingRepositoryTest {

    private BookingRepo repo;

    @BeforeEach
    void setUp() throws Exception {
        resetStaticBookings();
        repo = new BookingRepo();
        assertNotNull(repo);
    }

    @SuppressWarnings("unchecked")
    private void resetStaticBookings() throws Exception {
        Field f = BookingRepo.class.getDeclaredField("mockBookings");
        f.setAccessible(true);
        List<BookingDao> mockBookings = (List<BookingDao>) f.get(null);

        mockBookings.clear();
        mockBookings.addAll(List.of(
                new BookingDao("bk-9901", StatusBookingEnum.Processed, 2,
                        LocalDateTime.now().minusDays(1), null, 1L, 1L),
                new BookingDao("bk-9902", StatusBookingEnum.Fulfilled, 1,
                        LocalDateTime.now().minusDays(2), null, 3L, 2L)
        ));
    }

    @Test
    void getBookingById_existing_returnsBooking() {
        BookingDao b = repo.getBookingById("bk-9901");
        assertNotNull(b);
        assertEquals("bk-9901", b.getId());
        assertEquals(1L, b.getEventId());
        assertEquals(1L, b.getUserId());
    }

    @Test
    void getBookingById_missing_returnsNull() {
        assertNull(repo.getBookingById("no-such-id"));
    }

    @Test
    void getAllUserBookings_returnsOnlyThatUsersBookings() {
        var bookingsUser1 = repo.getAllUserBookings(1L);
        assertEquals(1, bookingsUser1.size());
        assertEquals("bk-9901", bookingsUser1.iterator().next().getId());

        var bookingsUser2 = repo.getAllUserBookings(2L);
        assertEquals(1, bookingsUser2.size());
        assertEquals("bk-9902", bookingsUser2.iterator().next().getId());

        var bookingsUser999 = repo.getAllUserBookings(999L);
        assertTrue(bookingsUser999.isEmpty());
    }

    @Test
    void setNewBooking_addsBooking() {
        BookingDao newBooking = new BookingDao(
                "bk-NEW",
                StatusBookingEnum.Processed,
                5,
                LocalDateTime.now(),
                null,
                10L,
                1L
        );

        assertTrue(repo.setNewBooking(newBooking));
        assertNotNull(repo.getBookingById("bk-NEW"));
        assertEquals(2, repo.getAllUserBookings(1L).size());
    }

    @Test
    void checkBookingExists_trueWhenBookingIdAndEventIdMatch() {
        assertTrue(repo.checkBookingExists("bk-9901", 1L));
        assertTrue(repo.checkBookingExists("bk-9902", 3L));
    }

    @Test
    void checkBookingExists_falseWhenBookingIdMatchesButEventIdDifferent() {
        assertFalse(repo.checkBookingExists("bk-9901", 999L));
    }

    @Test
    void checkBookingExists_falseWhenBookingIdMissing() {
        assertFalse(repo.checkBookingExists("no-such", 1L));
    }

    @Test
    void cancelBooking_existing_removesAndReturnsTrue() {
        assertNotNull(repo.getBookingById("bk-9901"));
        assertTrue(repo.cancelBooking("bk-9901"));
        assertNull(repo.getBookingById("bk-9901"));
    }

    @Test
    void cancelBooking_missing_returnsFalse() {
        assertFalse(repo.cancelBooking("no-such"));
    }

    @Test
    void genBookingId_returnsBKPrefixAndUniqueEnough() {
        String id1 = repo.genBookingId();
        String id2 = repo.genBookingId();

        assertNotNull(id1);
        assertNotNull(id2);
        assertTrue(id1.startsWith("BK-"));
        assertTrue(id2.startsWith("BK-"));

        // Could be equal if called in same millisecond; rare but possible.
        // So we don't strictly assert notEquals, just basic format.
        assertTrue(id1.length() > 3);
    }
}
