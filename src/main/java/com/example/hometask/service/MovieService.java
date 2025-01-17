package com.example.hometask.service;

import com.example.hometask.data.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    Movie saveMovie(Movie movie);
    List<Movie> getAllMovies();
    Optional<Movie> getMovieById(Long id);
    Movie updateMovie(Long id, Movie updatedMovie);
    void deleteMovie(Long id);
}