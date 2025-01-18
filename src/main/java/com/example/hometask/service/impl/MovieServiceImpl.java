package com.example.hometask.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.example.hometask.data.Movie;
import com.example.hometask.repository.MovieRepository;
import com.example.hometask.service.MovieField;
import com.example.hometask.service.MovieService;
import com.example.hometask.service.converter.MovieConverter;
import com.example.hometask.repository.entity.MovieEntity;
import com.example.hometask.service.mapper.MovieFieldMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieConverter movieConverter;


    @Override
    public Movie saveMovie(Movie movie) {
        MovieEntity entity = movieConverter.toEntity(movie);
        return movieConverter.toDto(movieRepository.save(entity));
    }

    @Override
    public List<Movie> getAllMovies(String details) {

        final List<MovieEntity> response = new ArrayList<>();
        if (StringUtil.isNullOrEmpty(details)) {
            response.addAll(movieRepository.findAll());
        } else {
            final Set<MovieField> fieldsToFetch = MovieField.parseFields(details);
            List<Object[]> results = movieRepository.findAllMoviesByDynamicFields(fieldsToFetch);

             for (Object[] row : results) {
                Map<String, Object> rowMap = new HashMap<>();
                int index = 0;
                for (MovieField field : fieldsToFetch) {
                    rowMap.put(field.getFieldName(), row[index++]);
                }
                response.add(MovieFieldMapper.INSTANCE.map(rowMap));
            }
        }

        return response.stream()
                .map(movieConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).map(movieConverter::toDto).orElseThrow();
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
            return movieConverter.toDto(savedEntity);
        }).orElseThrow(() -> new EntityNotFoundException("Movie not found with ID: " + id));
    }

    @Override
    public Long deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new EntityNotFoundException("Movie not found: " + id);
        }
        movieRepository.deleteById(id);
        return id;
    }
}
