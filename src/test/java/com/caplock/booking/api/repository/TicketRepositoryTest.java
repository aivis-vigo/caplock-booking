package com.caplock.booking.api.repository;

import com.caplock.booking.TicketType;
import com.caplock.booking.entity.Ticket;
import com.caplock.booking.repository.jpa.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void TicketRepository_SaveAll_ReturnsSavedTickets() {
        Ticket ticket = Ticket.builder()
                .ticketType(TicketType.STANDARD)
                .event("Basic Show")
                .section("A")
                .row("1C")
                .seatNumber("4")
                .holderName("John Doe")
                .holderEmail("johndoe@example.com")
                .currency("USD")
                .purchasedAt(LocalDateTime.now())
                .status("PENDING")
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        assertThat(savedTicket).isNotNull();
        assertThat(savedTicket.getId()).isGreaterThan(0L);
        assertThat(savedTicket.getTicketNumber()).isNotNull();
        assertThat(savedTicket.getHolderName()).isEqualTo("John Doe");
    }

}
