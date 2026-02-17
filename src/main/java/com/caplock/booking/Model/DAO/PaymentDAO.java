package com.caplock.booking.Model.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDAO {
    private Long id;
    private int bookingId;
    private int status;
    private String paymentMethod;
    private LocalDate paidAt;
}
