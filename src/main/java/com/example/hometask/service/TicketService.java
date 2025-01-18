package com.example.hometask.service;

import com.example.hometask.data.Ticket;

import java.util.List;

public interface TicketService {
     Ticket bookTicket(Ticket ticket);
     Ticket updateTicket(Long id, Ticket updatedSTicket);
     Ticket findTicketById(Long id);
     Long deleteTicket(Long id);
     List<Ticket> getAllTickets();
     List<Ticket> getAllTicketsByUser(Long userId);
     List<Ticket> getAllTicketsByShowtime(Long showtimeId);
}