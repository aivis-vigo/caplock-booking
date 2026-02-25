package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.PaymentMethodEnum;
import lombok.Data;

import java.util.List;

@Data
public class BookingRequestDTO {
    private Long eventId;
    private String holderName;
    private String holderEmail;
    private String discountCode;
    private List<TicketSelectionDTO> tickets;
    private PaymentMethodEnum paymentMethod;
}
