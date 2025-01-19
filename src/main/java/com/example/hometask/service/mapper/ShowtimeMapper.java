package com.example.hometask.service.mapper;

import com.example.hometask.data.Showtime;
import com.example.hometask.repository.ShowtimeRepository;
import com.example.hometask.repository.entity.ShowtimeEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {MovieMapper.class})
public abstract class ShowtimeMapper implements EntityMapper<Showtime, ShowtimeEntity> {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Override
    @Mapping(target = "movieId", source = "movie.id")
    public abstract Showtime toDto(ShowtimeEntity entity);

    @Override
    @Mapping(target = "movie", source = "movieId", qualifiedByName = "idToMovieEntity")
    public abstract ShowtimeEntity toEntity(Showtime dto);

    @Override
    @Mapping(target = "movie", source = "movieId", qualifiedByName = "idToMovieEntity")
    @Mapping(target = "id", ignore = true)
    public abstract void updateEntityFromDto(Showtime dto, @MappingTarget ShowtimeEntity entity);

    @Named("idToShowtimeEntity")
    public ShowtimeEntity idToEntity(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found for ID: " + id));
    }
}
