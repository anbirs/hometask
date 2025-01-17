package com.example.hometask.controller;

import com.example.hometask.data.Movie;
import com.example.hometask.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/movies")
public class MovieControllerImpl implements MovieController {
    private final MovieService movieService;

    public MovieControllerImpl(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.saveMovie(movie));
    }

    @Override
    public ResponseEntity<Movie> updateMovie(Long id, Movie updatedMovie) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteMovie(Long id) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @Override
    public ResponseEntity<Movie> getMovieById(Long id) {
        return null;
    }
}
