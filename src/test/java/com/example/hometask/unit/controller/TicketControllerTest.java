package com.example.hometask.unit.controller;

import com.example.hometask.controller.impl.TicketControllerImpl;
import com.example.hometask.data.Ticket;
import com.example.hometask.service.TicketService;
import org.junit.jupiter.api.Test;
import com.example.hometask.controller.ApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketControllerImpl ticketController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void getAllTickets() throws Exception {
        List<Ticket> tickets = List.of(
                new Ticket(1L, 1L, 1L, "A1", BigDecimal.valueOf(100)),
                new Ticket(2L, 1L, 1L, "A2", BigDecimal.valueOf(100))
        );

        when(ticketService.getAllTickets()).thenReturn(tickets);

        MockHttpServletResponse response = mockMvc.perform(get("/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String expectedResponse = "{\"data\":[{\"id\":1,\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A1\",\"price\":100}," +
                "{\"id\":2,\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A2\",\"price\":100}]}";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);

        verify(ticketService, times(1)).getAllTickets();
    }

    @Test
    void getTicketsByUser() throws Exception {
        Long userId = 1L;
        List<Ticket> tickets = List.of(
                new Ticket(1L, userId, 1L, "A1", BigDecimal.valueOf(100)),
                new Ticket(2L, userId, 1L, "A2", BigDecimal.valueOf(100))
        );

        when(ticketService.getAllTicketsByUser(userId)).thenReturn(tickets);

        MockHttpServletResponse response = mockMvc.perform(get("/v1/tickets/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String expectedResponse = "{\"data\":[{\"id\":1,\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A1\",\"price\":100}," +
                "{\"id\":2,\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A2\",\"price\":100}]}";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);

        verify(ticketService, times(1)).getAllTicketsByUser(userId);
    }

    @Test
    void getTicketsByShowtime() throws Exception {
        Long showtimeId = 1L;
        List<Ticket> tickets = List.of(
                new Ticket(1L, 1L, showtimeId, "A1", BigDecimal.valueOf(100)),
                new Ticket(2L, 1L, showtimeId, "A2", BigDecimal.valueOf(100))
        );

        when(ticketService.getAllTicketsByShowtime(showtimeId)).thenReturn(tickets);

        MockHttpServletResponse response = mockMvc.perform(get("/v1/tickets/showtime/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String expectedResponse = "{\"data\":[{\"id\":1,\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A1\",\"price\":100}," +
                "{\"id\":2,\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A2\",\"price\":100}]}";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);

        verify(ticketService, times(1)).getAllTicketsByShowtime(showtimeId);
    }

    @Test
    void getTicketById() throws Exception {
        Long ticketId = 1L;
        Ticket ticket = new Ticket(ticketId, 1L, 1L, "A1", BigDecimal.valueOf(100));

        when(ticketService.findTicketById(ticketId)).thenReturn(ticket);

        MockHttpServletResponse response = mockMvc.perform(get("/v1/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String expectedResponse = "{\"data\":{\"id\":1,\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A1\",\"price\":100}}";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);

        verify(ticketService, times(1)).findTicketById(ticketId);
    }

    @Test
    void addTicket() throws Exception {
        Ticket ticket = new Ticket(null, 1L, 1L, "A1", BigDecimal.valueOf(100));
        Ticket savedTicket = new Ticket(1L, 1L, 1L, "A1", BigDecimal.valueOf(100));

        when(ticketService.bookTicket(ticket)).thenReturn(savedTicket);

        MockHttpServletResponse response = mockMvc.perform(post("/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A1\",\"price\":100}"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String expectedResponse = "{\"data\":{\"id\":1,\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A1\",\"price\":100}}";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);

        verify(ticketService, times(1)).bookTicket(ticket);
    }

    @Test
    void updateTicket() throws Exception {
        Long ticketId = 1L;
        Ticket updatedTicket = new Ticket(ticketId, 1L, 1L, "A2", BigDecimal.valueOf(120));

        when(ticketService.updateTicket(ticketId, updatedTicket)).thenReturn(updatedTicket);

        MockHttpServletResponse response = mockMvc.perform(put("/v1/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A2\",\"price\":120}"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String expectedResponse = "{\"data\":{\"id\":1,\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A2\",\"price\":120}}";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);

        verify(ticketService, times(1)).updateTicket(ticketId, updatedTicket);
    }

    @Test
    void deleteTicket() throws Exception {
        Long ticketId = 1L;

        when(ticketService.deleteTicket(ticketId)).thenReturn(ticketId);

        MockHttpServletResponse response = mockMvc.perform(delete("/v1/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String expectedResponse = "{\"data\":1}";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);

        verify(ticketService, times(1)).deleteTicket(ticketId);
    }
}
