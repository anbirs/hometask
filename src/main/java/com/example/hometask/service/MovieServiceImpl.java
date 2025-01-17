package com.example.hometask.service;

import com.example.hometask.data.Movie;
import com.example.hometask.repository.MovieRepository;
import com.example.hometask.repository.converter.Converter;
import com.example.hometask.repository.converter.MovieConverter;
import com.example.hometask.repository.entity.MovieEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieConverter converter;


    @Override
    public Movie saveMovie(Movie movie) {
        MovieEntity entity = converter.toEntity(movie);
        return converter.toDto(movieRepository.save(entity));
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id).map(converter::toDto);
    }

    @Override
    public Movie updateMovie(Long id, Movie updatedMovie) {
        return movieRepository.findById(id).map(existingEntity -> {
            existingEntity.setTitle(updatedMovie.getTitle());
            existingEntity.setGenre(updatedMovie.getGenre());
            existingEntity.setDuration(updatedMovie.getDuration());
            existingEntity.setRating(updatedMovie.getRating());
            existingEntity.setReleaseYear(updatedMovie.getReleaseYear());
            MovieEntity savedEntity = movieRepository.save(existingEntity);
            return converter.toDto(savedEntity);
        }).orElseThrow(() -> new EntityNotFoundException("Movie not found with ID: " + id));
    }

    @Override
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new EntityNotFoundException("Movie not found: " + id);
        }
        movieRepository.deleteById(id);
    }
}
