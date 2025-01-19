package com.example.hometask.service.validator;

import com.example.hometask.repository.TicketRepository;
import com.example.hometask.repository.entity.ShowtimeEntity;
import com.example.hometask.repository.entity.TicketEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketValidator {
    @Autowired
    private TicketRepository ticketRepository;

    public void validateTicketForShowtime(TicketEntity ticketEntity) {

        ShowtimeEntity showtime = ticketEntity.getShowtime();

        List<TicketEntity> tickets = ticketRepository.findByShowtime_Id(showtime.getId());
        if (tickets == null) {
            throw new IllegalArgumentException("Unable to create ticket for showtime: Seats number is not defined");
        }
        if (tickets.size() >= showtime.getMaxSeats()) {
            throw new IllegalArgumentException("Unable to create ticket for showtime: Sold out");
        }
        if (tickets.stream().anyMatch(ticket ->
                ticketEntity.getSeatNumber().equals(ticket.getSeatNumber()))) {
            throw new IllegalArgumentException("Unable to create ticket for showtime: Seat already booked");
        }

    }
}
