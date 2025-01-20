package com.example.hometask.unit.service;

import com.example.hometask.data.Showtime;
import com.example.hometask.repository.ShowtimeRepository;
import com.example.hometask.repository.TicketRepository;
import com.example.hometask.repository.entity.ShowtimeEntity;
import com.example.hometask.service.impl.ShowtimesServiceImpl;
import com.example.hometask.service.mapper.ShowtimeMapper;
import com.example.hometask.service.validator.ShowtimeValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShowtimesServiceTest {

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ShowtimeMapper showtimeMapper;

    @Mock
    private ShowtimeValidator showtimeValidator;

    @InjectMocks
    private ShowtimesServiceImpl showtimesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveShowtime() {
        Showtime dto = new Showtime(null, 1L, "Theater 1", 100, null, null);
        ShowtimeEntity entity = new ShowtimeEntity(null, null, "Theater 1", 100, null, null);
        ShowtimeEntity savedEntity = new ShowtimeEntity(1L, null, "Theater 1", 100, null, null);
        Showtime expectedDto = new Showtime(1L, 1L, "Theater 1", 100, null, null);

        when(showtimeMapper.toEntity(dto)).thenReturn(entity);
        when(showtimeRepository.save(entity)).thenReturn(savedEntity);
        when(showtimeMapper.toDto(savedEntity)).thenReturn(expectedDto);

        Showtime result = showtimesService.saveShowtime(dto);

        assertEquals(expectedDto, result);
        verify(showtimeValidator, times(1)).validateOverlappingShowtimes(entity);
        verify(showtimeRepository, times(1)).save(entity);
        verify(showtimeMapper, times(1)).toDto(savedEntity);
    }

    @Test
    void saveShowtime_Overlapping() {
        Showtime showtime = new Showtime(null, 1L, "Theater 1", 100, null, null);
        ShowtimeEntity entity = new ShowtimeEntity(null, null, "Theater 1", 100, null, null);

        when(showtimeMapper.toEntity(showtime)).thenReturn(entity);
        doThrow(new IllegalArgumentException("Overlapping")).when(showtimeValidator).validateOverlappingShowtimes(entity);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> showtimesService.saveShowtime(showtime));

        assertEquals("Overlapping", exception.getMessage());
        verify(showtimeValidator, times(1)).validateOverlappingShowtimes(entity);
        verifyNoInteractions(showtimeRepository);
    }

    @Test
    void getShowtimes() {
        List<ShowtimeEntity> entities = List.of(
                new ShowtimeEntity(1L, null, "Theater 1", 100, null, null),
                new ShowtimeEntity(2L, null, "Theater 2", 120, null, null)
        );

        List<Showtime> expectedDtos = List.of(
                new Showtime(1L, 1L, "Theater 1", 100, null, null),
                new Showtime(2L, 1L, "Theater 2", 120, null, null)
        );

        when(showtimeRepository.findAll()).thenReturn(entities);
        when(showtimeMapper.toDto(any(ShowtimeEntity.class))).thenAnswer(invocation -> {
            ShowtimeEntity entity = invocation.getArgument(0);
            return expectedDtos.stream().filter(dto -> dto.getId().equals(entity.getId())).findFirst().orElse(null);
        });

        List<Showtime> result = showtimesService.getShowtimes(null, null);

        assertEquals(2, result.size());
        assertEquals(expectedDtos, result);
        verify(showtimeRepository, times(1)).findAll();
        verify(showtimeMapper, times(2)).toDto(any(ShowtimeEntity.class));
    }

    @Test
    void getShowtimes_ByMovieIdAndTheater() {
        Long movieId = 1L;
        String theaterName = "Theater 1";

        List<ShowtimeEntity> entities = List.of(
                new ShowtimeEntity(1L, null, "Theater 1", 100, null, null),
                new ShowtimeEntity(2L, null, "Theater 1", 120, null, null)
        );

        List<Showtime> expectedDtos = List.of(
                new Showtime(1L, 1L, "Theater 1", 100, null, null),
                new Showtime(2L, 1L, "Theater 1", 120, null, null)
        );

        when(showtimeRepository.findByMovieIdAndTheaterIgnoreCase(movieId, theaterName)).thenReturn(entities);
        when(showtimeMapper.toDto(any(ShowtimeEntity.class))).thenAnswer(invocation -> {
            ShowtimeEntity entity = invocation.getArgument(0);
            return expectedDtos.stream().filter(dto -> dto.getId().equals(entity.getId())).findFirst().orElse(null);
        });

        List<Showtime> result = showtimesService.getShowtimes(movieId, theaterName);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedDtos, result);
        verify(showtimeRepository, times(1)).findByMovieIdAndTheaterIgnoreCase(movieId, theaterName);
        verify(showtimeMapper, times(2)).toDto(any(ShowtimeEntity.class));
    }

    @Test
    void getShowtimes_ByMovieId() {
        Long movieId = 1L;

        List<ShowtimeEntity> entities = List.of(
                new ShowtimeEntity(1L, null, "Theater 1", 100, null, null),
                new ShowtimeEntity(2L, null, "Theater 2", 120, null, null)
        );

        List<Showtime> expectedDtos = List.of(
                new Showtime(1L, 1L, "Theater 1", 100, null, null),
                new Showtime(2L, 1L, "Theater 2", 120, null, null)
        );

        when(showtimeRepository.findByMovieId(movieId)).thenReturn(entities);
        when(showtimeMapper.toDto(any(ShowtimeEntity.class))).thenAnswer(invocation -> {
            ShowtimeEntity entity = invocation.getArgument(0);
            return expectedDtos.stream().filter(dto -> dto.getId().equals(entity.getId())).findFirst().orElse(null);
        });

        List<Showtime> result = showtimesService.getShowtimes(movieId, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedDtos, result);
        verify(showtimeRepository, times(1)).findByMovieId(movieId);
        verify(showtimeMapper, times(2)).toDto(any(ShowtimeEntity.class));
    }

    @Test
    void getShowtimes_ByTheaterName() {
        String theaterName = "Theater 1";

        List<ShowtimeEntity> entities = List.of(
                new ShowtimeEntity(1L, null, "Theater 1", 100, null, null),
                new ShowtimeEntity(2L, null, "Theater 1", 120, null, null)
        );

        List<Showtime> expectedDtos = List.of(
                new Showtime(1L, 1L, "Theater 1", 100, null, null),
                new Showtime(2L, 1L, "Theater 1", 120, null, null)
        );

        when(showtimeRepository.findByTheaterIgnoreCase(theaterName)).thenReturn(entities);
        when(showtimeMapper.toDto(any(ShowtimeEntity.class))).thenAnswer(invocation -> {
            ShowtimeEntity entity = invocation.getArgument(0);
            return expectedDtos.stream().filter(dto -> dto.getId().equals(entity.getId())).findFirst().orElse(null);
        });

        List<Showtime> result = showtimesService.getShowtimes(null, theaterName);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedDtos, result);
        verify(showtimeRepository, times(1)).findByTheaterIgnoreCase(theaterName);
        verify(showtimeMapper, times(2)).toDto(any(ShowtimeEntity.class));

    }

    @Test
    void getShowtimeById() {
        Long showtimeId = 1L;
        ShowtimeEntity entity = new ShowtimeEntity(showtimeId, null, "Theater 1", 100, null, null);
        Showtime expectedDto = new Showtime(showtimeId, 1L, "Theater 1", 100, null, null);

        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(entity));
        when(showtimeMapper.toDto(entity)).thenReturn(expectedDto);

        Showtime result = showtimesService.getShowtimeById(showtimeId);

        assertEquals(expectedDto, result);
        verify(showtimeRepository, times(1)).findById(showtimeId);
        verify(showtimeMapper, times(1)).toDto(entity);
    }

    @Test
    void getShowtimeById_NotFound() {
        Long showtimeId = 1L;
        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> showtimesService.getShowtimeById(showtimeId));
        verify(showtimeRepository, times(1)).findById(showtimeId);
    }

    @Test
    void updateShowtime() {
        Long showtimeId = 1L;
        Showtime updatedDto = new Showtime(showtimeId, 1L, "Theater 1", 120, null, null);
        ShowtimeEntity existingEntity = new ShowtimeEntity(showtimeId, null, "Theater 1", 100, null, null);
        ShowtimeEntity updatedEntity = new ShowtimeEntity(showtimeId, null, "Theater 1", 120, null, null);
        Showtime expectedDto = new Showtime(showtimeId, 1L, "Theater 1", 120, null, null);

        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(existingEntity));
        when(showtimeRepository.save(existingEntity)).thenReturn(updatedEntity);
        when(showtimeMapper.toDto(updatedEntity)).thenReturn(expectedDto);

        Showtime result = showtimesService.updateShowtime(showtimeId, updatedDto);

        assertEquals(expectedDto, result);
        verify(showtimeMapper, times(1)).updateEntityFromDto(updatedDto, existingEntity);
        verify(showtimeRepository, times(1)).save(existingEntity);
        verify(showtimeMapper, times(1)).toDto(updatedEntity);
    }

    @Test
    void updateShowtime_NotFound() {
        Long showtimeId = 1L;
        Showtime updatedDto = new Showtime(showtimeId, 1L, "Theater 1", 120, null, null);

        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> showtimesService.getShowtimeById(showtimeId));
        verify(showtimeRepository, times(1)).findById(showtimeId);

    }

    @Test
    void deleteShowtime() {
        Long showtimeId = 1L;
        when(showtimeRepository.existsById(showtimeId)).thenReturn(true);

        Long result = showtimesService.deleteShowtime(showtimeId);

        assertEquals(showtimeId, result);
        verify(showtimeRepository, times(1)).deleteById(showtimeId);
    }

    @Test
    void deleteShowtime_NotFound() {
        Long showtimeId = 1L;
        when(showtimeRepository.existsById(showtimeId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> showtimesService.deleteShowtime(showtimeId));
        verify(showtimeRepository, times(1)).existsById(showtimeId);
    }
}