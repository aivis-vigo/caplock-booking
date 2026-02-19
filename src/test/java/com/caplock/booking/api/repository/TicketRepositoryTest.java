package com.caplock.booking.api.repository;

import com.caplock.booking.TicketType;
import com.caplock.booking.entity.Ticket;
import com.caplock.booking.repository.jpa.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void init() {
        Ticket ticket = Ticket.builder()
                .ticketType(TicketType.STANDARD)
                .event("Basic Show")
                .section("A")
                .row("1C")
                .seatNumber("4")
                .holderName("Walter White")
                .holderEmail("heisenberg@example.com")
                .currency("USD")
                .purchasedAt(LocalDateTime.now())
                .status("PENDING")
                .build();

        ticketRepository.save(ticket);
    }

    @Test
    public void TicketRepository_SaveAll_ReturnsSavedTickets() {
        List<Ticket> tickets = ticketRepository.findAll();

        assertThat(tickets).isNotNull();
        assertThat(tickets.getFirst().getId()).isGreaterThan(0L);
        assertThat(tickets.getFirst().getHolderName()).isEqualTo("Walter White");
    }

    @Test
    public void TicketRepository_findByHolderName_returnsHoldersTickets() {
        Ticket ticket2 = Ticket.builder()
                .ticketType(TicketType.VIP)
                .event("High-end Show")
                .section("C")
                .row("13B")
                .seatNumber("12")
                .holderName("Jesse Pinkman")
                .holderEmail("jessepinkman@example.com")
                .currency("USD")
                .purchasedAt(LocalDateTime.now())
                .status("PENDING")
                .build();

        ticketRepository.save(ticket2);

        List<Ticket> tickets = ticketRepository.findByHolderName(ticket2.getHolderName());

        assertThat(tickets).isNotNull();
        assertThat(tickets.getFirst().getHolderName()).isEqualTo("Jesse Pinkman");
    }

    @Test
    public void TicketRepository_noTicketsForHolder_returnsNoTickets() {
        List<Ticket> usersTickets = ticketRepository.findByHolderName("Jesse Pinkman");

        assertThat(usersTickets).isEmpty();
    }

    @Test
    public void TicketRepository_updateTicketInformation_ticketUpdatedSuccessfully() {
        Ticket ticket2 = Ticket.builder()
                .ticketType(TicketType.STANDARD)
                .event("Basic Show")
                .section("A")
                .row("1C")
                .seatNumber("4")
                .holderName("Hank Schrader")
                .holderEmail("hankschrader@dea.gov")
                .currency("USD")
                .purchasedAt(LocalDateTime.now())
                .status("PENDING")
                .build();

        ticketRepository.save(ticket2);

        ticket2.setHolderName("Jesse Pinkman");
        ticket2.setHolderEmail("jessepinkman@example.com");

        Ticket updatedTicket = ticketRepository.save(ticket2);

        assertThat(updatedTicket).isNotNull();
        assertThat(updatedTicket.getHolderName()).isEqualTo("Jesse Pinkman");
        assertThat(updatedTicket.getHolderEmail()).isEqualTo("jessepinkman@example.com");
    }

    @Test
    public void TicketRepository_deleteTicket_ticketDeletedSuccessfully() {
        Ticket ticket2 = Ticket.builder()
                .ticketType(TicketType.STANDARD)
                .event("Basic Show")
                .section("A")
                .row("1C")
                .seatNumber("4")
                .holderName("Hank Schrader")
                .holderEmail("hankschrader@dea.gov")
                .currency("USD")
                .purchasedAt(LocalDateTime.now())
                .status("PENDING")
                .build();

        ticketRepository.save(ticket2);

        assertThat(ticketRepository.findAll().size()).isEqualTo(2);

        ticketRepository.deleteById(ticket2.getId());
        ticketRepository.deleteById(ticketRepository.findAll().getFirst().getId());

        assertThat(ticketRepository.findAll().size()).isEqualTo(0);
    }

}