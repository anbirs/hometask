package com.example.hometask.unit.service;

import com.example.hometask.data.Movie;
import com.example.hometask.repository.MovieRepository;
import com.example.hometask.repository.entity.MovieEntity;
import com.example.hometask.service.impl.MovieServiceImpl;
import com.example.hometask.service.mapper.MovieField;
import com.example.hometask.service.mapper.MovieMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveMovie() {
        Movie dto = new Movie("Movie", "Sci-Fi", 142, "A", 2010, 1L);
        Movie expectedDto = new Movie("Movie", "Sci-Fi", 142, "A", 2010, 1L);
        MovieEntity entity = new MovieEntity(1L, "Movie", "Sci-Fi", 142, "A", 2010);

        when(movieMapper.toEntity(dto)).thenReturn(entity);
        when(movieRepository.save(entity)).thenReturn(entity);
        when(movieMapper.toDto(entity)).thenReturn(dto);

        Movie result = movieService.saveMovie(dto);

        assertEquals(expectedDto, result);
    }

    @Test
    void getAllMovies_NoDetails() {
        List<MovieEntity> entities = List.of(
                new MovieEntity(1L, "Movie", "Sci-Fi", 142, "A", 2010),
                new MovieEntity(2L, "Another Movie", "Drama", 120, "B", 2012)
        );

        List<Movie> dto = List.of(
                new Movie("Movie", "Sci-Fi", 142, "A", 2010, 1L),
                new Movie("Another Movie", "Drama", 120, "B", 2012, 2L)
        );

        List<Movie> expectedDto = List.of(
                new Movie("Movie", "Sci-Fi", 142, "A", 2010, 1L),
                new Movie("Another Movie", "Drama", 120, "B", 2012, 2L)
        );

        when(movieRepository.findAll()).thenReturn(entities);
        when(movieMapper.toDto(any(MovieEntity.class))).thenAnswer(invocation -> {
            MovieEntity entity = invocation.getArgument(0);
            return dto.stream().filter(m -> m.getId().equals(entity.getId())).findFirst().orElse(null);
        });

        List<Movie> result = movieService.getAllMovies(null);

        assertEquals(2, result.size());
        for (int i = 0; i < expectedDto.size(); i++) {
            assertEquals(expectedDto.get(i), result.get(i), "Element " + i + " not equals");
        }

        verify(movieRepository, times(1)).findAll();
        verify(movieMapper, times(2)).toDto(any(MovieEntity.class));
    }

    @Test
    void getAllMovies_Details() {
        List<MovieEntity> entities = List.of(new MovieEntity(1L, "Movie", "Sci-Fi", 142, "A", 2010));

        Movie movie = new Movie("Movie", null, null, "A", 2010, null);
        List<Movie> expectedDto = List.of(movie);

        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{"Movie", "A", 2010});

        when(movieRepository.findAll()).thenReturn(entities);
        List<MovieField> fieldsToFetch = List.of(MovieField.TITLE, MovieField.RATING, MovieField.YEAR);

        when(movieRepository.findAllMoviesByDynamicFields(fieldsToFetch)).thenReturn(results);

        MovieEntity movieEntity = new MovieEntity(null, "Movie", null, null, "A", 2010);
        when(movieMapper.toDto(movieEntity)).thenReturn(movie);

        when(objectMapper.convertValue(any(), eq(MovieEntity.class))).thenReturn(movieEntity);

        List<Movie> result = movieService.getAllMovies("title,rating,releaseYear");

        assertEquals(1, result.size());
        assertEquals(expectedDto.get(0), result.get(0));
     }

    @Test
    void getMovieById() {
        Long movieId = 1L;
        MovieEntity entity = new MovieEntity(movieId, "Movie", "Sci-Fi", 142, "A", 2010);
        Movie expectedDto = new Movie("Movie", "Sci-Fi", 142, "A", 2010, 1L);
        Movie dto = new Movie("Movie", "Sci-Fi", 142, "A", 2010, movieId);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(entity));
        when(movieMapper.toDto(entity)).thenReturn(dto);

        Movie result = movieService.getMovieById(movieId);

        assertEquals(expectedDto, result);
    }

    @Test
    void getMovieById_NotFound() {
        Long movieId = 1L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> movieService.getMovieById(movieId));
    }

    @Test
    void updateMovieById_NotFound() {
        Long movieId = 1L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> movieService.updateMovie(movieId, new Movie()));
    }

    @Test
    void deleteMovie() {
        Long movieId = 1L;
        when(movieRepository.existsById(movieId)).thenReturn(true);
        Long result = movieService.deleteMovie(movieId);
        assertEquals(movieId, result);
        verify(movieRepository, times(1)).deleteById(movieId);
    }

    @Test
    void deleteMovie_NotFound() {
        Long movieId = 1L;
        when(movieRepository.existsById(movieId)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> movieService.deleteMovie(movieId));
    }
}