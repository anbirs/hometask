package com.example.hometask.service.mapper;

import com.example.hometask.data.User;
import com.example.hometask.repository.UserRepository;
import com.example.hometask.repository.entity.Role;
import com.example.hometask.repository.entity.UserEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper implements EntityMapper<User, UserEntity> {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Mapping(target = "password", ignore = true)
    public abstract User toDto(UserEntity entity);

    @Mapping(target = "role", source = "role")
    public abstract UserEntity toEntity(User dto);

    protected Role mapStringToEnum(String role) {
        if (role == null) {
            return null;
        }
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Named("idToUserEntity")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    public UserEntity idToEntity(Long id) {
        if (id == null) {
            return null;
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    public abstract void updateEntityFromDto(User dto, @MappingTarget UserEntity entity);
}

