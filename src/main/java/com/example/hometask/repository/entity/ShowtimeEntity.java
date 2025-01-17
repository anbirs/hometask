package com.example.hometask.repository.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ShowtimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MovieEntity movie;

    private String theater;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
