package com.example.hometask.unit.service.mapper;

import com.example.hometask.data.Showtime;
import com.example.hometask.repository.ShowtimeRepository;
import com.example.hometask.repository.entity.MovieEntity;
import com.example.hometask.repository.entity.ShowtimeEntity;

import com.example.hometask.service.mapper.MovieMapper;
import com.example.hometask.service.mapper.ShowtimeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShowtimeMapperTest {

    @InjectMocks
    private ShowtimeMapper showtimeMapper = Mappers.getMapper(ShowtimeMapper.class);

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private MovieMapper movieMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToDto() {
        var start = LocalDateTime.of(2025, 2, 15, 12, 0);
        var end = LocalDateTime.of(2025, 2, 15, 14, 0);
        MovieEntity movie = new MovieEntity(3L, "Movie", "Sci-Fi", 142, "A", 2010);
        ShowtimeEntity entity = new ShowtimeEntity(1L, movie, "cinema", 100, start, end);

        Showtime expectedDto = new Showtime(1L, 3L, "cinema", 100, start, end);

        Showtime dto = showtimeMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(expectedDto, dto);
    }

    @Test
    void testToEntity() {
        var start = LocalDateTime.of(2025, 2, 15, 12, 0);
        var end = LocalDateTime.of(2025, 2, 15, 14, 0);
        Showtime dto = new Showtime(1L, 3L, "cinema", 100, start, end);

        when(movieMapper.idToEntity(3L))
                .thenReturn(new MovieEntity(3L, "Movie", "Sci-Fi", 142, "A", 2010));

        MovieEntity movie = new MovieEntity(3L, "Movie", "Sci-Fi", 142, "A", 2010);
        ShowtimeEntity expectedEntity = new ShowtimeEntity(1L, movie, "cinema", 100, start, end);

        ShowtimeEntity entity = showtimeMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(expectedEntity, entity);
    }

    @Test
    void testUpdateEntityFromDto() {
        var start = LocalDateTime.now();
        var end = LocalDateTime.now().plusHours(1);
        var startUpd = LocalDateTime.now().plusHours(1);
        var endUpd = LocalDateTime.now().plusHours(3);
        ShowtimeEntity existingEntity = new ShowtimeEntity(
                1L,
                new MovieEntity(2L, "Old Movie", "Drama", 120, "B", 2020),
                "Cinema 1",
                50,
                start,
                end
        );

        Showtime dto = new Showtime(
                null,
                3L,
                "Cinema 2",
                150,
                startUpd,
                endUpd
        );

        ShowtimeEntity expectedEntity = new ShowtimeEntity(
                1L,
                new MovieEntity(3L, "New Movie", "Thriller", 140, "C", 2022),
                "Cinema 2",
                150,
                startUpd,
                endUpd
        );

        when(movieMapper.idToEntity(3L))
                .thenReturn(new MovieEntity(3L, "New Movie", "Thriller", 140, "C", 2022));

        showtimeMapper.updateEntityFromDto(dto, existingEntity);

        assertEquals(expectedEntity, existingEntity);
    }

    @Test
    void testIdToEntity() {
        var start = LocalDateTime.now();
        var end = LocalDateTime.now().plusHours(2);
        Long showtimeId = 4L;
        ShowtimeEntity entity = new ShowtimeEntity(showtimeId, null, "cinema", 100, start, end);
        ShowtimeEntity expectedEntity = new ShowtimeEntity(showtimeId, null, "cinema", 100, start, end);

        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(entity));

        ShowtimeEntity result = showtimeMapper.idToEntity(showtimeId);

        assertNotNull(result);
        assertEquals(expectedEntity, result);
    }

    @Test
    void testIdToEntity_NotFound() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> showtimeMapper.idToEntity(1L));
    }
}
