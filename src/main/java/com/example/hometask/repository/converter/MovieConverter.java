package com.example.hometask.repository.converter;

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
        MovieEntity entity = new MovieEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setGenre(dto.getGenre());
        entity.setDuration(dto.getDuration());
        entity.setRating(dto.getRating());
        entity.setReleaseYear(dto.getReleaseYear());
        return entity;
    }
}
