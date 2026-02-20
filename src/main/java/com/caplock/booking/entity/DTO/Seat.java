package com.caplock.booking.entity.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    private String section;
    private String row;
    private String seatNumber;

    @Override
    public String toString() {
        return section + row + seatNumber;
    }
}
