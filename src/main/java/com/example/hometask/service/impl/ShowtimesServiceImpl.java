package com.example.hometask.service.impl;

import com.example.hometask.data.Showtime;
import com.example.hometask.repository.ShowtimeRepository;
import com.example.hometask.repository.TicketRepository;
import com.example.hometask.repository.entity.ShowtimeEntity;
import com.example.hometask.service.ShowtimesService;
import com.example.hometask.service.mapper.ShowtimeMapper;
import com.example.hometask.service.validator.ShowtimeValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimesServiceImpl implements ShowtimesService {
    public static final String SHOWTIME_NOT_FOUND = "Showtime not found: ";
    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowtimeMapper showtimeMapper;

    @Autowired
    private ShowtimeValidator showtimeValidator;

    @Override
    public Showtime saveShowtime(Showtime showtime) {
        // find showtimes by theater and  existing start  < current start < existing end   or   existing start  <  current end < existing end
        ShowtimeEntity entity = showtimeMapper.toEntity(showtime);
        showtimeValidator.validateOverlappingShowtimes(entity);
        return showtimeMapper.toDto(showtimeRepository.save(entity));
    }

    @Override
    public List<Showtime> getShowtimes(Long movieId, String theaterName) {
        final List<ShowtimeEntity> response;
        if (movieId != null && theaterName != null) {
            response = showtimeRepository.findByMovieIdAndTheaterIgnoreCase(movieId, theaterName);
        } else if (movieId != null) {
            response = showtimeRepository.findByMovieId(movieId);
        } else if (theaterName != null) {
            response = showtimeRepository.findByTheaterIgnoreCase(theaterName);
        } else {
            response = showtimeRepository.findAll();
        }
        return response.stream()
                .map(showtimeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Showtime getShowtimeById(Long id) {
        return showtimeMapper.toDto(showtimeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(SHOWTIME_NOT_FOUND + id)));
    }

    @Override
    public Showtime updateShowtime(Long id, Showtime updatedShowtime) {
        return showtimeRepository.findById(id)
                .map(existingEntity -> {
                    showtimeMapper.updateEntityFromDto(updatedShowtime, existingEntity);
                    return showtimeRepository.save(existingEntity);
                })
                .map(showtimeMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(SHOWTIME_NOT_FOUND + id));
    }

    @Override
    public Long deleteShowtime(Long id) {
        if (!ticketRepository.findByShowtime_Id(id).isEmpty()) {
            throw new IllegalArgumentException("Showtime cannot be removed, as it has related tickets");
        }
        if (!showtimeRepository.existsById(id)) {
            throw new EntityNotFoundException(SHOWTIME_NOT_FOUND + id);
        }

        showtimeRepository.deleteById(id);
        return id;
    }
}
