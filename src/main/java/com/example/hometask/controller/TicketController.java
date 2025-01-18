package com.example.hometask.controller;

import com.example.hometask.data.Ticket;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TicketController {
    @GetMapping()
    ResponseEntity<List<Ticket>> getAllTickets();

    @GetMapping("/users/{id}")
    ResponseEntity<List<Ticket>> getTicketsByUser(@PathVariable Long userId);

    @GetMapping("/showtimes/{id}")
    ResponseEntity<List<Ticket>> getTicketsByShowtime(@PathVariable Long showtimeId);

    @GetMapping("/{id}")
    ResponseEntity<Ticket> getTicketById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<Ticket> addTicket(@RequestBody Ticket ticket);

    @PutMapping("/{id}")
    ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket updatedTicket);

    @DeleteMapping("/{id}")
    ResponseEntity<Long> deleteTicket(@PathVariable Long id);

}
