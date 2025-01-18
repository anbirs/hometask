package com.example.hometask.service.impl;

import com.example.hometask.data.Ticket;
import com.example.hometask.repository.ShowtimeRepository;
import com.example.hometask.repository.TicketRepository;
import com.example.hometask.repository.entity.ShowtimeEntity;
import com.example.hometask.repository.entity.TicketEntity;
import com.example.hometask.service.TicketService;
import com.example.hometask.service.converter.ShowtimeConverter;
import com.example.hometask.service.converter.TicketConverter;
import com.example.hometask.service.converter.UserConverter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ShowtimeRepository showtimeRepository;
    @Autowired
    private TicketConverter ticketConverter;
    @Autowired
    private ShowtimeConverter showtimeConverter;
    @Autowired
    private UserConverter userConverter;

    @Override
    public Ticket bookTicket(Ticket ticket) {
        TicketEntity ticketEntity = ticketConverter.toEntity(ticket);
        validateTicketForShowtime(ticketEntity);
        TicketEntity savedTicketEntity = ticketRepository.save(ticketEntity);
        ShowtimeEntity showtime = ticketEntity.getShowtime();
        showtime.getTickets().add(savedTicketEntity);
        showtimeRepository.save(showtime);
        return ticketConverter.toDto(savedTicketEntity);
    }

    @Override
    public Ticket updateTicket(Long id, Ticket updatedSTicket) {
        return ticketRepository.findById(id).map(existingEntity -> {
            existingEntity.setPrice(updatedSTicket.getPrice());
            existingEntity.setShowtime(showtimeConverter.toEntity(updatedSTicket.getShowtime()));
            existingEntity.setUser(userConverter.toEntity(updatedSTicket.getUser()));
            existingEntity.setSeatNumber(updatedSTicket.getSeatNumber());
            TicketEntity savedEntity = ticketRepository.save(existingEntity);
            return ticketConverter.toDto(savedEntity);
        }).orElseThrow(() -> new EntityNotFoundException("Ticket not found with ID: " + id));
    }

    private void validateTicketForShowtime(TicketEntity ticketEntity) {

        ShowtimeEntity showtime = ticketEntity.getShowtime();

        Set<TicketEntity> tickets = showtime.getTickets();
        if (tickets.size() >= showtime.getMaxSeats()) {
            throw new IllegalArgumentException("Unable to create ticket for showtime: Sold out");
        }
        if (tickets.stream().anyMatch(ticket ->
                ticketEntity.getSeatNumber().equals(ticket.getSeatNumber()))) {
            throw new IllegalArgumentException("Unable to create ticket for showtime: Seat already booked");
        }

    }

    @Override
    public Ticket findTicketById(Long id) {
        return ticketConverter.toDto(ticketRepository.findById(id).orElseThrow());
    }

    @Override
    public Long deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new EntityNotFoundException("Ticket not found: " + id);
        }
        ticketRepository.deleteById(id);
        return id;
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticketConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getAllTicketsByUser(Long userId) {
        return ticketRepository.findByUser_Id(userId).stream()
                .map(ticketConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getAllTicketsByShowtime(Long showtimeId) {
        return ticketRepository.findByShowtime_Id(showtimeId).stream()
                .map(ticketConverter::toDto)
                .collect(Collectors.toList());
    }
}