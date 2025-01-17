package com.example.hometask.service;

import com.example.hometask.data.Showtime;
import com.example.hometask.repository.ShowtimeRepository;
import com.example.hometask.repository.entity.ShowtimeEntity;
import com.example.hometask.service.converter.MovieConverter;
import com.example.hometask.service.converter.ShowtimeConverter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShowtimesServiceImpl implements ShowtimesService {
    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private ShowtimeConverter showtimeConverter;

    @Autowired
    private MovieConverter movieConverter;

    @Override
    public Showtime saveShowtime(Showtime showtime) {
        // find showtimes by theater and  existing start  < current start < existing end   or   existing start  <  current end < existing end
        validateOverlappingShowtimes(showtime);
        return showtimeConverter.toDto(showtimeRepository.save(showtimeConverter.toEntity(showtime)));
    }

    private void validateOverlappingShowtimes(Showtime showtime) {
        List<ShowtimeEntity> existingShowtimes = showtimeRepository.findByTheater(showtime.getTheater());

        LocalDateTime startTime = showtime.getStartTime();
        LocalDateTime endTime = showtime.getEndTime();

        List<ShowtimeEntity> overlappingShowtimes = existingShowtimes.stream().filter(existingShowtime -> startTime.isBefore(existingShowtime.getEndTime()) && endTime.isAfter(existingShowtime.getStartTime())
        ).toList();

        if (!overlappingShowtimes.isEmpty()) {
            throw new IllegalArgumentException("Unable to create showtime: Overlapping " + overlappingShowtimes.stream()
                    .map(st -> st.getId() + "(" + st.getStartTime() + "-" + st.getEndTime() + ")").collect(Collectors.joining(";")));
        }
    }

    @Override
    public List<Showtime> getShowtimes(Long movieId, String theaterName) {
        final List<ShowtimeEntity> response;
        if (movieId != null && theaterName != null) {
            response = showtimeRepository.findByMovieIdAndTheater(movieId, theaterName);
        } else if (movieId != null) {
            response = showtimeRepository.findByMovieId(movieId);
        } else if (theaterName != null) {
            response = showtimeRepository.findByTheater(theaterName);
        } else {
            response = showtimeRepository.findAll();
        }
        return response.stream()
                .map(showtimeConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Showtime> getShowtimeById(Long id) {
        return Optional.empty();
    }

    @Override
    public Showtime updateShowtime(Long id, Showtime updatedShowtime) {

        ShowtimeEntity existingShowtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Showtime not found with ID: " + id));

        validateOverlappingShowtimes(updatedShowtime);


        existingShowtime.setMovie(movieConverter.toEntity(updatedShowtime.getMovie()));
        existingShowtime.setTheater(updatedShowtime.getTheater());
        existingShowtime.setStartTime(updatedShowtime.getStartTime());
        existingShowtime.setEndTime(updatedShowtime.getEndTime());

        return showtimeConverter.toDto(showtimeRepository.save(existingShowtime));
    }

    @Override
    public void deleteShowtime(Long id) {

    }
}
