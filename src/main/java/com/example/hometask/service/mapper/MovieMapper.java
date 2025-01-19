package com.example.hometask.service.mapper;

import com.example.hometask.data.Movie;
import com.example.hometask.repository.MovieRepository;
import com.example.hometask.repository.entity.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class MovieMapper implements EntityMapper<Movie, MovieEntity> {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public abstract Movie toDto(MovieEntity entity);

    @Override
    public abstract MovieEntity toEntity(Movie dto);

    @Override
    @Mapping(target = "id", ignore = true)
    public abstract void updateEntityFromDto(Movie dto, @MappingTarget MovieEntity entity);

    @Named("idToMovieEntity")
    public MovieEntity idToEntity(Long id) {
        if (id == null) {
            return null;
        }
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found for ID: " + id));
    }
}
