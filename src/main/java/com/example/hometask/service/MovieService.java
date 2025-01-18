package com.example.hometask.service;

import com.example.hometask.data.Movie;

import java.util.List;

public interface MovieService {
    Movie saveMovie(Movie movie);
    List<Movie> getAllMovies(String details);
    Movie getMovieById(Long id);
    Movie updateMovie(Long id, Movie updatedMovie);
    Long deleteMovie(Long id);
}