package com.example.hometask.unit.service;

import com.example.hometask.data.Ticket;
import com.example.hometask.repository.TicketRepository;
import com.example.hometask.repository.entity.TicketEntity;
import com.example.hometask.service.impl.TicketServiceImpl;
import com.example.hometask.service.mapper.TicketMapper;
import com.example.hometask.service.validator.TicketValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private TicketValidator ticketValidator;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void bookTicket() {
        Long ticketId = 1L;
        Ticket dto = new Ticket(ticketId, 1L, 1L, "1", BigDecimal.valueOf(10));
        TicketEntity ticketEntity = new TicketEntity(ticketId, null, null, "1", BigDecimal.valueOf(10));
        TicketEntity savedEntity = new TicketEntity(ticketId, null, null, "1", BigDecimal.valueOf(10));
        Ticket expectedDto = new Ticket(ticketId, 1L, 1L, "1", BigDecimal.valueOf(10));

        when(ticketMapper.toEntity(dto)).thenReturn(ticketEntity);
        when(ticketRepository.save(ticketEntity)).thenReturn(savedEntity);
        when(ticketMapper.toDto(savedEntity)).thenReturn(expectedDto);

        Ticket result = ticketService.bookTicket(dto);

        assertEquals(expectedDto, result);
        verify(ticketValidator, times(1)).validateTicketForShowtime(ticketEntity);
        verify(ticketRepository, times(1)).save(ticketEntity);
        verify(ticketMapper, times(1)).toDto(savedEntity);
    }

    @Test
    void bookTicket_ValidationFailed() {
        Long ticketId = 1L;
        Ticket dto = new Ticket(ticketId, 1L, 1L, "", BigDecimal.valueOf(10));
        TicketEntity ticketEntity = new TicketEntity(ticketId, null, null, "", BigDecimal.valueOf(10));

        when(ticketMapper.toEntity(dto)).thenReturn(ticketEntity);
        doThrow(new IllegalArgumentException("Validation Failed")).when(ticketValidator).validateTicketForShowtime(ticketEntity);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ticketService.bookTicket(dto));
        assertEquals("Validation Failed", exception.getMessage());
        verify(ticketValidator, times(1)).validateTicketForShowtime(ticketEntity);
        verifyNoInteractions(ticketRepository);
    }

    @Test
    void updateTicket() {
        Long ticketId = 1L;
        TicketEntity existingEntity = new TicketEntity(ticketId, null, null, "1", BigDecimal.valueOf(10));
        Ticket updatedDto = new Ticket(ticketId, 1L, 1L, "2", BigDecimal.valueOf(15));
        TicketEntity updatedEntity = new TicketEntity(ticketId, null, null, "2", BigDecimal.valueOf(15));
        Ticket expectedDto = new Ticket(ticketId, 1L, 1L, "2", BigDecimal.valueOf(15));

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingEntity));
        when(ticketRepository.save(existingEntity)).thenReturn(updatedEntity);
        when(ticketMapper.toDto(updatedEntity)).thenReturn(expectedDto);

        Ticket result = ticketService.updateTicket(ticketId, updatedDto);

        assertEquals(expectedDto, result);
        verify(ticketMapper, times(1)).updateEntityFromDto(updatedDto, existingEntity);
        verify(ticketRepository, times(1)).save(existingEntity);
        verify(ticketMapper, times(1)).toDto(updatedEntity);
    }

    @Test
    void findTicketById() {
        Long ticketId = 1L;
        TicketEntity entity = new TicketEntity(ticketId, null, null, "", BigDecimal.valueOf(10));
        Ticket expectedDto = new Ticket(ticketId, 1L, 1L, "", BigDecimal.valueOf(10));

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(entity));
        when(ticketMapper.toDto(entity)).thenReturn(expectedDto);

        Ticket result = ticketService.findTicketById(ticketId);

        assertEquals(expectedDto, result);
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketMapper, times(1)).toDto(entity);
    }

    @Test
    void findTicketById_NotFound() {
        Long ticketId = 1L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ticketService.findTicketById(ticketId));
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void deleteTicket() {
        Long ticketId = 1L;
        TicketEntity ticket = new TicketEntity(ticketId, null, null, null, null);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        Long result = ticketService.deleteTicket(ticketId);
        assertEquals(ticketId, result);
        verify(ticketRepository, times(1)).deleteById(ticketId);
    }

    @Test
    void deleteTicket_NotFound() {
        Long ticketId = 1L;
        when(ticketRepository.existsById(ticketId)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> ticketService.deleteTicket(ticketId));
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void getAllTickets() {
        List<TicketEntity> entities = List.of(
                new TicketEntity(1L, null, null, "1", BigDecimal.valueOf(10)),
                new TicketEntity(2L, null, null, "2", BigDecimal.valueOf(15))
        );
        List<Ticket> expectedDtos = List.of(
                new Ticket(1L, 1L, 1L, "1", BigDecimal.valueOf(10)),
                new Ticket(2L, 1L, 1L, "2", BigDecimal.valueOf(15))
        );

        when(ticketRepository.findAll()).thenReturn(entities);
        when(ticketMapper.toDto(any(TicketEntity.class))).thenAnswer(invocation -> {
            TicketEntity entity = invocation.getArgument(0);
            return expectedDtos.stream().filter(dto -> dto.getId().equals(entity.getId())).findFirst().orElse(null);
        });

        List<Ticket> result = ticketService.getAllTickets();

        assertEquals(2, result.size());
        assertEquals(expectedDtos, result);
        verify(ticketRepository, times(1)).findAll();
        verify(ticketMapper, times(2)).toDto(any(TicketEntity.class));
    }

    @Test
    void getAllTicketsByUser() {
        Long userId = 1L;
        List<TicketEntity> entities = List.of(
                new TicketEntity(1L, null, null, "1", BigDecimal.valueOf(10))
        );

        List<Ticket> expectedDtos = List.of(
                new Ticket(1L, userId, 1L, "1", BigDecimal.valueOf(10))
        );

        when(ticketRepository.findByUser_Id(userId)).thenReturn(entities);
        when(ticketMapper.toDto(any(TicketEntity.class))).thenAnswer(invocation -> {
            TicketEntity entity = invocation.getArgument(0);
            return expectedDtos.stream().filter(dto -> dto.getId().equals(entity.getId())).findFirst().orElse(null);
        });

        List<Ticket> result = ticketService.getAllTicketsByUser(userId);

        assertEquals(1, result.size());
        assertEquals(expectedDtos, result);
        verify(ticketRepository, times(1)).findByUser_Id(userId);
        verify(ticketMapper, times(1)).toDto(any(TicketEntity.class));
    }

    @Test
    void getAllTicketsByShowtime() {
        Long showtimeId = 1L;
        List<TicketEntity> entities = List.of(
                new TicketEntity(1L, null, null, "1", BigDecimal.valueOf(10)),
                new TicketEntity(2L, null, null, "1", BigDecimal.valueOf(10)),
                new TicketEntity(3L, null, null, "1", BigDecimal.valueOf(10))
        );

        List<Ticket> expectedDtos = List.of(
                new Ticket(1L, 1L, showtimeId, "1", BigDecimal.valueOf(10)),
                new Ticket(2L, 5L, showtimeId, "1", BigDecimal.valueOf(10)),
                new Ticket(3L, 8L, showtimeId, "1", BigDecimal.valueOf(10))
        );

        when(ticketRepository.findByShowtime_Id(showtimeId)).thenReturn(entities);
        when(ticketMapper.toDto(any(TicketEntity.class))).thenAnswer(invocation -> {
            TicketEntity entity = invocation.getArgument(0);
            return expectedDtos.stream().filter(dto -> dto.getId().equals(entity.getId())).findFirst().orElse(null);
        });

        List<Ticket> result = ticketService.getAllTicketsByShowtime(showtimeId);

        assertEquals(3, result.size());
        assertEquals(expectedDtos, result);
        verify(ticketRepository, times(1)).findByShowtime_Id(showtimeId);
        verify(ticketMapper, times(3)).toDto(any(TicketEntity.class));
    }
}