package com.caplock.booking.api.repository;

import com.caplock.booking.entity.TicketType;
import com.caplock.booking.entity.dao.TicketEntity;
import com.caplock.booking.repository.TicketRepository;
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
        TicketEntity ticket = new TicketEntity();
        ticket.setTicketType(TicketType.STANDARD);
        ticket.setTicketCode("Basic Show");
        ticket.setSection("A");
        ticket.setRow("1C");
        ticket.setSeatNumber("4");
        ticket.setHolderName("Walter White");
        ticket.setHolderEmail("heisenberg@example.com");
        ticket.setIssuedAt(LocalDateTime.now());

        ticketRepository.save(ticket);
    }

    @Test
    public void TicketRepository_SaveAll_ReturnsSavedTickets() {
        List<TicketEntity> tickets = ticketRepository.findAll();

        assertThat(tickets).isNotNull();
        assertThat(tickets.getFirst().getId()).isGreaterThan(0L);
        assertThat(tickets.getFirst().getHolderName()).isEqualTo("Walter White");
    }

    @Test
    public void TicketRepository_findByHolderName_returnsHoldersTickets() {
        TicketEntity ticket2 = new TicketEntity();
        ticket2.setTicketType(TicketType.VIP);
        ticket2.setTicketCode("High-end Show");
        ticket2.setSection("C");
        ticket2.setRow("13B");
        ticket2.setSeatNumber("12");
        ticket2.setHolderName("Jesse Pinkman");
        ticket2.setHolderEmail("jessepinkman@example.com");
        ticket2.setIssuedAt(LocalDateTime.now());

        ticketRepository.save(ticket2);

        List<TicketEntity> tickets = ticketRepository.findByHolderName(ticket2.getHolderName());

        assertThat(tickets).isNotNull();
        assertThat(tickets.getFirst().getHolderName()).isEqualTo("Jesse Pinkman");
    }

    @Test
    public void TicketRepository_noTicketsForHolder_returnsNoTickets() {
        List<TicketEntity> usersTickets = ticketRepository.findByHolderName("Jesse Pinkman");

        assertThat(usersTickets).isEmpty();
    }

    @Test
    public void TicketRepository_updateTicketInformation_ticketUpdatedSuccessfully() {
        TicketEntity ticket2 = new TicketEntity();
        ticket2.setTicketType(TicketType.STANDARD);
        ticket2.setTicketCode("Basic Show");
        ticket2.setSection("A");
        ticket2.setRow("1C");
        ticket2.setSeatNumber("4");
        ticket2.setHolderName("Hank Schrader");
        ticket2.setHolderEmail("hankschrader@dea.gov");
        ticket2.setIssuedAt(LocalDateTime.now());

        ticketRepository.save(ticket2);

        ticket2.setHolderName("Jesse Pinkman");
        ticket2.setHolderEmail("jessepinkman@example.com");

        TicketEntity updatedTicket = ticketRepository.save(ticket2);

        assertThat(updatedTicket).isNotNull();
        assertThat(updatedTicket.getHolderName()).isEqualTo("Jesse Pinkman");
        assertThat(updatedTicket.getHolderEmail()).isEqualTo("jessepinkman@example.com");
    }

    @Test
    public void TicketRepository_deleteTicket_ticketDeletedSuccessfully() {
        TicketEntity ticket2 = new TicketEntity();
        ticket2.setTicketType(TicketType.STANDARD);
        ticket2.setTicketCode("Basic Show");
        ticket2.setSection("A");
        ticket2.setRow("1C");
        ticket2.setSeatNumber("4");
        ticket2.setHolderName("Hank Schrader");
        ticket2.setHolderEmail("hankschrader@dea.gov");
        ticket2.setIssuedAt(LocalDateTime.now());

        ticketRepository.save(ticket2);

        assertThat(ticketRepository.findAll().size()).isEqualTo(2);

        ticketRepository.deleteById(ticket2.getId());
        ticketRepository.deleteById(ticketRepository.findAll().getFirst().getId());

        assertThat(ticketRepository.findAll().size()).isEqualTo(0);
    }

}
