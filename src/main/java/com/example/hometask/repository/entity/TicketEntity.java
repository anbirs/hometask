package com.example.hometask.repository.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ShowtimeEntity showtime;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private UserEntity user;


    private String seatNumber;
    private BigDecimal price;

}
