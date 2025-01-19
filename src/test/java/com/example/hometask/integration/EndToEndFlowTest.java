package com.example.hometask.integration;


import com.example.hometask.config.JwtTokenProvider;
import com.example.hometask.data.ApiResponse;
import com.example.hometask.data.Movie;
import com.example.hometask.data.Showtime;
import com.example.hometask.data.Ticket;
import com.example.hometask.repository.MovieRepository;
import com.example.hometask.repository.ShowtimeRepository;
import com.example.hometask.repository.TicketRepository;
import com.example.hometask.repository.UserRepository;
import com.example.hometask.repository.entity.Role;
import com.example.hometask.repository.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class EndToEndFlowTest {

    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper().findAndRegisterModules();

    private HttpHeaders adminHeaders;
    private HttpHeaders customerHeaders;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
        showtimeRepository.deleteAll();
        ticketRepository.deleteAll();

        // Add admin user
        UserEntity adminUser = new UserEntity(
                null,
                "admin",
                "admin@example.com",
                Role.ROLE_ADMIN,
                passwordEncoder.encode("password")
        );
        userRepository.save(adminUser);

        // Add customer user
        UserEntity customerUser = new UserEntity(
                null,
                "customer",
                "customer@example.com",
                Role.ROLE_CUSTOMER,
                passwordEncoder.encode("password")
        );
        userRepository.save(customerUser);

        // Authenticate admin user
        Authentication adminAuthentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        "admin", // Username
                        "password" // Raw password
                )
        );

        // Generate token for admin
        String adminToken = jwtTokenProvider.generateToken(adminAuthentication);

        adminHeaders = new HttpHeaders();
        adminHeaders.setBearerAuth(adminToken);

        Authentication customerAuthentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        "customer", "password"
                )
        );
        String customerToken = jwtTokenProvider.generateToken(customerAuthentication);

        customerHeaders = new HttpHeaders();
        customerHeaders.setBearerAuth(customerToken);

    }

    @Test
    void testEndToEndFlow() {
        Movie movieRequest = new Movie("Inception", "Sci-Fi", 148, "PG-13", 2010, null);
        HttpEntity<Movie> requestMovies = new HttpEntity<>(movieRequest, adminHeaders);
        ResponseEntity<ApiResponse> movieResponse = restTemplate.exchange(
                "/v1/movies", HttpMethod.POST, requestMovies, new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, movieResponse.getStatusCode());
        Long movieId = extractMovieId(movieResponse);

        Showtime showtimeRequest = new Showtime(null, movieId, "Theater 1", 100, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        HttpEntity<Showtime> requesShowtimes = new HttpEntity<>(showtimeRequest, adminHeaders);
        ResponseEntity<ApiResponse> showtimeResponse = restTemplate.exchange(
                "/v1/showtimes", HttpMethod.POST, requesShowtimes, new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, showtimeResponse.getStatusCode());
        Long showtimeId = extractShowtimeId(showtimeResponse);

        Ticket ticketRequest = new Ticket(null, null, showtimeId, "A1", BigDecimal.valueOf(10.0));
        HttpEntity<Ticket> requestTickets = new HttpEntity<>(ticketRequest, customerHeaders);
        ResponseEntity<ApiResponse> ticketResponse = restTemplate.exchange(
                "/v1/tickets", HttpMethod.POST, requestTickets, new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, ticketResponse.getStatusCode());

        HttpEntity<Ticket> requestTicketsList = new HttpEntity<>(customerHeaders);

        ResponseEntity<ApiResponse<List<Ticket>>> allTicketsResponse = restTemplate.exchange(
                "/v1/tickets/showtime/" + showtimeId, HttpMethod.GET, requestTicketsList,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, allTicketsResponse.getStatusCode());
        assertEquals(1, Objects.requireNonNull(allTicketsResponse.getBody()).getData().size());
    }

    private Long extractMovieId(ResponseEntity<ApiResponse> response) {
        LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) response.getBody().getData();
        Movie movie = objectMapper.convertValue(dataMap, Movie.class);
        return movie.getId();
    }

    private Long extractShowtimeId(ResponseEntity<ApiResponse> response) {
         LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) response.getBody().getData();
        Showtime showtime = objectMapper.convertValue(dataMap, Showtime.class);
        return showtime.getId();
    }
}
