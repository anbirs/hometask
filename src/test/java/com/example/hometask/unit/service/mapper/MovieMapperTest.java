package com.example.hometask.unit.service.mapper;

import com.example.hometask.data.Movie;
import com.example.hometask.repository.MovieRepository;
import com.example.hometask.repository.entity.MovieEntity;
import com.example.hometask.service.mapper.MovieMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieMapperTest {

    @InjectMocks
    private MovieMapper movieMapper = Mappers.getMapper(MovieMapper.class);

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToDto() {
        MovieEntity entity = new MovieEntity(1L, "Movie", "Sci-Fi", 142, "A", 2010);
        Movie expectedDto = new Movie("Movie", "Sci-Fi", 142, "A", 2010, 1L);

        Movie dto = movieMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(expectedDto, dto);
    }

    @Test
    void testToEntity() {
        Movie dto = new Movie("Movie", "Sci-Fi", 142, "A", 2010, 1L);
        MovieEntity expectedEntity = new MovieEntity(1L, "Movie", "Sci-Fi", 142, "A", 2010);

        MovieEntity entity = movieMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(expectedEntity, entity);
    }

    @Test
    void testUpdateEntityFromDto() {
        MovieEntity existingEntity = new MovieEntity(1L, "Old Title", "Old Genre", 100, "A", 2000);
        Movie dto = new Movie("New Title", "New Genre", 101,  "B", 2022, null);
        MovieEntity expectedEntity = new MovieEntity(1L, "New Title", "New Genre", 101,  "B", 2022);

        movieMapper.updateEntityFromDto(dto, existingEntity);

        assertEquals(expectedEntity, existingEntity);
    }

    @Test
    void testIdToEntity() {
        Long movieId = 1L;
        MovieEntity entity = new MovieEntity(movieId, "Movie", "Sci-Fi", 142, "A", 2010);
        MovieEntity expectedEntity = new MovieEntity(movieId, "Movie", "Sci-Fi", 142, "A", 2010);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(entity));

        MovieEntity result = movieMapper.idToEntity(movieId);

        assertNotNull(result);
        assertEquals(expectedEntity, result);
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void testIdToEntity_NotFound() {
        Long movieId = 1L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> movieMapper.idToEntity(movieId));
    }
}
