package com.example.hometask.unit.controller;

import com.example.hometask.controller.ApiExceptionHandler;
import com.example.hometask.controller.impl.ShowtimeControllerImpl;
import com.example.hometask.data.Showtime;
import com.example.hometask.service.ShowtimesService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ShowtimeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShowtimesService showtimesService;

    @InjectMocks
    private ShowtimeControllerImpl showtimeController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(showtimeController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void getShowtimes() throws Exception {
        var start = LocalDateTime.of(2025, 2, 15, 12, 0);
        var end = LocalDateTime.of(2025, 2, 15, 14, 0);

        List<Showtime> responseShowtimes = List.of(
                new Showtime(1L, 2L, "theater", 10, start, end),
                new Showtime(2L, 3L, "theater1", 10, start, end)
        );

        String resultString = "{\"data\":[{\"id\":1,\"movieId\":2,\"theater\":\"theater\",\"maxSeats\":10,\"startTime\":[2025,2,15,12,0],\"endTime\":[2025,2,15,14,0]}," +
                "{\"id\":2,\"movieId\":3,\"theater\":\"theater1\",\"maxSeats\":10,\"startTime\":[2025,2,15,12,0],\"endTime\":[2025,2,15,14,0]}]}";

        when(showtimesService.getShowtimes(null, null)).thenReturn(responseShowtimes);

        MockHttpServletResponse response = mockMvc.perform(get("/v1/showtimes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(resultString);

        verify(showtimesService, times(1)).getShowtimes(null, null);
    }

    @Test
    void getShowtimeById() throws Exception {
        Long id = 1L;
        var start = LocalDateTime.of(2025, 2, 15, 12, 0);
        var end = LocalDateTime.of(2025, 2, 15, 14, 0);

        Showtime responseShowtime = new Showtime(1L, 2L, "theater", 10, start, end);
        String resultString = "{\"data\":{\"id\":1,\"movieId\":2,\"theater\":\"theater\",\"maxSeats\":10,\"startTime\":[2025,2,15,12,0],\"endTime\":[2025,2,15,14,0]}}";

        when(showtimesService.getShowtimeById(id)).thenReturn(responseShowtime);

        MockHttpServletResponse response = mockMvc.perform(get("/v1/showtimes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(resultString);

        verify(showtimesService, times(1)).getShowtimeById(id);
    }

    @Test
    void addShowtime() throws Exception {
        var start = LocalDateTime.of(2025, 2, 15, 12, 0);
        var end = LocalDateTime.of(2025, 2, 15, 14, 0);
        Showtime req = new Showtime(null, 2L, "Movies", 10, start, end);

        MockHttpServletResponse response = mockMvc.perform(
                post("/v1/showtimes").contentType(MediaType.APPLICATION_JSON).content(
                        "{\n" +
                                "    \"movieId\": 2,\n" +
                                "    \"theater\": \"Movies\",\n" +
                                "    \"maxSeats\": 10,\n" +
                                "    \"startTime\": \"2025-02-15T12:00:00\",\n" +
                                "    \"endTime\": \"2025-02-15T14:00:00\"\n" +
                                "}"
                )).andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(showtimesService, times(1)).saveShowtime(req);
    }

    @Test
    void updateShowtime() throws Exception {
        var start = LocalDateTime.of(2025, 2, 15, 12, 0);
        var end = LocalDateTime.of(2025, 2, 15, 14, 0);
        Showtime req = new Showtime(null, 2L, "Movies", 10, start, end);

        MockHttpServletResponse response = mockMvc.perform(
                put("/v1/showtimes/1").contentType(MediaType.APPLICATION_JSON).content(
                        "{\n" +
                                "    \"movieId\": 2,\n" +
                                "    \"theater\": \"Movies\",\n" +
                                "    \"maxSeats\": 10,\n" +
                                "    \"startTime\": \"2025-02-15T12:00:00\",\n" +
                                "    \"endTime\": \"2025-02-15T14:00:00\"\n" +
                                "}"
                )).andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(showtimesService, times(1)).updateShowtime(1L, req);
    }

    @Test
    void deleteShowtime() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        delete("/v1/showtimes/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(showtimesService, times(1)).deleteShowtime(1L);
    }
}