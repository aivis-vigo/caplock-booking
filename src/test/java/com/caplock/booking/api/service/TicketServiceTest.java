package com.caplock.booking.api.service;

import com.caplock.booking.config.ModelMapperConfig;
import com.caplock.booking.entity.dao.TicketEntity;
import com.caplock.booking.entity.dto.CreateTicketDTO;
import com.caplock.booking.entity.dto.TicketDto;
import com.caplock.booking.repository.TicketRepository;
import com.caplock.booking.service.impl.QrService;
import com.caplock.booking.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @Mock
    private ModelMapperConfig mapperConfig;

    @Mock
    private Configuration mapperConfiguration;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    public void ticket_service_findAll_returnsTickets() {
        TicketEntity ticket = new TicketEntity();
        ticket.setId(1L);

        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(1L);

        when(ticketRepository.findAll()).thenReturn(List.of(ticket));
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
    public void TicketService_findByBookingId_returnsTickets() {
        TicketEntity ticket = new TicketEntity();
        ticket.setId(1L);
        ticket.setBookingId(3L);

        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(1L);
        ticketDto.setBookingId(3L);

        when(ticketRepository.findByBookingId(3L)).thenReturn(List.of(ticket));
        when(mapperConfig.modelMapper()).thenReturn(modelMapper);
        when(modelMapper.map(ticket, TicketDto.class)).thenReturn(ticketDto);

        List<TicketDto> response = ticketService.findByBookingId(3L);

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().getBookingId()).isEqualTo(3L);
    }

    @Test
    public void TicketService_create_returnsCreatedTicket() throws Exception {
        CreateTicketDTO createTicketDTO = new CreateTicketDTO();
        createTicketDTO.setHolderName("John Doe");
        createTicketDTO.setHolderEmail("john@example.com");

        TicketEntity mappedEntity = new TicketEntity();
        mappedEntity.setHolderName("John Doe");
        mappedEntity.setTicketNumber("test-ticket-number");

        TicketEntity savedTicket = new TicketEntity();
        savedTicket.setId(1L);
        savedTicket.setHolderName("John Doe");
        savedTicket.setTicketNumber("test-ticket-number");

        TicketDto mappedDto = new TicketDto();
        mappedDto.setId(1L);
        mappedDto.setHolderName("John Doe");

        when(modelMapper.getConfiguration()).thenReturn(mapperConfiguration);
        when(mapperConfiguration.setMatchingStrategy(any())).thenReturn(mapperConfiguration);
        when(mapperConfig.modelMapper()).thenReturn(modelMapper);
        when(modelMapper.map(createTicketDTO, TicketEntity.class)).thenReturn(mappedEntity);
        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(savedTicket);
        when(qrService.generateAndSave(any(), any())).thenReturn("/uploads/qr-codes/test-ticket-number.png");
        when(modelMapper.map(savedTicket, TicketDto.class)).thenReturn(mappedDto);

        TicketDto response = ticketService.create(createTicketDTO);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getHolderName()).isEqualTo("John Doe");
        verify(ticketRepository, org.mockito.Mockito.times(2)).save(any(TicketEntity.class));
    }

    @Test
    public void TicketService_update_returnsSuccess() {
        TicketDto updatedTicket = new TicketDto();
        updatedTicket.setHolderName("Jane Doe");

        TicketEntity existingTicket = new TicketEntity();
        existingTicket.setId(1L);

        TicketEntity mappedEntity = new TicketEntity();
        mappedEntity.setId(1L);
        mappedEntity.setHolderName("Jane Doe");

        TicketEntity savedTicket = new TicketEntity();
        savedTicket.setId(1L);
        savedTicket.setHolderName("Jane Doe");

        TicketDto mappedDto = new TicketDto();
        mappedDto.setId(1L);
        mappedDto.setHolderName("Jane Doe");

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(existingTicket));
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
        when(ticketRepository.existsById(1L)).thenReturn(true);

        ticketService.deleteById(1L);

        verify(ticketRepository).deleteById(1L);
    }
}