package com.example.hometask.controller;

import com.example.hometask.data.ApiResponse;
import com.example.hometask.data.Movie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/movies")
@Tag(name = "Movies", description = "Movie Management")
public interface MovieController {

    @GetMapping
    @Operation(summary = "Get Movies", description = "Get all movies or the list of specific details of the movies. Available values: details=title,genre,duration,rating,releaseYear,")
    ResponseEntity<ApiResponse<List<Movie>>> getMovies(@RequestParam(required = false) String details);

    @GetMapping("/{id}")
    @Operation(summary = "Get Movie by Id", description = "Get specific movie")
    ResponseEntity<ApiResponse<Movie>> getMovieById(@PathVariable Long id);

    @PostMapping
    @Operation(summary = "Create new Movie", description = "Save new movie")
    ResponseEntity<ApiResponse<Movie>> addMovie(@RequestBody Movie movie);

    @PutMapping("/{id}")
    @Operation(summary = "Update existing Movie", description = "Change details of the movie")
    ResponseEntity<ApiResponse<Movie>> updateMovie(@PathVariable Long id, @RequestBody Movie updatedMovie);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Movie", description = "Remove the movie from the storage")
    ResponseEntity<ApiResponse<Long>> deleteMovie(@PathVariable Long id);
}
