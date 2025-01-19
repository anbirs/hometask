package com.example.hometask.unit.service.mapper;

import com.example.hometask.data.Ticket;
import com.example.hometask.repository.TicketRepository;
import com.example.hometask.repository.entity.*;
import com.example.hometask.service.mapper.ShowtimeMapper;
import com.example.hometask.service.mapper.TicketMapper;
import com.example.hometask.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class TicketMapperTest {
    @InjectMocks
    private TicketMapper ticketMapper = Mappers.getMapper(TicketMapper.class);

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ShowtimeMapper showtimeMapper;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toDto() {
        var start = LocalDateTime.of(2025, 2, 15, 12, 0);
        var end = LocalDateTime.of(2025, 2, 15, 14, 0);
        ShowtimeEntity showtime = new ShowtimeEntity(2L, null, "cinema", 100, start, end);
        UserEntity user = new UserEntity(8L, "Username", "email", Role.ROLE_CUSTOMER, null);
        TicketEntity entity = new TicketEntity(1L, showtime, user, "20", BigDecimal.valueOf(7.5));

        Ticket expectedDto = new Ticket(1L, 8L, 2L, "20", BigDecimal.valueOf(7.5));

        Ticket dto = ticketMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(expectedDto, dto);
    }

    @Test
    void toEntity() {
        var start = LocalDateTime.of(2025, 2, 15, 12, 0);
        var end = LocalDateTime.of(2025, 2, 15, 14, 0);
        Ticket dto = new Ticket(1L, 8L, 1L, "20", BigDecimal.valueOf(7.5));

        when(showtimeMapper.idToEntity(1L))
                .thenReturn(new ShowtimeEntity(1L, null, "cinema", 100, start, end));
        when(userMapper.idToEntity(8L))
                .thenReturn(new UserEntity(8L, "Username", "email", Role.ROLE_CUSTOMER, null));

        TicketEntity expectedEntity = new TicketEntity(
                1L,
                new ShowtimeEntity(1L, null, "cinema", 100, start, end),
                new UserEntity(8L, "Username", "email", Role.ROLE_CUSTOMER, null),
                 "20",
                BigDecimal.valueOf(7.5));

        TicketEntity entity = ticketMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(expectedEntity, entity);
    }

    @Test
    void updateEntityFromDto() {
        var start = LocalDateTime.now();
        var end = LocalDateTime.now().plusHours(1);
        var startUpd = LocalDateTime.now().plusHours(1);
        var endUpd = LocalDateTime.now().plusHours(3);
        TicketEntity existingEntity = new TicketEntity( 1L,
                new ShowtimeEntity(1L, null, "cinema", 100, start, end),
                new UserEntity(8L, "Username", "email", Role.ROLE_CUSTOMER, null),
                "20",
                BigDecimal.valueOf(7.5));

        Ticket dto = new Ticket(null, 5L, 6L, "22",  BigDecimal.valueOf(6.99));

        TicketEntity expectedEntity = new TicketEntity(1L,
                new ShowtimeEntity(6L, null, "cinema1", 160, startUpd, endUpd),
                new UserEntity(5L, "AnotherUser", "email1", null, null),
                "22",
                BigDecimal.valueOf(6.99));

        when(showtimeMapper.idToEntity(6L))
                .thenReturn(new ShowtimeEntity(6L, null, "cinema1", 160, startUpd, endUpd));
        when(userMapper.idToEntity(5L))
                .thenReturn(new UserEntity(5L, "AnotherUser", "email1", null, null));

        ticketMapper.updateEntityFromDto(dto, existingEntity);

        assertEquals(expectedEntity, existingEntity);
    }
}