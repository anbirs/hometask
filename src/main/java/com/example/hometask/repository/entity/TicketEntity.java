package com.example.hometask.repository.entity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ShowtimeEntity showtimeEntity;

    private String seatNumber;
    private BigDecimal price;
}
