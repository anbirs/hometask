package com.example.hometask.controller;

import com.example.hometask.data.Movie;
import com.example.hometask.data.Showtime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RequestMapping("/api/showtimes")
public interface ShowtimeController {

    @GetMapping
    public ResponseEntity<List<Showtime>> getShowtimes();

    @GetMapping
    public ResponseEntity<List<Showtime>> getShowtimesByMovie(@PathVariable Long movieId);

    @GetMapping
    public ResponseEntity<List<Showtime>> getShowtimesByTheater(@PathVariable Long theaterId);

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id);

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie);

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie updatedMovie);


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id);
}
