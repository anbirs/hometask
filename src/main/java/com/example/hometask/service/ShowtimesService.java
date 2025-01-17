package com.example.hometask.service;

import com.example.hometask.data.Showtime;

import java.util.List;
import java.util.Optional;

public interface ShowtimesService {
    Showtime saveShowtime(Showtime showtime);
    List<Showtime> getShowtimes(Long movieId, String theaterName);
    Optional<Showtime> getShowtimeById(Long id);
    Showtime updateShowtime(Long id, Showtime updatedShowtime);
    void deleteShowtime(Long id);
}