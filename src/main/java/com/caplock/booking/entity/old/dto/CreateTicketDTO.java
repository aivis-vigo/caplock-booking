package com.caplock.booking.entity.old.dto;

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

    private TicketType ticketType;

    private String event;

    private String section;
    private String row;
    private String seatNumber;

    private String holderName;
    private String holderEmail;

    private String discountCode;

}
