package com.caplock.booking.config;

import com.caplock.booking.controller.AuthController;
import com.caplock.booking.entity.StatusBookingEnum;
import com.caplock.booking.entity.StatusEventEnum;
import com.caplock.booking.entity.StatusPaymentEnum;
import com.caplock.booking.entity.StatusTicketEnum;
import com.caplock.booking.entity.TicketType;
import com.caplock.booking.entity.UserRole;
import com.caplock.booking.entity.dao.BookingEntity;
import com.caplock.booking.entity.dao.BookingItemEntity;
import com.caplock.booking.entity.dao.EventEntity;
import com.caplock.booking.entity.dao.EventTicketConfigEntity;
import com.caplock.booking.entity.dao.InvoiceEntity;
import com.caplock.booking.entity.dao.PaymentEntity;
import com.caplock.booking.entity.dao.TicketEntity;
import com.caplock.booking.entity.dao.UserEntity;
import com.caplock.booking.entity.dto.EventDto;
import com.caplock.booking.entity.dto.EventTicketConfigDto;
import com.caplock.booking.repository.BookingItemRepository;
import com.caplock.booking.repository.BookingRepository;
import com.caplock.booking.repository.EventRepository;
import com.caplock.booking.repository.EventTicketConfigRepository;
import com.caplock.booking.repository.IUserRepository;
import com.caplock.booking.repository.InvoiceRepository;
import com.caplock.booking.repository.PaymentRepository;
import com.caplock.booking.repository.TicketRepository;
import com.caplock.booking.service.SeatReservationService;
import com.caplock.booking.service.impl.SeatReservationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final IUserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventTicketConfigRepository eventTicketConfigRepository;
    private final BookingRepository bookingRepository;
    private final BookingItemRepository bookingItemRepository;
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final SeatReservationService seatReservationService;
    private final AuthController authController;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0 || eventRepository.count() > 0) {
            log.info("DataSeeder skipped: existing data found.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();


        authController.signup("admin@example.com", "admin123", "pB:r6z,r)1E*{8-8:$(@[%V!g5r<M", "Admin User");
        authController.signup("jane.doe@example.com", "password123", "", "Jane Doe");

/*        UserEntity admin = new UserEntity();
        admin.setName("Admin User");
        admin.setEmailHash("admin@example.com");
        admin.setPasswordHash("admin123");
        admin.setRole(UserRole.ADMIN);
        admin.setCreatedAt(now.minusDays(30));
        admin.setNotificationToken("mock-token-admin");

        UserEntity user = new UserEntity();
        user.setName("Jane Doe");
        user.setEmailHash("jane.doe@example.com");
        user.setPasswordHash("password123");
        user.setRole(UserRole.USER);
        user.setCreatedAt(now.minusDays(5));
        user.setNotificationToken("mock-token-user");*/

        var admin = userRepository.findByEmailHash("admin@example.com").get();
        var user =  userRepository.findByEmailHash("jane.doe@example.com").get();

        EventEntity event1 = new EventEntity();
        event1.setTitle("Caplock Live");
        event1.setDescription("An evening of live talks and performances.");
        event1.setLocation("City Hall");
        event1.setStartTime(now.plusDays(7));
        event1.setEndTime(now.plusDays(7).plusHours(3));
        event1.setBookingOpenAt(now.minusDays(10));
        event1.setBookingDeadline(now.plusDays(5));
        event1.setCreatedAt(now.minusDays(12));
        event1.setCreatedBy(admin.getId());
        event1.setStatus(StatusEventEnum.Soon);
        event1.setCategory("Music");

        EventEntity event2 = new EventEntity();
        event2.setTitle("Tech Workshop");
        event2.setDescription("Hands-on workshop for beginners.");
        event2.setLocation("Innovation Hub");
        event2.setStartTime(now.plusDays(14));
        event2.setEndTime(now.plusDays(14).plusHours(4));
        event2.setBookingOpenAt(now.minusDays(3));
        event2.setBookingDeadline(now.plusDays(12));
        event2.setCreatedAt(now.minusDays(3));
        event2.setCreatedBy(admin.getId());
        event2.setStatus(StatusEventEnum.New);
        event2.setCategory("Education");


        eventRepository.saveAll(List.of(event1, event2));

        EventTicketConfigEntity config1 = new EventTicketConfigEntity();
        config1.setEventId(event1.getId());
        config1.setTicketType(TicketType.VIP);
        config1.setPrice(new BigDecimal("120.00"));
        config1.setNumOfSections(5);
        config1.setNumOfRows(10);
        config1.setNumSeatsPerRow(2);
        config1.setTotalSeats(50L);
        config1.setAvailableSeats(48L);
        config1.setSaleStart(now.minusDays(7));
        config1.setSaleEnd(now.plusDays(5));

        EventTicketConfigEntity config2 = new EventTicketConfigEntity();
        config2.setEventId(event1.getId());
        config2.setTicketType(TicketType.STANDARD);
        config2.setPrice(new BigDecimal("60.00"));
        config2.setNumOfSections(5);
        config2.setNumOfRows(10);
        config2.setNumSeatsPerRow(2);
        config2.setTotalSeats(200L);
        config2.setAvailableSeats(195L);
        config2.setSaleStart(now.minusDays(7));
        config2.setSaleEnd(now.plusDays(5));

        EventTicketConfigEntity config3 = new EventTicketConfigEntity();
        config3.setEventId(event2.getId());
        config3.setTicketType(TicketType.STUDENT);
        config3.setPrice(new BigDecimal("30.00"));
        config3.setNumOfSections(5);
        config3.setNumOfRows(10);
        config3.setNumSeatsPerRow(2);
        config3.setTotalSeats(100L);
        config3.setAvailableSeats(0L);
        config3.setSaleStart(now.minusDays(2));
        config3.setSaleEnd(now.plusDays(12));


        SeatReservationServiceImpl.populateSeatsForEvent(List.of(modelMapper.map(config1, EventTicketConfigDto.class), modelMapper.map(config2, EventTicketConfigDto.class)));
        SeatReservationServiceImpl.populateSeatsForEvent(List.of(modelMapper.map(config3, EventTicketConfigDto.class)));

        eventTicketConfigRepository.saveAll(List.of(config1, config2, config3));

        BookingEntity booking = new BookingEntity();
        booking.setConfirmationCode("CNF-" + UUID.randomUUID().toString().substring(0, 8));
        booking.setEventId(event1.getId());
        booking.setUserId(user.getId());
        booking.setTotalPrice(new BigDecimal("120.00"));
        booking.setDiscountCode("WELCOME10");
        booking.setStatus(StatusBookingEnum.DONE);
        booking.setCreatedAt(now.minusDays(1));
        booking.setUpdatedAt(now.minusDays(1));

        bookingRepository.save(booking);

        BookingItemEntity item = new BookingItemEntity();
        item.setBookingId(booking.getId());
        item.setEventTicketConfigId(config1.getId());
        item.setTicketType(TicketType.VIP);
        item.setQuantity(2);
        item.setPricePerSeat(new BigDecimal("60.00"));
        item.setSubtotal(new BigDecimal("120.00"));
        List<String> selectedSeats = new ArrayList<>(List.of("A0000", "A0001"));
        item.setSelectedSeats(selectedSeats);

        bookingItemRepository.save(item);

        seatReservationService.assignSeatsTemp(
                event1.getId(),
                List.of(
                        Pair.with(selectedSeats.getFirst(), TicketType.VIP),
                        Pair.with(selectedSeats.getLast(), TicketType.VIP)
                ),
                booking.getId());

        PaymentEntity payment = new PaymentEntity();
        payment.setBookingId(booking.getId());
        payment.setAmount(new BigDecimal("120.00"));
        payment.setStatus(StatusPaymentEnum.PAID);
        payment.setMethod("CARD");
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8));
        payment.setProviderResponse("APPROVED");
        payment.setCreatedAt(now.minusDays(1));
        payment.setPaidAt(now.minusDays(1));

        paymentRepository.save(payment);

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setBookingId(booking.getId());
        invoice.setPaymentId(payment.getId());
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8));
        invoice.setHolderName(user.getName());
        invoice.setHolderEmail(user.getEmailHash());
        invoice.setSubtotal(new BigDecimal("120.00"));
        invoice.setDiscount(new BigDecimal("0.00"));
        invoice.setTotalAmount(new BigDecimal("120.00"));
        invoice.setIssuedAt(now.minusDays(1));
        invoice.setCreatedAt(now.minusDays(1));
        invoice.setPdfUrl("invoices/" + invoice.getInvoiceNumber() + ".txt");

        invoiceRepository.save(invoice);

        TicketEntity ticket1 = new TicketEntity();
        ticket1.setBookingId(booking.getId());
        ticket1.setBookingItemId(item.getId());
        ticket1.setEventId(event1.getId());
        ticket1.setTicketCode(null);
        ticket1.setSeat("A");
        ticket1.setHolderName(user.getName());
        ticket1.setHolderEmail(user.getEmailHash());
        ticket1.setDiscountCode("WELCOME10");
        ticket1.setStatus(StatusTicketEnum.Issued);
        ticket1.setIssuedAt(now.minusDays(1));
        ticket1.setQrCodePath("uploads/qr/" + UUID.randomUUID() + ".png");

        TicketEntity ticket2 = new TicketEntity();
        ticket2.setBookingId(booking.getId());
        ticket2.setBookingItemId(item.getId());
        ticket2.setEventId(event1.getId());
        ticket2.setTicketCode(null);
        ticket1.setSeat("A");
        ticket2.setHolderName(user.getName());
        ticket2.setHolderEmail(user.getEmailHash());
        ticket2.setDiscountCode("WELCOME10");
        ticket2.setStatus(StatusTicketEnum.Issued);
        ticket2.setIssuedAt(now.minusDays(1));
        ticket2.setQrCodePath("uploads/qr/" + UUID.randomUUID() + ".png");

        ticketRepository.saveAll(List.of(ticket1, ticket2));

        log.info("DataSeeder inserted mock data.");
    }
}
