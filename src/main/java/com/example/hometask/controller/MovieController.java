package com.example.hometask.controller;

import com.example.hometask.data.Movie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface MovieController {

    @GetMapping
    public ResponseEntity<List<Movie>> getMovies(@RequestParam(required = false) String details);

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id);

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie);

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie updatedMovie);


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id);
}
