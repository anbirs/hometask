package com.example.hometask.service;

import com.example.hometask.data.User;
import com.example.hometask.repository.UserRepository;
import com.example.hometask.repository.entity.UserEntity;
import com.example.hometask.service.converter.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserConverter userConverter;

    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        UserEntity userEntity = userConverter.toEntity(user);
        if (userRepository.findByEmail(userEntity.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userConverter.toDto(userRepository.save(userEntity));
    }
}