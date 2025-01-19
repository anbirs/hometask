package com.example.hometask.service.validator;

import com.example.hometask.repository.ShowtimeRepository;
import com.example.hometask.repository.entity.ShowtimeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ShowtimeValidator {
    @Autowired
    private ShowtimeRepository showtimeRepository;

    public void validateOverlappingShowtimes(ShowtimeEntity showtime) {
        List<ShowtimeEntity> existingShowtimes = showtimeRepository.findByTheaterIgnoreCase(showtime.getTheater());

        LocalDateTime startTime = showtime.getStartTime();
        LocalDateTime endTime = showtime.getEndTime();

        List<ShowtimeEntity> overlappingShowtimes = existingShowtimes.stream().filter(existingShowtime -> !Objects.equals(existingShowtime.getId(), showtime.getId()))
                .filter(existingShowtime -> startTime.isBefore(existingShowtime.getEndTime()) && endTime.isAfter(existingShowtime.getStartTime())
                ).toList();

        if (!overlappingShowtimes.isEmpty()) {
            throw new IllegalArgumentException("Unable to create showtime: Overlapping " + overlappingShowtimes.stream()
                    .map(st -> st.getId() + "(" + st.getStartTime() + "-" + st.getEndTime() + ")").collect(Collectors.joining(";")));
        }
    }
}
