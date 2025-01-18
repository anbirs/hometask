package com.example.hometask.service.converter;

import com.example.hometask.data.Showtime;
import com.example.hometask.repository.ShowtimeRepository;
import com.example.hometask.repository.entity.ShowtimeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShowtimeConverter implements Converter <Showtime, ShowtimeEntity>{
    @Autowired
    private MovieConverter movieConverter;
    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Override
    public Showtime toDto(ShowtimeEntity entity) {
        return new Showtime(
                entity.getId(),
                movieConverter.toDto(entity.getMovie()).getId(),
                entity.getTheater(),
                entity.getMaxSeats(),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }

    @Override
    public ShowtimeEntity toEntity(Showtime dto) {
        return new ShowtimeEntity(
        dto.getId(),
        movieConverter.toEntity(dto.getMovie()),
        dto.getTheater(),
        dto.getMaxSeats(),
        dto.getStartTime(),
        dto.getEndTime());
    }

    public ShowtimeEntity toEntity(Long id) {
        return showtimeRepository.findById(id).orElseThrow();
    }
}
