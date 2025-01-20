package com.example.hometask.integration;


import com.example.hometask.config.JwtTokenProvider;
import com.example.hometask.data.*;
import com.example.hometask.repository.MovieRepository;
import com.example.hometask.repository.ShowtimeRepository;
import com.example.hometask.repository.TicketRepository;
import com.example.hometask.repository.UserRepository;
import com.example.hometask.repository.entity.*;
import com.example.hometask.service.MovieService;
import com.example.hometask.service.ShowtimesService;
import com.example.hometask.service.TicketService;
import com.example.hometask.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

import static org.junit.jupiter.api.Assertions.*;

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
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShowtimesService showtimesService;

    @Autowired
    private TicketService ticketService;

    @PersistenceContext
    private EntityManager entityManager;

    private ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper().findAndRegisterModules();

    private HttpHeaders adminHeaders;
    private HttpHeaders customerHeaders;

    @BeforeEach
    void setUp() {
        List<MovieEntity> movies = movieRepository.findAll();
        movies.forEach(e -> movieService.deleteMovie(e.getId()));
        List<TicketEntity> tickets = ticketRepository.findAll();
        tickets.forEach(ticket -> ticketService.deleteTicket(ticket.getId()));
        List<UserEntity> users = userRepository.findAll();
        users.forEach(user -> userService.deleteUser(user.getId()));
        List<ShowtimeEntity> showtimes = showtimeRepository.findAll();
        showtimes.forEach(showtime -> showtimesService.deleteShowtime(showtime.getId()));


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
        Movie movieRequest =  new Movie("Test Movie 1", "Abc", 123, "KKK", 2020, null);
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

        HttpEntity<Ticket> requestTicketsList = new HttpEntity<>(adminHeaders);

        ResponseEntity<ApiResponse<List<Ticket>>> allTicketsResponse = restTemplate.exchange(
                "/v1/tickets/showtime/" + showtimeId, HttpMethod.GET, requestTicketsList,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, allTicketsResponse.getStatusCode());
        assertEquals(1, Objects.requireNonNull(allTicketsResponse.getBody()).getData().size());
    }

    @Test
    void testMovieManagement() {
        Movie movie1 = new Movie("Test Movie 1", "Abc", 123, "KKK", 2020, null);
        Movie movie2 = new Movie("Movie 2", "Cd", 132, "AAA", 1974, null);
        Movie movie3 = new Movie("Superman", "lll", 190, "UUU", 2025, null);

        HttpEntity<Movie> movieRequest1 = new HttpEntity<>(movie1, adminHeaders);
        HttpEntity<Movie> movieRequest2 = new HttpEntity<>(movie2, adminHeaders);
        HttpEntity<Movie> movieRequest3 = new HttpEntity<>(movie3, adminHeaders);
        Long idToUpdate1 = extractMovieId(restTemplate.exchange(
                "/v1/movies", HttpMethod.POST, movieRequest1, new ParameterizedTypeReference<>() {}
        ));
        Long idToUpdate2 = extractMovieId(restTemplate.exchange(
                "/v1/movies", HttpMethod.POST, movieRequest2, new ParameterizedTypeReference<>() {}
        ));
        Long idToUpdate3 = extractMovieId(restTemplate.exchange(
                "/v1/movies", HttpMethod.POST, movieRequest3, new ParameterizedTypeReference<>() {}
        ));

        HttpEntity requestMoviesList = new HttpEntity<>(customerHeaders);

        ResponseEntity<ApiResponse<List<Movie>>> moviesResponse = restTemplate.exchange(
                "/v1/movies", HttpMethod.GET, requestMoviesList,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, moviesResponse.getStatusCode());
        assertEquals(3, Objects.requireNonNull(moviesResponse.getBody()).getData().size());

        ResponseEntity<ApiResponse<List<Movie>>> moviesDetailsResponse = restTemplate.exchange(
                "/v1/movies?details=title,genre,duration", HttpMethod.GET, requestMoviesList,
                new ParameterizedTypeReference<>() {}
        );

        List<Movie> movieDetails = List.of(new Movie("Test Movie 1", "Abc", 123, null, null, null),
                    new Movie("Movie 2", "Cd", 132, null, null, null),
                    new Movie("Superman", "lll", 190, null, null, null));


        assertEquals(HttpStatus.OK, moviesDetailsResponse.getStatusCode());
        List<Movie> respData = Objects.requireNonNull(moviesDetailsResponse.getBody()).getData();
        assertEquals(3, respData.size());
        assertEquals(movieDetails, respData);

        Movie movieUpdateDto = new Movie("Updated Movie 1", "ddd", 200, "ttt", 2000, null);
        HttpEntity<Movie> movieUpdate = new HttpEntity<>(movieUpdateDto, adminHeaders);
        ResponseEntity<ApiResponse> movieUpdateResponse = restTemplate.exchange(
                "/v1/movies/" + idToUpdate1, HttpMethod.PUT, movieUpdate, new ParameterizedTypeReference<>() {}
        );

        movieUpdateDto.setId(extractMovieId(movieUpdateResponse));
        assertEquals(HttpStatus.OK, moviesDetailsResponse.getStatusCode());
        Movie movieUpdateResponseData = extractMovie(movieUpdateResponse);

        assertEquals(movieUpdateDto, movieUpdateResponseData);

        // delete
        ResponseEntity<ApiResponse<Long>> deleteResponse = restTemplate.exchange(
                "/v1/movies/" + idToUpdate3, HttpMethod.DELETE, requestMoviesList,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.FORBIDDEN, deleteResponse.getStatusCode());

        restTemplate.exchange(
                "/v1/movies/" + idToUpdate3, HttpMethod.DELETE,  new HttpEntity<>(adminHeaders),
                new ParameterizedTypeReference<>() {}
        );

        // get size == 2
        ResponseEntity<ApiResponse<List<Movie>>> moviesReducedResponse = restTemplate.exchange(
                "/v1/movies", HttpMethod.GET, requestMoviesList,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, moviesReducedResponse.getStatusCode());
        assertEquals(2, Objects.requireNonNull(moviesReducedResponse.getBody()).getData().size());

        ResponseEntity<ApiResponse> movieResponse = restTemplate.exchange(
                "/v1/movies/" + idToUpdate2, HttpMethod.GET, requestMoviesList,
                new ParameterizedTypeReference<>() {}
        );
        movie2.setId(extractMovieId(movieResponse));
        assertEquals(HttpStatus.OK, moviesReducedResponse.getStatusCode());
        assertEquals(movie2, extractMovie(movieResponse));

    }

    @Test
    void testShowtimeManagement() {
        // create movies: admin
        Movie movie1 = new Movie("Test Movie 1", "Abc", 123, "KKK", 2020, null);
        Movie movie2 = new Movie("Movie 2", "Cd", 132, "AAA", 1974, null);
        Movie movie3 = new Movie("Superman", "lll", 190, "UUU", 2025, null);

        HttpEntity<Movie> movieRequest1 = new HttpEntity<>(movie1, adminHeaders);
        HttpEntity<Movie> movieRequest2 = new HttpEntity<>(movie2, adminHeaders);
        HttpEntity<Movie> movieRequest3 = new HttpEntity<>(movie3, adminHeaders);
        HttpEntity requestGetAsAdmin = new HttpEntity<>(adminHeaders);
        Long movie1Id = extractMovieId(restTemplate.exchange(
                "/v1/movies", HttpMethod.POST, movieRequest1, new ParameterizedTypeReference<>() {}
        ));
        Long movie2Id = extractMovieId(restTemplate.exchange(
                "/v1/movies", HttpMethod.POST, movieRequest2, new ParameterizedTypeReference<>() {}
        ));
        Long movie3Id = extractMovieId(restTemplate.exchange(
                "/v1/movies", HttpMethod.POST, movieRequest3, new ParameterizedTypeReference<>() {}
        ));

        // create showtime: admin
        var start = LocalDateTime.now().plusHours(24);
        var end = LocalDateTime.now().plusHours(26);
        Showtime showtimeRequest = new Showtime(null, movie1Id, "Theater 1", 100, start, end);
        HttpEntity<Showtime> postShowtimes = new HttpEntity<>(showtimeRequest, adminHeaders);
        ResponseEntity<ApiResponse> showtimeResponse = restTemplate.exchange(
                "/v1/showtimes", HttpMethod.POST, postShowtimes, new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, showtimeResponse.getStatusCode());

        // create overlapping showtime: admin
        Showtime showtimeOverlappingRequest = new Showtime(null, movie1Id, "Theater 1", 100, start, end);
        ResponseEntity<ApiResponse> showtimeOverlappingResponse = restTemplate.exchange(
                "/v1/showtimes", HttpMethod.POST, new HttpEntity<>(showtimeOverlappingRequest, adminHeaders), new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.BAD_REQUEST, showtimeOverlappingResponse.getStatusCode());

        // create more stowtimes: admin
        restTemplate.exchange(
                "/v1/showtimes", HttpMethod.POST,
                new HttpEntity<>(new Showtime(null, movie2Id, "Theater 1", 100, start.plusHours(4), end.plusHours(6)), adminHeaders), new ParameterizedTypeReference<>() {}
        );

        restTemplate.exchange(
                "/v1/showtimes", HttpMethod.POST,
                new HttpEntity<>(new Showtime(null, movie1Id, "Theater 2", 100, start, end), adminHeaders), new ParameterizedTypeReference<>() {}
        );

        restTemplate.exchange(
                "/v1/showtimes", HttpMethod.POST,
                new HttpEntity<>(new Showtime(null, movie2Id, "Theater 3", 100, start, end), adminHeaders), new ParameterizedTypeReference<>() {}
        );

        // get all check size: customer
        HttpEntity requestGetAsCustomer = new HttpEntity<>(customerHeaders);

        ResponseEntity<ApiResponse<List<Showtime>>> stResponse = restTemplate.exchange(
                "/v1/showtimes", HttpMethod.GET, requestGetAsCustomer,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, stResponse.getStatusCode());
        assertEquals(4, Objects.requireNonNull(stResponse.getBody()).getData().size());

        // update showtime: admin
        Long idToUpdate = extractShowtimeId(showtimeResponse);
        ResponseEntity<ApiResponse> updateShowTimeResponse = restTemplate.exchange(
                "/v1/showtimes/" + idToUpdate, HttpMethod.PUT,
                new HttpEntity<>(new Showtime(null, movie3Id, "Theater 3", 100, start.plusHours(4), end.plusHours(6)), adminHeaders), new ParameterizedTypeReference<>() {}
        );

        Showtime expectedShowtime = new Showtime(extractShowtimeId(updateShowTimeResponse), movie3Id, "Theater 3", 100, start.plusHours(4), end.plusHours(6));

        assertEquals(HttpStatus.OK, updateShowTimeResponse.getStatusCode());
        assertEquals(expectedShowtime, extractShowtime(updateShowTimeResponse));

        // get not existing showtime: customer
        ResponseEntity<ApiResponse> noStResponse = restTemplate.exchange(
                "/v1/showtimes/100", HttpMethod.GET, requestGetAsCustomer,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.NO_CONTENT, noStResponse.getStatusCode());

        // get showtimes by movie: customer
        ResponseEntity<ApiResponse<List<Showtime>>> stByMovieResponse = restTemplate.exchange(
                "/v1/showtimes?movieId=" + movie3Id, HttpMethod.GET, requestGetAsCustomer,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, stByMovieResponse.getStatusCode());
        assertEquals(1, Objects.requireNonNull(stByMovieResponse.getBody()).getData().size());

        // get showtimes by theater: admin
        ResponseEntity<ApiResponse<List<Showtime>>> stByTheaterResponse = restTemplate.exchange(
                "/v1/showtimes?theaterName=Theater 3", HttpMethod.GET, requestGetAsAdmin,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, stByTheaterResponse.getStatusCode());
        assertEquals(2, Objects.requireNonNull(stByTheaterResponse.getBody()).getData().size());

        // delete showtime: admin
        ResponseEntity<ApiResponse<Long>> deleteResponse = restTemplate.exchange(
                "/v1/showtimes/" + idToUpdate, HttpMethod.DELETE, requestGetAsAdmin,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<ApiResponse<List<Showtime>>> getAllResponse = restTemplate.exchange(
                "/v1/showtimes", HttpMethod.GET, requestGetAsAdmin,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        assertEquals(3, Objects.requireNonNull(getAllResponse.getBody()).getData().size());

    }


    @Test
    void testUserManagement() {
        // create admin: admin
        User adminUser = new User("AdminUser", "admin@example.com", "password", "ROLE_ADMIN", null);
        HttpEntity<User> adminUserRequest = new HttpEntity<>(adminUser, adminHeaders);

        ResponseEntity<ApiResponse<Long>> adminResponse = restTemplate.exchange(
                "/v1/users/register/admin", HttpMethod.POST, adminUserRequest, new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, adminResponse.getStatusCode());
        Long adminUserId = Objects.requireNonNull(adminResponse.getBody()).getData();

        // create customer: no auth
        User customerUser = new User("CustomerUser", "customer@example.com", "password", "ROLE_CUSTOMER", null);
        HttpEntity<User> customerUserRequest = new HttpEntity<>(customerUser);

        ResponseEntity<ApiResponse<Long>> customerResponse = restTemplate.exchange(
                "/v1/users/register/customer", HttpMethod.POST, customerUserRequest, new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, customerResponse.getStatusCode());
        Long customerUserId = Objects.requireNonNull(customerResponse.getBody()).getData();

        // get all users: admin
        ResponseEntity<ApiResponse<List<User>>> adminGetAllUsersResponse = restTemplate.exchange(
                "/v1/users", HttpMethod.GET, new HttpEntity<>(adminHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, adminGetAllUsersResponse.getStatusCode());
        assertTrue(Objects.requireNonNull(adminGetAllUsersResponse.getBody()).getData().size() >= 2); // At least admin + customer

        // get all users: customer
        ResponseEntity<ApiResponse<List<User>>> customerGetAllUsersResponse = restTemplate.exchange(
                "/v1/users", HttpMethod.GET, new HttpEntity<>(customerHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.FORBIDDEN, customerGetAllUsersResponse.getStatusCode()); // Assuming "ROLE_CUSTOMER" cannot view all users

        // delete user: admin
        ResponseEntity<ApiResponse<Long>> deleteCustomerResponse = restTemplate.exchange(
                "/v1/users/" + customerUserId, HttpMethod.DELETE, new HttpEntity<>(adminHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, deleteCustomerResponse.getStatusCode());
        assertEquals(customerUserId, Objects.requireNonNull(deleteCustomerResponse.getBody()).getData());

        // verify deletion
        ResponseEntity<ApiResponse<List<User>>> getAllUsersAfterDeletionResponse = restTemplate.exchange(
                "/v1/users", HttpMethod.GET, new HttpEntity<>(adminHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, getAllUsersAfterDeletionResponse.getStatusCode());
        assertFalse(Objects.requireNonNull(getAllUsersAfterDeletionResponse.getBody()).getData()
                .stream().anyMatch(user -> user.getId().equals(customerUserId)));
    }


    @Test
    void testTicketManagement() {
        Long movieId = extractMovieId(restTemplate.exchange(
                "/v1/movies", HttpMethod.POST, new HttpEntity<>(new Movie("Newest", "dsff", 190, "ere", 2025, null), adminHeaders), new ParameterizedTypeReference<>() {}
        ));
        Long showtime1Id = extractShowtimeId(restTemplate.exchange(
                "/v1/showtimes", HttpMethod.POST,
                new HttpEntity<>(new Showtime(null, movieId, "Theater 1", 100, LocalDateTime.now().plusHours(46), LocalDateTime.now().plusHours(48)), adminHeaders), new ParameterizedTypeReference<>() {}
        ));


        User customerUser = new User("CustomerU", "hfgh", "password", "ROLE_CUSTOMER", null);
        HttpEntity<User> customerUserRequest = new HttpEntity<>(customerUser);

        ResponseEntity<ApiResponse> customerResponse = restTemplate.exchange(
                "/v1/users/register/customer", HttpMethod.POST, customerUserRequest, new ParameterizedTypeReference<>() {}
        );

        User adminUser = new User("Admin888", "8989@example.com", "password", "ROLE_ADMIN", null);
        HttpEntity<User> adminUserRequest = new HttpEntity<>(adminUser, adminHeaders);

        ResponseEntity<ApiResponse<Long>> adminResponse = restTemplate.exchange(
                "/v1/users/register/admin", HttpMethod.POST, adminUserRequest, new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, adminResponse.getStatusCode());
        Long adminUserId = Objects.requireNonNull(adminResponse.getBody()).getData();


        // create ticket: customer
        Long customerId = Long.valueOf((Integer)customerResponse.getBody().getData());
        Ticket customerTicket = new Ticket(null, customerId, showtime1Id, "A1", BigDecimal.valueOf(10.0));
        HttpEntity<Ticket> customerTicketRequest = new HttpEntity<>(customerTicket, customerHeaders);

        ResponseEntity<ApiResponse> customerTicketResponse = restTemplate.exchange(
                "/v1/tickets", HttpMethod.POST, customerTicketRequest, new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, customerTicketResponse.getStatusCode());
        Long customerTicketId = extracTicketrId(customerTicketResponse);

        // create ticket: admin
        Ticket adminTicket = new Ticket(null, adminUserId, showtime1Id, "B1", BigDecimal.valueOf(15.0));
        HttpEntity<Ticket> adminTicketRequest = new HttpEntity<>(adminTicket, adminHeaders);

        ResponseEntity<ApiResponse<Long>> adminTicketResponse = restTemplate.exchange(
                "/v1/tickets", HttpMethod.POST, adminTicketRequest, new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.FORBIDDEN, adminTicketResponse.getStatusCode());

        // create invalid ticket: customer (duplicate seat)
        Ticket invalidTicket = new Ticket(null,  customerId, showtime1Id, "A1", BigDecimal.valueOf(12.0));
        ResponseEntity<ApiResponse> invalidTicketResponse = restTemplate.exchange(
                "/v1/tickets", HttpMethod.POST, new HttpEntity<>(invalidTicket, customerHeaders), new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.BAD_REQUEST, invalidTicketResponse.getStatusCode());

        // get all tickets: admin
        ResponseEntity<ApiResponse<List<Ticket>>> allTicketsAdminResponse = restTemplate.exchange(
                "/v1/tickets", HttpMethod.GET, new HttpEntity<>(adminHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, allTicketsAdminResponse.getStatusCode());
        assertTrue(!Objects.requireNonNull(allTicketsAdminResponse.getBody()).getData().isEmpty());

        // get all tickets by user: customer
        ResponseEntity<ApiResponse<List<Ticket>>> ticketsByUserResponse = restTemplate.exchange(
                "/v1/tickets/user/1", HttpMethod.GET, new HttpEntity<>(customerHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, ticketsByUserResponse.getStatusCode());

        // get all tickets: customer
        ResponseEntity<ApiResponse<List<Ticket>>> allTicketsCustomerResponse = restTemplate.exchange(
                "/v1/tickets/user/" + customerId, HttpMethod.GET, new HttpEntity<>(customerHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, allTicketsCustomerResponse.getStatusCode());
        assertTrue(!Objects.requireNonNull(allTicketsCustomerResponse.getBody()).getData().isEmpty());

        ResponseEntity<ApiResponse<List<Ticket>>> allTicketsShowtimeResponse = restTemplate.exchange(
                "/v1/tickets/showtime/" + showtime1Id, HttpMethod.GET, new HttpEntity<>(adminHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, allTicketsShowtimeResponse.getStatusCode());
        assertTrue(!Objects.requireNonNull(allTicketsShowtimeResponse.getBody()).getData().isEmpty());

        // update ticket: customer
        Ticket updatedTicket = new Ticket(customerTicketId, customerId, showtime1Id, "C1", BigDecimal.valueOf(20.0));
        ResponseEntity<ApiResponse<Ticket>> updateTicketResponse = restTemplate.exchange(
                "/v1/tickets/" + customerTicketId, HttpMethod.PUT, new HttpEntity<>(updatedTicket, customerHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, updateTicketResponse.getStatusCode());
        assertEquals(updatedTicket.getSeatNumber(), Objects.requireNonNull(updateTicketResponse.getBody()).getData().getSeatNumber());

        // delete ticket: customer
        ResponseEntity<ApiResponse<Long>> deleteTicketResponse = restTemplate.exchange(
                "/v1/tickets/" + customerTicketId, HttpMethod.DELETE, new HttpEntity<>(customerHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, deleteTicketResponse.getStatusCode());
        assertEquals(customerTicketId, Objects.requireNonNull(deleteTicketResponse.getBody()).getData());

        // verify deletion
        ResponseEntity<ApiResponse<List<Ticket>>> verifyDeletionResponse = restTemplate.exchange(
                "/v1/tickets", HttpMethod.GET, new HttpEntity<>(adminHeaders),
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, verifyDeletionResponse.getStatusCode());
        assertFalse(Objects.requireNonNull(verifyDeletionResponse.getBody()).getData()
                .stream().anyMatch(ticket -> ticket.getId().equals(customerTicketId)));
    }

    private Long extracTicketrId(ResponseEntity<ApiResponse> response) {
        LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) response.getBody().getData();
        Ticket movie = objectMapper.convertValue(dataMap, Ticket.class);
        return movie.getId();
    }


    private Long extractMovieId(ResponseEntity<ApiResponse> response) {
        LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) response.getBody().getData();
        Movie movie = objectMapper.convertValue(dataMap, Movie.class);
        return movie.getId();
    }

    private Movie extractMovie(ResponseEntity<ApiResponse> response) {
        LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) response.getBody().getData();
        return objectMapper.convertValue(dataMap, Movie.class);
    }

    private Long extractShowtimeId(ResponseEntity<ApiResponse> response) {
        LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) response.getBody().getData();
        Showtime showtime = objectMapper.convertValue(dataMap, Showtime.class);
        return showtime.getId();
    }

    private Long extractUserId(ResponseEntity<ApiResponse> response) {
        LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) response.getBody().getData();
        User User = objectMapper.convertValue(dataMap, User.class);
        return User.getId();
    }

    private Showtime extractShowtime(ResponseEntity<ApiResponse> response) {
        LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) response.getBody().getData();
        return objectMapper.convertValue(dataMap, Showtime.class);
    }
}
