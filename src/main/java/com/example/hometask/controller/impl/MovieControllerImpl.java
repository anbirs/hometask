package com.example.hometask.controller.impl;

import com.example.hometask.controller.MovieController;
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
    public ResponseEntity<Movie> addMovie(Movie movie) {
        return ResponseEntity.ok(movieService.saveMovie(movie));
    }

    @Override
    public ResponseEntity<Movie> updateMovie(Long id, Movie updatedMovie) {
        return ResponseEntity.ok(movieService.updateMovie(id, updatedMovie));
    }

    @Override
    public ResponseEntity<Long> deleteMovie(Long id) {
        return ResponseEntity.ok(movieService.deleteMovie(id));
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getMovies(String details) {
        return ResponseEntity.ok(movieService.getAllMovies(details));
    }

    @Override
    public ResponseEntity<Movie> getMovieById(Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }
}
