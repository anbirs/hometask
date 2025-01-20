package com.example.hometask.service.impl;

import com.example.hometask.data.Ticket;
import com.example.hometask.repository.TicketRepository;
import com.example.hometask.repository.entity.TicketEntity;
import com.example.hometask.service.TicketService;
import com.example.hometask.service.mapper.TicketMapper;
import com.example.hometask.service.validator.TicketValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {
    public static final String TICKET_NOT_FOUND = "Ticket not found: ";
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private TicketValidator ticketValidator;

    @Override
    public Ticket bookTicket(Ticket ticket) {
        TicketEntity ticketEntity = ticketMapper.toEntity(ticket);
        ticketValidator.validateTicketForShowtime(ticketEntity);
        TicketEntity savedTicketEntity = ticketRepository.save(ticketEntity);
        return ticketMapper.toDto(savedTicketEntity);
    }

    @Override
    public Ticket updateTicket(Long id, Ticket updatedSTicket) {
        return ticketRepository.findById(id)
                .map(existingEntity -> {
                    ticketMapper.updateEntityFromDto(updatedSTicket, existingEntity);
                    return ticketRepository.save(existingEntity);
                })
                .map(ticketMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with ID: " + id));
    }

    @Override
    public Ticket findTicketById(Long id) {
        return ticketMapper.toDto(ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TICKET_NOT_FOUND + id)));
    }

    @Override
    @Transactional
    public Long deleteTicket(Long id) {
        TicketEntity ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TICKET_NOT_FOUND + id));
        ticket.setUser(null);
        ticket.setShowtime(null);
        ticketRepository.save(ticket);
        ticketRepository.deleteById(id);
        return id;
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getAllTicketsByUser(Long userId) {
        return ticketRepository.findByUser_Id(userId).stream()
                .map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getAllTicketsByShowtime(Long showtimeId) {
        return ticketRepository.findByShowtime_Id(showtimeId).stream()
                .map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }
}