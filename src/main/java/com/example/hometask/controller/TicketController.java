package com.example.hometask.controller;

import com.example.hometask.data.ApiResponse;
import com.example.hometask.data.Ticket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/tickets")
@Tag(name = "Tickets", description = "Ticket Management: booking, search")
public interface TicketController {
    @GetMapping()
    @Operation(summary = "Get all Tickets", description = "Get all booked tickets")
    ResponseEntity<ApiResponse<List<Ticket>>> getAllTickets();

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Tickets by Showtime", description = "Find all booked tickets by specific user")
    ResponseEntity<ApiResponse<List<Ticket>>> getTicketsByUser(@PathVariable Long userId);

    @GetMapping("/showtime/{showtimeId}")
    @Operation(summary = "Get Tickets by Showtime", description = "Find all booked tickets for specific showtime")
    ResponseEntity<ApiResponse<List<Ticket>>> getTicketsByShowtime(@PathVariable Long showtimeId);

    @GetMapping("/{id}")
    @Operation(summary = "Get Ticket by Id", description = "Get specific Ticket")
    ResponseEntity<ApiResponse<Ticket>> getTicketById(@PathVariable Long id);

    @PostMapping
    @Operation(summary = "Create new Ticket", description = "Book a ticket")
    ResponseEntity<ApiResponse<Ticket>> addTicket(@RequestBody Ticket ticket);

    @PutMapping("/{id}")
    @Operation(summary = "Update existing Ticket", description = "Change details of the Ticket")
    ResponseEntity<ApiResponse<Ticket>> updateTicket(@PathVariable Long id, @RequestBody Ticket updatedTicket);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Ticket", description = "Remove the Ticket from the storage")
    ResponseEntity<ApiResponse<Long>> deleteTicket(@PathVariable Long id);

}
