package com.caplock.booking.service;

import com.caplock.booking.entity.StatusBookingEnum;
import com.caplock.booking.entity.StatusPaymentEnum;
import com.caplock.booking.entity.TicketType;
import com.caplock.booking.entity.dto.*;
import com.caplock.booking.repository.EventTicketConfigRepository;
import com.caplock.booking.service.impl.SeatReservationServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowServiceImpl implements FlowService {

    private final EventService eventService;
    private final BookingService bookingService;
    private final SeatReservationServiceImpl seatReservationServiceImpl;
    private final PaymentService paymentService;
    private final TicketService ticketService;
    private final InvoiceService invoiceService;
    private final EventTicketConfigRepository eventTicketConfigRepository;

    @Override
    public void handleBooking(BookingRequestDTO request) {
        // TODO: validations

        // create new booking using information provided from the event booking form
        // TODO: fetch logged in users information from spring to assign userId
        Long userId = (long) 111;

        List<BookingRequestDTO.TicketSelectionDTO> tickets = request.getTickets();
        BigDecimal totalPrice = tickets.stream()
                .map(ticket -> eventTicketConfigRepository.findById(ticket.getTicketConfigId())
                        .orElseThrow(() -> new EntityNotFoundException("Ticket config not found: " + ticket.getTicketConfigId()))
                        .getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        EventDto evenDetails = eventService.getEventDetailsByEventId(request.getEventId()).orElseThrow(
                () -> new IllegalArgumentException("Object must be not null")
        ).getEvent();

        BookingDto newBooking = BookingDto.builder()
                .eventId(evenDetails.getId())
                .userId(userId)
                .totalPrice(totalPrice)
                .discountCode(request.getDiscountCode())
                .build();

        BookingDto bookingDetails = bookingService.createNewBooking(newBooking);

        // temp. assign selected seatsList<BookingRequestDTO.TicketSelectionDTO> tickets = request.getTickets();
        for (var ticket : tickets) {
            var result = seatReservationServiceImpl.assignSeatsTemp(ticket, bookingDetails.getId());
            if (!result.getValue0())
                log.error("Failed to assign seat {} for ticket config id {}", ticket.getSeat(), ticket.getTicketConfigId());
        }

        // TODO: 5. handle payment
        // TODO on success -> status changes to CONFIRMED
        // TODO on fail -> status changes to FAILED or CANCELED
        String transactionId = "1558df5-8c9b-4e7a-9c3a-2b1e5f6a7b8c";

        var paymentDto = paymentService.create(new PaymentDto(
                null,
                bookingDetails.getId(),
                totalPrice,
                StatusPaymentEnum.PENDING,
                request.getPaymentMethod().toString(),
                transactionId,
                "OK",
                LocalDateTime.now(),
                null
        ));

        if (paymentDto.getStatus().equals(StatusPaymentEnum.PAID)) {
            //Thread.currentThread().wait(1000);

            log.info("Seat: {}", tickets.getFirst().getSeat());

            // TODO: 6. generate tickets
            for (var ticket : tickets) {
                CreateTicketDTO newTicket = new CreateTicketDTO(
                        TicketType.VIP,
                        evenDetails.getTitle(),
                        ticket.getSeat(),
                        request.getHolderName(),
                        request.getHolderEmail(),
                        request.getDiscountCode()
                );
                ticketService.create(newTicket);
            }

            // TODO: 7. generate invoice
            var invoice = invoiceService.generateInvoiceFromForm(new InvoiceFormDto(
                    bookingDetails.getId(),
                    totalPrice,
                    request.getDiscountCode(),
                    paymentDto.getId(),
                    request.getHolderName(),
                    request.getHolderEmail())); // it also generates invoice

            bookingDetails.setStatus(StatusBookingEnum.DONE);
            bookingService.update(bookingDetails.getId(), bookingDetails);
        } else {
            // Show error message to user and redirect to booking page
            log.error("Payment failed for booking id: {}, payment id: {}", bookingDetails.getId(), paymentDto.getId());
            bookingDetails.setStatus(StatusBookingEnum.CANCELLED);
            bookingService.update(bookingDetails.getId(), bookingDetails);
        }
    }

}
