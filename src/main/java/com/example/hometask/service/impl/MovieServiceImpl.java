package com.example.hometask.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.example.hometask.data.Movie;
import com.example.hometask.repository.MovieRepository;
import com.example.hometask.repository.ShowtimeRepository;
import com.example.hometask.service.mapper.MovieField;
import com.example.hometask.service.MovieService;
import com.example.hometask.service.mapper.MovieMapper;
import com.example.hometask.repository.entity.MovieEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    public static final String MOVIE_NOT_FOUND = "Movie not found ";
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ShowtimeRepository showtimeRepository;
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public Movie saveMovie(Movie movie) {
        MovieEntity entity = movieMapper.toEntity(movie);
        return movieMapper.toDto(movieRepository.save(entity));
    }

    @Override
    public List<Movie> getAllMovies(String details) {

        final List<MovieEntity> response = new ArrayList<>();
        if (StringUtil.isNullOrEmpty(details)) {
            response.addAll(movieRepository.findAll());
        } else {
            mapDetailsToFields(details, response);
        }

        return response.stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }

    private void mapDetailsToFields(String details, List<MovieEntity> response) {
        final List<MovieField> fieldsToFetch = MovieField.parseFields(details);
        List<Object[]> results = movieRepository.findAllMoviesByDynamicFields(fieldsToFetch);

        for (Object[] row : results) {
           Map<String, Object> rowMap = new HashMap<>();
           int index = 0;
           for (MovieField field : fieldsToFetch) {
               rowMap.put(field.getFieldName(), row[index++]);
           }
           MovieEntity movie = objectMapper.convertValue(rowMap, MovieEntity.class);
           response.add(movie);
       }
    }

    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).map(movieMapper::toDto).orElseThrow(() -> new EntityNotFoundException(MOVIE_NOT_FOUND + id));
    }

    @Override
    public Movie updateMovie(Long id, Movie updatedMovie) {
        return movieRepository.findById(id)
                .map(existingEntity -> {
                    movieMapper.updateEntityFromDto(updatedMovie, existingEntity);
                    return movieRepository.save(existingEntity);
                })
                .map(movieMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(MOVIE_NOT_FOUND + id));

    }

    @Override
    @Transactional
    public Long deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new EntityNotFoundException(MOVIE_NOT_FOUND + id);
        }
        showtimeRepository.cleanMovieReferences(id);
        movieRepository.deleteById(id);
        return id;
    }
}
