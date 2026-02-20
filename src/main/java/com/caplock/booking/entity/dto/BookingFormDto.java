package com.caplock.booking.entity.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingFormDto {

    // --- Read-only (display on page, but not editable) ---
    private long eventId;
    private String eventTitle;
    private String eventLocation;
    private java.time.LocalDateTime eventDate;
    private java.time.LocalDateTime eventStartTime;

    // --- Not shown / derived (hidden or set server-side) ---
    private long userId;          // usually from logged-in user // may be null
    private String bookingId;     // optional (for edit flow)
    private String status;        // optional (better to set server-side)

    // --- User input ---
    @Builder.Default
    private List<Seat> seats = new ArrayList<>();

}

;