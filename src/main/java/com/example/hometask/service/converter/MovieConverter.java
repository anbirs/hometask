package com.example.hometask.service.converter;

import com.example.hometask.data.Movie;
import com.example.hometask.repository.entity.MovieEntity;
import org.springframework.stereotype.Component;

@Component
public class MovieConverter implements Converter <Movie, MovieEntity>{

    @Override
    public Movie toDto(MovieEntity entity) {
        return new Movie(
                entity.getId(),
                entity.getTitle(),
                entity.getGenre(),
                entity.getDuration(),
                entity.getRating(),
                entity.getReleaseYear()
        );
    }

    @Override
    public MovieEntity toEntity(Movie dto) {
        return new MovieEntity(
                dto.getId(),
                dto.getTitle(),
                dto.getGenre(),
                dto.getDuration(),
                dto.getRating(),
                dto.getReleaseYear());
    }
}
