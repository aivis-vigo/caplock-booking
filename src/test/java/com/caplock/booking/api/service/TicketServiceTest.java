package com.caplock.booking.api.service;

import com.caplock.booking.entity.dao.TicketEntity;
import com.caplock.booking.entity.dto.CreateTicketDTO;
import com.caplock.booking.entity.dto.TicketDto;
import com.caplock.booking.repository.TicketRepository;
import com.caplock.booking.service.impl.TicketServiceImpl;
import com.caplock.booking.service.impl.QrService;
import com.google.zxing.WriterException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private QrService qrService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    public void TicketService_findAll_returnsTickets() {
        TicketEntity ticket = new TicketEntity();
        ticket.setId(1L);

        when(ticketRepository.findAll()).thenReturn(List.of(ticket));

        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(1L);

        when(modelMapper.map(ticket, TicketDto.class)).thenReturn(ticketDto);

        List<TicketDto> response = ticketService.findAll();

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().getId()).isEqualTo(1L);
    }

    @Test
    public void TicketService_findByHolderName() {
        TicketEntity ticket = new TicketEntity();
        ticket.setHolderName("John Doe");

        TicketDto ticketDto = new TicketDto();
        ticketDto.setHolderName("John Doe");

        when(ticketRepository.findByHolderName("John Doe")).thenReturn(List.of(ticket));
        when(modelMapper.map(ticket, TicketDto.class)).thenReturn(ticketDto);

        List<TicketDto> response = ticketService.findByHolderName("John Doe");

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().getHolderName()).isEqualTo("John Doe");
    }

    @Test
    public void TicketService_create_returnsCreatedTicket() throws IOException, WriterException {
        CreateTicketDTO createTicketDTO = new CreateTicketDTO();
        createTicketDTO.setHolderName("John Doe");
        createTicketDTO.setHolderEmail("john@example.com");

        TicketEntity savedTicket = new TicketEntity();
        savedTicket.setId(1L);
        savedTicket.setHolderName("John Doe");
        savedTicket.setHolderEmail("john@example.com");

        TicketDto mappedDto = new TicketDto();
        mappedDto.setId(1L);
        mappedDto.setHolderName("John Doe");

        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(savedTicket);
        when(qrService.generateAndSave(any(String.class), any(String.class))).thenReturn("/tmp/qr.png");
        when(modelMapper.map(savedTicket, TicketDto.class)).thenReturn(mappedDto);

        TicketDto response = ticketService.create(createTicketDTO);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getHolderName()).isEqualTo("John Doe");

        verify(ticketRepository).save(any(TicketEntity.class));
    }

    @Test
    public void TicketService_update_returnsSuccess() {
        TicketDto updatedTicket = new TicketDto();
        updatedTicket.setHolderName("Jane Doe");

        TicketEntity savedTicket = new TicketEntity();
        savedTicket.setId(1L);
        savedTicket.setHolderName("Jane Doe");

        TicketEntity mappedEntity = new TicketEntity();
        mappedEntity.setId(1L);
        mappedEntity.setHolderName("Jane Doe");

        TicketDto mappedDto = new TicketDto();
        mappedDto.setId(1L);
        mappedDto.setHolderName("Jane Doe");

        when(modelMapper.map(updatedTicket, TicketEntity.class)).thenReturn(mappedEntity);
        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(savedTicket);
        when(modelMapper.map(savedTicket, TicketDto.class)).thenReturn(mappedDto);

        TicketDto response = ticketService.update(1L, updatedTicket);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getHolderName()).isEqualTo("Jane Doe");
        verify(ticketRepository).save(any(TicketEntity.class));
    }

    @Test
    public void TicketService_deleteById_returnsSuccess() {
        ticketService.deleteById(1L);

        verify(ticketRepository).deleteById(1L);
    }
}
