package com.example.hometask.unit.service;

import com.example.hometask.data.User;
import com.example.hometask.repository.TicketRepository;
import com.example.hometask.repository.UserRepository;
import com.example.hometask.repository.entity.Role;
import com.example.hometask.repository.entity.TicketEntity;
import com.example.hometask.repository.entity.UserEntity;
import com.example.hometask.service.impl.UserServiceImpl;
import com.example.hometask.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        User user = new User("username", "user@example.com", "password", null, null);
        UserEntity userEntity = new UserEntity(null, "username", "user@example.com", null, "password");
        UserEntity savedEntity = new UserEntity(1L, "username", "user@example.com", Role.ROLE_CUSTOMER, "encodedPassword");
        User expectedUser = new User("username", "user@example.com", "encodedPassword", "ROLE_CUSTOMER", 1L);

        when(userMapper.toEntity(user)).thenReturn(userEntity);
        when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(userEntity)).thenReturn(savedEntity);
        when(userMapper.toDto(savedEntity)).thenReturn(expectedUser);

        User result = userService.registerUser(user, Role.ROLE_CUSTOMER);

        assertEquals(expectedUser, result);
        verify(userMapper, times(1)).toEntity(user);
        verify(userRepository, times(1)).findByUsername("username");
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toDto(savedEntity);
    }

    @Test
    void registerUser_UsernameAlreadyExists() {
        User user = new User("username", "user@example.com", "password", null, null);
        UserEntity existingUserEntity = new UserEntity(1L, "username", "user@example.com", Role.ROLE_CUSTOMER, "password");

        when(userMapper.toEntity(user)).thenReturn(existingUserEntity);
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(existingUserEntity));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user, Role.ROLE_CUSTOMER));

        assertEquals("username already registered", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("username");
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void deleteUser_Success() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity(userId, "username", "user@example.com", Role.ROLE_CUSTOMER, "password");
        List<TicketEntity> tickets = List.of(new TicketEntity(1L, null, null, "A1", null));

        when(userRepository.existsById(userId)).thenReturn(true);
        when(ticketRepository.findByUser_Id(userId)).thenReturn(tickets);

        Long result = userService.deleteUser(userId);

        assertEquals(userId, result);
        verify(ticketRepository, times(1)).findByUser_Id(userId);
        verify(ticketRepository, times(1)).deleteById(1L);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_NotFound() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(userId));

        assertEquals("User not found: " + userId, exception.getMessage());
        verify(userRepository, times(1)).existsById(userId);
        verifyNoInteractions(ticketRepository);
    }

    @Test
    void getAllUsers() {
        List<UserEntity> entities = List.of(
                new UserEntity(1L, "admin", "admin@example.com", Role.ROLE_ADMIN, "password"),
                new UserEntity(2L, "customer", "customer@example.com", Role.ROLE_CUSTOMER, "password")
        );

        List<User> expectedDtos = List.of(
                new User("admin", "admin@example.com", "password", "ROLE_ADMIN", 1L),
                new User("customer", "customer@example.com", "password", "ROLE_CUSTOMER", 2L)
        );

        when(userRepository.findAll()).thenReturn(entities);
        when(userMapper.toDto(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity entity = invocation.getArgument(0);
            return expectedDtos.stream().filter(dto -> dto.getId().equals(entity.getId())).findFirst().orElse(null);
        });

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(expectedDtos, result);
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(2)).toDto(any(UserEntity.class));
    }
}