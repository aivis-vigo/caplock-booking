package com.caplock.booking.controller;

import com.caplock.booking.entity.StatusBookingEnum;
import com.caplock.booking.entity.StatusPaymentEnum;
import com.caplock.booking.entity.TicketType;
import com.caplock.booking.entity.dao.BookingEntity;
import com.caplock.booking.entity.dto.*;
import com.caplock.booking.service.*;
import com.caplock.booking.service.impl.SeatReservationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.plaf.TableHeaderUI;
import java.awt.print.Book;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("api/v1/handle-booking")
@RequiredArgsConstructor
public class FlowController {

    private final EventService eventService;
    private final BookingService bookingService;
    private final SeatReservationServiceImpl seatReservationServiceImpl;
    private final PaymentService paymentService;
    private final TicketService ticketService;
    private final InvoiceService invoiceService;

    @PostMapping
    public String handleBooking(@ModelAttribute BookingRequestDTO request) throws InterruptedException {
        log.info("Started processing booking with id: {}", request.getEventId());

        // TODO: validations

        // create new booking using information provided from the event booking form
        // TODO: fetch logged in users information from spring to assign userId
        Long userId = (long) 111;
        BigDecimal totalPrice = new BigDecimal(140);
        // TODO: add discount count code field in the form
        String discountCode = "AV25B";
        String holderName = "John Doe";
        String holderEmail = "email@email.com";
        String paymentMethod = "CREDIT_CARD";
        String transactionId = "1558df5-8c9b-4e7a-9c3a-2b1e5f6a7b8c";

        EventDto evenDetails = eventService.getEventDetailsByEventId(request.getEventId()).orElseThrow(() -> new IllegalArgumentException("Object must be not null")).getEvent(); // not proper exception

        BookingDto newBooking = BookingDto.builder()
                .eventId(evenDetails.getId())
                .userId(userId)
                .totalPrice(totalPrice)
                .build();

        BookingDto bookingDetails = bookingService.createNewBooking(newBooking);

        // TODO: 4. assign seats
        // temp. assign selected seats
        // TODO: split these into 2 files
        List<BookingRequestDTO.TicketSelectionDTO> tickets = request.getTickets();
        log.info("Ticket seat config: {}, Seat number: {}", tickets.getFirst().getTicketConfigId().toString(), tickets.getFirst().getSeat());
        for (var ticket : tickets) {
            var result = seatReservationServiceImpl.assignSeatsTemp(ticket, bookingDetails.getId());
            if (!result.getValue0())
                log.error("Failed to assign seat {} for ticket config id {}", ticket.getSeat(), ticket.getTicketConfigId());
        }

        // TODO: 5. handle payment
        // TODO on success -> status changes to CONFIRMED
        // TODO on fail -> status changes to FAILED or CANCELED
        var paymentDto = paymentService.create(new PaymentDto(
                null,
                bookingDetails.getId(),
                totalPrice,
                StatusPaymentEnum.PENDING,
                paymentMethod,
                transactionId,
                "OK",
                LocalDateTime.now(),
                null
        ));

        //Thread.currentThread().wait(1000);

        if (paymentDto.getStatus().equals(StatusPaymentEnum.PAID)) {
            //Thread.currentThread().wait(1000);

            log.info("Seat: {}", tickets.getFirst().getSeat());

            // TODO: 6. generate tickets
            for (var ticket : tickets) {
                CreateTicketDTO newTicket = new CreateTicketDTO(
                        TicketType.VIP,
                        evenDetails.getTitle(),
                        ticket.getSeat(),
                        "John",
                        "john@example.com",
                        discountCode
                );
                ticketService.create(newTicket);
            }

            // TODO: 7. generate invoice
            var invoice = invoiceService.generateInvoiceFromForm(new InvoiceFormDto(
                    bookingDetails.getId(),
                    totalPrice,
                    discountCode,
                    paymentDto.getId(),
                    holderName,
                    holderEmail)); // it also generates invoice

            bookingDetails.setStatus(StatusBookingEnum.DONE);
            bookingService.update(bookingDetails.getId(), bookingDetails);
        } else {
            // Show error message to user and redirect to booking page
            log.error("Payment failed for booking id: {}, payment id: {}", bookingDetails.getId(), paymentDto.getId());
            bookingDetails.setStatus(StatusBookingEnum.CANCELLED);
            bookingService.update(bookingDetails.getId(), bookingDetails);
        }
        return "redirect:/ui/bookings";
    }

}
