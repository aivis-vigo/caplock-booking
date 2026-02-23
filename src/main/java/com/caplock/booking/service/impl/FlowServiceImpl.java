package com.caplock.booking.service.impl;

import com.caplock.booking.entity.StatusBookingEnum;
import com.caplock.booking.entity.TicketType;
import com.caplock.booking.entity.dto.*;
import com.caplock.booking.event.PaymentSucceededEvent;
import com.caplock.booking.service.*;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FlowServiceImpl implements FlowService {
    private final UserService userService;
    private final EventService eventService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;
    private final TicketService ticketService;
    private final BookingService bookingService;
    private final ObjectProvider<SeatReservationService> seatReservationServiceProvider;

    @Override
    public Triplet<Boolean, String, Object> processBookingFlow(Long userId,
                                                               EventDetailsDto eventDetails,
                                                               BookingDto bookingDto,
                                                               List<BookingItemDto> bookingItemDtos,
                                                               PaymentDto paymentDto) {
        // 1. Validate input data
        // 2. Check seat availability
        // 3. Create booking and booking items
        // 4. Process payment
        // 5. Update booking status based on payment result
        // 6. Return result

        // -----------Input validation logic-----------
        if (bookingDto.getUserId() == null || eventDetails.getEvent().getId() == null
                || eventDetails.getTicketConfig().isEmpty() || paymentDto.getAmount().compareTo(BigDecimal.ZERO) <= 0
                || bookingItemDtos.stream().anyMatch(x -> x.getTicketType() == null)
                || bookingItemDtos.stream().anyMatch(x -> x.getBookingId() == null)
                || bookingItemDtos.stream().anyMatch(x -> x.getSelectedSeats() == null || x.getSelectedSeats().isEmpty())
                || !Objects.equals(bookingDto.getUserId(), userId) || bookingItemDtos.isEmpty()) {
            return Triplet.with(false, "Invalid input data", null);
        }

        // -----------Booking logic-----------
        bookingDto.setStatus(StatusBookingEnum.PROCESSING);

        var bookResult = bookingService.create(bookingDto, bookingItemDtos);
        bookingDto = bookResult.getValue0().orElseThrow();
        if (bookResult.getValue1() == false) {
            return Triplet.with(false, "Booking creation failed: " + bookResult.getValue2(), null);
        }

        // -----------Seat reservation logic-----------
        List<Pair<String, TicketType>> selectedSeats = new ArrayList<>();
        for (BookingItemDto bookingItemDto : bookingItemDtos) {
            for (var seat : bookingItemDto.getSelectedSeats())
                selectedSeats.add(Pair.with(seat, bookingItemDto.getTicketType()));
        }
        var result = seatReservationServiceProvider.getObject().assignSeatsTemp(eventDetails.getEvent().getId(), selectedSeats, bookingDto.getId());
        if (result.getValue0() == false) return Triplet.with(result.getValue0(), result.getValue1(), null);

        bookingDto.setStatus(StatusBookingEnum.WAITING_PAYMENT);
        bookingService.update(bookingDto.getId(), bookingDto);

        //------------Payment processing logic-----------
        if (paymentService.create(paymentDto) == null) {
            bookingDto.setStatus(StatusBookingEnum.PAYMENT_FAILED);
            bookingService.update(bookingDto.getId(), bookingDto);
            seatReservationServiceProvider
                    .getObject().clearReservationOfSeats(eventDetails.getEvent().getId(), bookingDto.getId()); // not implemented yet, but should be implemented in future
            return Triplet.with(false, "Payment processing failed", null);
        }

        // -----------Invoice generation logic-----------
        var uid = invoiceService.getNewInvoiceNumber();
        var user = userService.getById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        var invoiceDto = new InvoiceDto(null, bookingDto.getId(),
                paymentDto.getId(), uid, user.getName(),
                user.getEmailHash(), BigDecimal.ZERO, BigDecimal.ZERO,
                paymentDto.getAmount(), LocalDateTime.now(), null);
        var invoice = invoiceService.create(invoiceDto);
        if (invoice == null) {
            return Triplet.with(false, "Invoice creation failed", null);
        }
        invoiceService.generateInvoice(invoice.getId());


        for (var seat : selectedSeats)
            ticketService.create(new CreateTicketDTO(
                    seat.getValue1(),
                    eventDetails.getEvent().toString(),  // TODO: redo, depending on how is meant to be.
                    seat.getValue0().substring(0, 1),
                    seat.getValue0().substring(1, 3),
                    seat.getValue0().substring(3, 5),
                    user.getName(),
                    user.getEmailHash(),
                    bookingDto.getDiscountCode()));


        bookingDto.setStatus(StatusBookingEnum.DONE);
        bookingService.update(bookingDto.getId(), bookingDto);

        return Triplet.with(true, "Booking processed successfully", null); // TODO: return actual data instead of null
    }
}
