package com.example.hometask.unit.controller;

import com.example.hometask.controller.ApiExceptionHandler;
import com.example.hometask.controller.impl.UserControllerImpl;
import com.example.hometask.data.User;
import com.example.hometask.repository.entity.Role;
import com.example.hometask.service.UserService;
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
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserControllerImpl userController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void registerUser() throws Exception {
        User requestUser = new User("john_doe", "john.doe@example.com", "password123", null, null);
        Long userId = 1L;

        when(userService.registerUser(requestUser, Role.ROLE_CUSTOMER)).thenReturn(new User("john_doe", "john.doe@example.com", "password123", Role.ROLE_CUSTOMER.toString(), userId));

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users/register/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"username\": \"john_doe\",\n" +
                                "    \"email\": \"john.doe@example.com\",\n" +
                                "    \"password\": \"password123\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"data\":1}");

        verify(userService, times(1)).registerUser(requestUser, Role.ROLE_CUSTOMER);
    }

    @Test
    void registerAdmin() throws Exception {
        User requestUser = new User("admin_user", "admin@example.com", "admin123", null, null);
        Long adminId = 2L;

        when(userService.registerUser(requestUser, Role.ROLE_ADMIN)).thenReturn(new User("admin_user", "admin@example.com", "admin123", Role.ROLE_ADMIN.toString(), adminId));

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users/register/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"username\": \"admin_user\",\n" +
                                "    \"email\": \"admin@example.com\",\n" +
                                "    \"password\": \"admin123\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"data\":2}");

        verify(userService, times(1)).registerUser(requestUser, Role.ROLE_ADMIN);
    }

    @Test
    void getAllUsers() throws Exception {
        List<User> users = List.of(
                new User("user1", "user1@example.com", null, "ROLE_CUSTOMER", 1L),
                new User("admin1", "admin1@example.com", null, "ROLE_ADMIN", 2L)
        );

        when(userService.getAllUsers()).thenReturn(users);

        MockHttpServletResponse response = mockMvc.perform(get("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String expectedResponse = "{\"data\":[{\"username\":\"user1\",\"email\":\"user1@example.com\",\"role\":\"ROLE_CUSTOMER\",\"id\":1}," +
                "{\"username\":\"admin1\",\"email\":\"admin1@example.com\",\"role\":\"ROLE_ADMIN\",\"id\":2}]}";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void deleteUser() throws Exception {
        Long userId = 1L;

        when(userService.deleteUser(userId)).thenReturn(userId);

        MockHttpServletResponse response = mockMvc.perform(delete("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"data\":1}");

        verify(userService, times(1)).deleteUser(userId);
    }
}
