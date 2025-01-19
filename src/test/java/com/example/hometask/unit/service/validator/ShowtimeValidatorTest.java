package com.example.hometask.unit.service.validator;

import com.example.hometask.repository.ShowtimeRepository;
import com.example.hometask.repository.entity.ShowtimeEntity;
import com.example.hometask.service.validator.ShowtimeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShowtimeValidatorTest {

    @Mock
    private ShowtimeRepository showtimeRepository;

    @InjectMocks
    private ShowtimeValidator showtimeValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateOverlappingShowtimes() {
        ShowtimeEntity newShowtime = new ShowtimeEntity(
                1L,
                null,
                "Theater 1",
                100,
                LocalDateTime.of(2025, 1, 20, 10, 0),
                LocalDateTime.of(2025, 1, 20, 12, 0)
        );

        List<ShowtimeEntity> existingShowtimes = List.of(
                new ShowtimeEntity(2L, null, "Theater 1", 100,
                        LocalDateTime.of(2025, 1, 20, 8, 0),
                        LocalDateTime.of(2025, 1, 20, 9, 30)),
                new ShowtimeEntity(3L, null, "Theater 1", 100,
                        LocalDateTime.of(2025, 1, 20, 12, 30),
                        LocalDateTime.of(2025, 1, 20, 14, 0))
        );

        when(showtimeRepository.findByTheaterIgnoreCase("Theater 1")).thenReturn(existingShowtimes);

        assertDoesNotThrow(() -> showtimeValidator.validateOverlappingShowtimes(newShowtime));

        verify(showtimeRepository, times(1)).findByTheaterIgnoreCase("Theater 1");

    }

    @Test
    void validateOverlappingShowtimes_Overlapping() {
        ShowtimeEntity newShowtime = new ShowtimeEntity(
                1L,
                null,
                "Theater 1",
                100,
                LocalDateTime.of(2025, 1, 20, 10, 0),
                LocalDateTime.of(2025, 1, 20, 12, 0)
        );

        List<ShowtimeEntity> existingShowtimes = List.of(
                new ShowtimeEntity(2L, null, "Theater 1", 100,
                        LocalDateTime.of(2025, 1, 20, 9, 0),
                        LocalDateTime.of(2025, 1, 20, 10, 30)),
                new ShowtimeEntity(3L, null, "Theater 1", 100,
                        LocalDateTime.of(2025, 1, 20, 11, 30),
                        LocalDateTime.of(2025, 1, 20, 13, 0))
        );

        when(showtimeRepository.findByTheaterIgnoreCase("Theater 1")).thenReturn(existingShowtimes);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> showtimeValidator.validateOverlappingShowtimes(newShowtime));

        assertTrue(exception.getMessage().contains("Unable to create showtime: Overlapping"));

        verify(showtimeRepository, times(1)).findByTheaterIgnoreCase("Theater 1");


    }


    @Test
    void validateOverlappingShowtimes_DifferentTheatre() {
        ShowtimeEntity newShowtime = new ShowtimeEntity(
                1L,
                null,
                "Theater 2",
                100,
                LocalDateTime.of(2025, 1, 20, 10, 0),
                LocalDateTime.of(2025, 1, 20, 12, 0)
        );

        List<ShowtimeEntity> existingShowtimes = List.of(
                new ShowtimeEntity(2L, null, "Theater 1", 100,
                        LocalDateTime.of(2025, 1, 20, 9, 0),
                        LocalDateTime.of(2025, 1, 20, 10, 30))
        );

        when(showtimeRepository.findByTheaterIgnoreCase("Theater 2")).thenReturn(List.of());

        assertDoesNotThrow(() -> showtimeValidator.validateOverlappingShowtimes(newShowtime));

        verify(showtimeRepository, times(1)).findByTheaterIgnoreCase("Theater 2");

    }
}