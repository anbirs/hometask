package com.example.hometask.controller;

import com.example.hometask.data.Showtime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ShowtimeController {

    @GetMapping
    ResponseEntity<List<Showtime>> getShowtimes(Long movieId, String theater);

    @GetMapping("/{id}")
    ResponseEntity<Showtime> getShowtimeById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<Showtime> addShowtime(@RequestBody Showtime showtime);

    @PutMapping("/{id}")
    ResponseEntity<Showtime> updateShowtime(@PathVariable Long id, @RequestBody Showtime updatedShowtime);


    @DeleteMapping("/{id}")
    ResponseEntity<Long> deleteShowtime(@PathVariable Long id);
}
