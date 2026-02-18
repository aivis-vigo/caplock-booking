package com.caplock.booking.service;

import org.javatuples.Pair;

import java.util.List;

public interface ISeatReservationService {

    Pair<Boolean, String> assignSeats(String bookId, long eventId, long qty, List<String> seats);

    boolean clearReservationOfSeats(String bookId, long eventId, List<String> seats);
}
