package com.example.hometask.controller;

import com.example.hometask.data.Showtime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ShowtimeController {

    @GetMapping
    public ResponseEntity<List<Showtime>> getShowtimes(Long movieId, String theater);

    @GetMapping("/{id}")
    public ResponseEntity<Showtime> getShowtimeById(@PathVariable Long id);

    @PostMapping
    public ResponseEntity<Showtime> addShowtime(@RequestBody Showtime showtime);

    @PutMapping("/{id}")
    public ResponseEntity<Showtime> updateShowtime(@PathVariable Long id, @RequestBody Showtime updatedShowtime);


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id);
}
