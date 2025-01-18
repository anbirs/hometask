package com.example.hometask.data;

import java.time.LocalDateTime;

public class Showtime {

    private Long id;
    private Long movie;
    private String theater;
    private Integer maxSeats;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Showtime(Long id, Long movie, String theater, Integer maxSeats, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.movie = movie;
        this.theater = theater;
        this.maxSeats = maxSeats;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Showtime() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovie() {
        return movie;
    }

    public void setMovie(Long movie) {
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

    public Integer getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(Integer maxSeats) {
        this.maxSeats = maxSeats;
    }
}
