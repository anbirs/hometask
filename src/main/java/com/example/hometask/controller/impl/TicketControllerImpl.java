package com.example.hometask.controller.impl;

import com.example.hometask.controller.TicketController;
import com.example.hometask.data.ApiResponse;
import com.example.hometask.data.Ticket;
import com.example.hometask.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TicketControllerImpl implements TicketController {

    @Autowired
    private TicketService ticketService;

    @Override
    public ResponseEntity<ApiResponse<List<Ticket>>> getAllTickets() {
        return ResponseEntity.ok(ApiResponse.success(ticketService.getAllTickets()));
    }

    @Override
    public ResponseEntity<ApiResponse<List<Ticket>>> getTicketsByUser(Long userId) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.getAllTicketsByUser(userId)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<Ticket>>> getTicketsByShowtime(Long showtimeId) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.getAllTicketsByShowtime(showtimeId)));
    }

    @Override
    public ResponseEntity<ApiResponse<Ticket>> getTicketById(Long id) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.findTicketById(id)));
    }

    @Override
    public ResponseEntity<ApiResponse<Ticket>> addTicket(Ticket ticket) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.bookTicket(ticket)));
    }

    @Override
    public ResponseEntity<ApiResponse<Ticket>> updateTicket(Long id, Ticket updatedTicket) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.updateTicket(id, updatedTicket)));
    }

    @Override
    public ResponseEntity<ApiResponse<Long>> deleteTicket(Long id) {
        return ResponseEntity.ok(ApiResponse.success(ticketService.deleteTicket(id)));
    }
}
