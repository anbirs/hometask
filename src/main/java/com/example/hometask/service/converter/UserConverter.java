package com.example.hometask.service.converter;

import com.example.hometask.data.User;
import com.example.hometask.repository.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter <User, UserEntity>{

    @Override
    public User toDto(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getName(),
                entity.getPassword(),
                entity.getRole()
        );
    }

    @Override
    public UserEntity toEntity(User dto) {
        return new UserEntity(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getRole());
    }
}
