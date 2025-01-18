package com.example.hometask.service.impl;

import com.example.hometask.data.User;
import com.example.hometask.repository.UserRepository;
import com.example.hometask.repository.entity.UserEntity;
import com.example.hometask.service.UserService;
import com.example.hometask.service.converter.UserConverter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        UserEntity userEntity = userConverter.toEntity(user);
        if (userRepository.findByUsername(userEntity.getUsername()).isPresent()) {
            throw new IllegalArgumentException("username already registered");
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userConverter.toDto(userRepository.save(userEntity));
    }

    @Override
    public User findUserByUsername(String username) {
        return userConverter.toDto(userRepository.findByUsername(username).orElseThrow());
    }

    @Override
    public Long deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
        return id;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userConverter::toDto)
                .collect(Collectors.toList());
    }
}