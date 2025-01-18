package com.example.hometask.service;

import com.example.hometask.data.Showtime;

import java.util.List;

public interface ShowtimesService {
    Showtime saveShowtime(Showtime showtime);
    List<Showtime> getShowtimes(Long movieId, String theaterName);
    Showtime getShowtimeById(Long id);
    Showtime updateShowtime(Long id, Showtime updatedShowtime);
    Long deleteShowtime(Long id);
}