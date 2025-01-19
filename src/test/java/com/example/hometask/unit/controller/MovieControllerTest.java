package com.example.hometask.unit.controller;

import com.example.hometask.controller.ApiExceptionHandler;
import com.example.hometask.controller.impl.MovieControllerImpl;
import com.example.hometask.data.Movie;
import com.example.hometask.service.MovieService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieControllerImpl movieController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void getMovies() throws Exception {
        List<Movie> responseMovies = List.of(
                new Movie("First", "Comedy", 178, "R", 2000, 8L),
                new Movie("3", "Comedy", 160, "R", 2000, 9L)
        );

        String resultString = "{\"data\":[{\"title\":\"First\",\"genre\":\"Comedy\",\"duration\":178,\"rating\":\"R\",\"releaseYear\":2000,\"id\":8}," +
                "{\"title\":\"3\",\"genre\":\"Comedy\",\"duration\":160,\"rating\":\"R\",\"releaseYear\":2000,\"id\":9}]}";

        when(movieService.getAllMovies(null)).thenReturn(responseMovies);

        MockHttpServletResponse response = mockMvc.perform(get("/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(resultString);

        verify(movieService, times(1)).getAllMovies(null);
    }

    @Test
    void getMoviesDetails() throws Exception {
        List<Movie> responseMovies = List.of(
                new Movie("First", null, null, null, 2000, null),
                new Movie("3", null, null, null, 2000, null)
        );

        String resultString = "{\"data\":[{\"title\":\"First\",\"releaseYear\":2000}," +
                "{\"title\":\"3\",\"releaseYear\":2000}]}";

        when(movieService.getAllMovies("title,releaseYear")).thenReturn(responseMovies);

        MockHttpServletResponse response = mockMvc.perform(get("/v1/movies?details=title,releaseYear")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(resultString);

        verify(movieService, times(1)).getAllMovies("title,releaseYear");
    }

    @Test
    void getMovieById() throws Exception {
        Long id = 8L;
        Movie resp = new Movie("First", "Comedy", 178, "R", 2000, id);
        String resultString = "{\"data\":{\"title\":\"First\",\"genre\":\"Comedy\",\"duration\":178,\"rating\":\"R\",\"releaseYear\":2000,\"id\":8}}";

        when(movieService.getMovieById(id)).thenReturn(resp);

        MockHttpServletResponse response = mockMvc.perform(get("/v1/movies/8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(resultString);

        verify(movieService, times(1)).getMovieById(id);
    }

    @Test
    void getMovieByIdNotFound() throws Exception {

        when(movieService.getMovieById(1L)).thenThrow(new EntityNotFoundException("No data"));

        MockHttpServletResponse response = mockMvc.perform(get("/v1/movies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"error\":\"No data\"}");

        verify(movieService, times(1)).getMovieById(1L);
    }

    @Test
    void addMovie() throws Exception {
        Movie req = new Movie("First", "Comedy", 178, "R", 2000, null);

        MockHttpServletResponse response = mockMvc.perform(
                post("/v1/movies").contentType(MediaType.APPLICATION_JSON).content(
                        "{\n" +
                                "    \"title\": \"First\",\n" +
                                "    \"genre\": \"Comedy\",\n" +
                                "    \"duration\": 178,\n" +
                                "    \"rating\": \"R\",\n" +
                                "    \"releaseYear\": 2000\n" +
                                "}"
                )).andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(movieService, times(1)).saveMovie(req);
    }

    @Test
    void updateMovie() throws Exception {
        Movie req = new Movie("First", "Comedy", 178, "R", 2000, null);
        MockHttpServletResponse response = mockMvc.perform(
                put("/v1/movies/1").contentType(MediaType.APPLICATION_JSON).content(
                        "{\n" +
                                "    \"title\": \"First\",\n" +
                                "    \"genre\": \"Comedy\",\n" +
                                "    \"duration\": 178,\n" +
                                "    \"rating\": \"R\",\n" +
                                "    \"releaseYear\": 2000\n" +
                                "}"
                )).andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(movieService, times(1)).updateMovie(1L, req);
    }

    @Test
    void deleteMovie() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                delete("/v1/movies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(movieService, times(1)).deleteMovie(1L);
    }
}