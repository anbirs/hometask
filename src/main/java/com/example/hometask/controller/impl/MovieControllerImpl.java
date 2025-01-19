package com.example.hometask.controller.impl;

import com.example.hometask.controller.MovieController;
import com.example.hometask.data.ApiResponse;
import com.example.hometask.data.Movie;
import com.example.hometask.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieControllerImpl implements MovieController {
    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<ApiResponse<Movie>> addMovie(Movie movie) {
        return ResponseEntity.ok(ApiResponse.success(movieService.saveMovie(movie)));
    }

    @Override
    public ResponseEntity<ApiResponse<Movie>> updateMovie(Long id, Movie updatedMovie) {
        return ResponseEntity.ok(ApiResponse.success(movieService.updateMovie(id, updatedMovie)));
    }

    @Override
    public ResponseEntity<ApiResponse<Long>> deleteMovie(Long id) {
        return ResponseEntity.ok(ApiResponse.success(movieService.deleteMovie(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Movie>>> getMovies(String details) {
        return ResponseEntity.ok(ApiResponse.success(movieService.getAllMovies(details)));
    }

    @Override
    public ResponseEntity<ApiResponse<Movie>> getMovieById(Long id) {
        return ResponseEntity.ok(ApiResponse.success(movieService.getMovieById(id)));
    }
}
