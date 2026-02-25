package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateTicketDTO {

    private Long bookingId;
    private Long bookingItemId;
    private Long eventId;

    private String event;

    private String seat;

    private String holderName;
    private String holderEmail;

    private String discountCode;

}
