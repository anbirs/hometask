package com.example.hometask.service.converter;

import com.example.hometask.data.User;
import com.example.hometask.repository.UserRepository;
import com.example.hometask.repository.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter <User, UserEntity>{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User toDto(UserEntity entity) {
        return new User(
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole().toString(),
                entity.getId()
                );
    }

    @Override
    public UserEntity toEntity(User dto) {
        return new UserEntity(
                dto.getId(),
                dto.getUsername(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getRole());
    }

    public UserEntity toEntity(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
