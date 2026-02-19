package com.caplock.booking.api.service;

import com.caplock.booking.dto.CreateTicketDTO;
import com.caplock.booking.dto.Response;
import com.caplock.booking.dto.TicketDTO;
import com.caplock.booking.entity.Ticket;
import com.caplock.booking.repository.jpa.TicketRepository;
import com.caplock.booking.service.QrService;
import com.caplock.booking.service.TicketServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TicketServiceImplementation ticketService;

    @Mock
    private QrService qrService;

    @Test
    public void TicketService_findAll_returnsTickets() {
        Ticket ticket = new Ticket();
        TicketDTO ticketDTO = new TicketDTO();

        when(ticketRepository.findAll()).thenReturn(List.of(ticket));
        when(modelMapper.map(ticket, TicketDTO.class)).thenReturn(ticketDTO);

        Response<List<TicketDTO>> response = ticketService.findAll();

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getData()).hasSize(1);
        assertThat(response.getData()).contains(ticketDTO);
    }

    @Test
    public void TicketService_findByHolderName() {
        Ticket ticket = new Ticket();
        TicketDTO ticketDTO = new TicketDTO();

        when(ticketRepository.findByHolderName("John Doe")).thenReturn(List.of(ticket));
        when(modelMapper.map(ticket, TicketDTO.class)).thenReturn(ticketDTO);

        Response<List<TicketDTO>> response = ticketService.findByHolderName("John Doe");

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).hasSize(1);
        assertThat(response.getData()).contains(ticketDTO);
    }

    @Test
    public void TicketService_create_returnsCreatedTicket() throws Exception {
        CreateTicketDTO createTicketDTO = new CreateTicketDTO();
        createTicketDTO.setHolderName("John Doe");
        createTicketDTO.setHolderEmail("john@example.com");

        Ticket savedTicket = new Ticket();
        savedTicket.setId(1L);
        savedTicket.setTicketNumber("TKT-001");

        TicketDTO ticketDTO = new TicketDTO();

        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);
        when(qrService.generateAndSave("TKT-001", "TKT-001")).thenReturn("/qr/TKT-001.png");
        when(modelMapper.map(savedTicket, TicketDTO.class)).thenReturn(ticketDTO);

        Response<TicketDTO> response = ticketService.create(createTicketDTO);

        assertThat(response.getStatusCode()).isEqualTo(201);
        assertThat(response.getMessage()).isEqualTo("Ticket created successfully");
        assertThat(response.getData()).isEqualTo(ticketDTO);

        // verify save was called twice — once before QR, once after setting QR path
        verify(ticketRepository, times(2)).save(any(Ticket.class));
        verify(qrService).generateAndSave("TKT-001", "TKT-001");
    }

    @Test
    public void TicketService_update_returnsSuccess() {
        Ticket existingTicket = new Ticket();
        existingTicket.setId(1L);

        Ticket updatedTicket = new Ticket();

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(existingTicket));
        when(ticketRepository.save(updatedTicket)).thenReturn(updatedTicket);

        Response<?> response = ticketService.update(1L, updatedTicket);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Ticket updated successfully");
        assertThat(response.getData()).isNull();

        assertThat(updatedTicket.getId()).isEqualTo(1L);
        verify(ticketRepository).save(updatedTicket);
    }

    @Test
    public void TicketService_deleteById_returnsSuccess() {
        when(ticketRepository.existsById(1L)).thenReturn(true);

        Response<?> response = ticketService.deleteById(1L);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Ticket deleted successfully");
        assertThat(response.getData()).isNull();

        verify(ticketRepository).deleteById(1L);
    }

}
