package com.example.hometask.unit.service.mapper;

import com.example.hometask.data.User;
import com.example.hometask.repository.UserRepository;
import com.example.hometask.repository.entity.Role;
import com.example.hometask.repository.entity.UserEntity;
import com.example.hometask.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toDto() {
        UserEntity entity = new UserEntity(1L, "Username", "email", Role.ROLE_CUSTOMER, "secret");
        User expectedDto = new User("Username", "email", null, "ROLE_CUSTOMER", 1L);

        User dto = userMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(expectedDto, dto);
    }

    @Test
    void toEntity() {
        User dto = new User("Username", "email", "secret", "ROLE_CUSTOMER",1L);
        UserEntity expectedEntity = new UserEntity(1L, "Username", "email", Role.ROLE_CUSTOMER, "secret");

        UserEntity entity = userMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(expectedEntity, entity);
    }

    @Test
    void idToEntity() {
        Long userId = 1L;
        UserEntity entity = new UserEntity(userId, "Username", "email", Role.ROLE_CUSTOMER, "secret");
        UserEntity expectedEntity = new UserEntity(userId, "Username", "email", Role.ROLE_CUSTOMER, "secret");
        when(userRepository.findById(userId)).thenReturn(Optional.of(entity));

        UserEntity result = userMapper.idToEntity(userId);

        assertNotNull(result);
        assertEquals(expectedEntity, result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void updateEntityFromDto() {
        Long userId = 1L;
        UserEntity existingEntity = new UserEntity(userId, "Username", "email", Role.ROLE_CUSTOMER, "secret");
        User dto = new User("Updated", "new_email", "hacked", "ROLE_CUSTOMER",null);
        UserEntity expectedEntity = new UserEntity(userId, "Updated", "new_email", Role.ROLE_CUSTOMER,  "secret");

        userMapper.updateEntityFromDto(dto, existingEntity);

        assertEquals(expectedEntity, existingEntity);
    }

    @Test
    void testIdToEntity_NotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userMapper.idToEntity(userId));
    }
}