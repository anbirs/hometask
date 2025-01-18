package com.example.hometask.controller;

import com.example.hometask.data.Movie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface MovieController {

    @GetMapping
    ResponseEntity<List<Movie>> getMovies(@RequestParam(required = false) String details);

    @GetMapping("/{id}")
    ResponseEntity<Movie> getMovieById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<Movie> addMovie(@RequestBody Movie movie);

    @PutMapping("/{id}")
    ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie updatedMovie);

    @DeleteMapping("/{id}")
    ResponseEntity<Long> deleteMovie(@PathVariable Long id);
}
