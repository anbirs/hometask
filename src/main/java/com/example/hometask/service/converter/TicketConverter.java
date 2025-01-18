package com.example.hometask.service.converter;

import com.example.hometask.data.Ticket;
import com.example.hometask.repository.entity.TicketEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketConverter implements Converter <Ticket, TicketEntity>{
    @Autowired
    private ShowtimeConverter showtimeConverter;
    @Autowired
    private UserConverter userConverter;

    @Override
    public Ticket toDto(TicketEntity entity) {
        return new Ticket(
                entity.getId(),
                entity.getUser().getId(),
                entity.getShowtime().getId(),
                entity.getSeatNumber(),
                entity.getPrice()
                );
    }

    @Override
    public TicketEntity toEntity(Ticket dto) {
        return new TicketEntity(
                dto.getId(),
                showtimeConverter.toEntity(dto.getShowtime()),
                userConverter.toEntity(dto.getUser()),
                dto.getSeatNumber(),
                dto.getPrice());
    }
}
