package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.StatusBookingEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookingDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private Long eventId;
    private Long userId;
    private String confirmationCode;
    private BigDecimal totalPrice;
    private String discountCode;
    private StatusBookingEnum status;
}
