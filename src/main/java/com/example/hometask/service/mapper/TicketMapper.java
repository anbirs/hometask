package com.example.hometask.service.mapper;

import com.example.hometask.data.Ticket;
import com.example.hometask.repository.entity.TicketEntity;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ShowtimeMapper.class})
public abstract class TicketMapper implements EntityMapper<Ticket, TicketEntity> {

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "showtimeId", source = "showtime.id")
    public abstract Ticket toDto(TicketEntity entity);

    @Override
    @Mapping(target = "user", source = "userId", qualifiedByName = "idToUserEntity")
    @Mapping(target = "showtime", source = "showtimeId", qualifiedByName = "idToShowtimeEntity")
    public abstract TicketEntity toEntity(Ticket dto);

    @Override
    @Mapping(target = "user", source = "userId", qualifiedByName = "idToUserEntity")
    @Mapping(target = "showtime", source = "showtimeId", qualifiedByName = "idToShowtimeEntity")
    @Mapping(target = "id", ignore = true)
    public abstract void updateEntityFromDto(Ticket dto, @MappingTarget TicketEntity entity);
}
