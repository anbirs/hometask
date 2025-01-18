package com.example.hometask.controller.impl;

import com.example.hometask.controller.TicketController;
import com.example.hometask.data.Ticket;
import com.example.hometask.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/tickets")
public class TicketControllerImpl implements TicketController {

    @Autowired
    private TicketService ticketService;

    @Override
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @Override
    public ResponseEntity<List<Ticket>> getTicketsByUser(Long userId) {
        return ResponseEntity.ok(ticketService.getAllTicketsByUser(userId));
    }

    @Override
    public ResponseEntity<List<Ticket>> getTicketsByShowtime(Long showtimeId) {
        return ResponseEntity.ok(ticketService.getAllTicketsByShowtime(showtimeId));
    }

    @Override
    public ResponseEntity<Ticket> getTicketById(Long id) {
        return ResponseEntity.ok(ticketService.findTicketById(id));
    }

    @Override
    public ResponseEntity<Ticket> addTicket(Ticket ticket) {
            return ResponseEntity.ok(ticketService.bookTicket(ticket));
    }

    @Override
    public ResponseEntity<Ticket> updateTicket(Long id, Ticket updatedTicket) {
        return ResponseEntity.ok(ticketService.updateTicket(id, updatedTicket));
    }

    @Override
    public ResponseEntity<Long> deleteTicket(Long id) {
        return ResponseEntity.ok(ticketService.deleteTicket(id));
    }
}
