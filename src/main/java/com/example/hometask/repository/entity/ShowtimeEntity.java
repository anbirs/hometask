package com.example.hometask.repository.entity;

import com.example.hometask.data.Movie;
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

    public ShowtimeEntity(Long id, Movie movie, String theater, LocalDateTime startTime, LocalDateTime endTime) {

    }

    public ShowtimeEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovieEntity getMovie() {
        return movie;
    }

    public void setMovie(MovieEntity movie) {
        this.movie = movie;
    }

    public String getTheater() {
        return theater;
    }

    public void setTheater(String theater) {
        this.theater = theater;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
