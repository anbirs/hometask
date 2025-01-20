package com.example.hometask.service.impl;

import com.example.hometask.data.User;
import com.example.hometask.repository.TicketRepository;
import com.example.hometask.repository.UserRepository;
import com.example.hometask.repository.entity.Role;
import com.example.hometask.repository.entity.TicketEntity;
import com.example.hometask.repository.entity.UserEntity;
import com.example.hometask.service.UserService;
import com.example.hometask.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user, Role role) {
        UserEntity userEntity = userMapper.toEntity(user);
        if (userRepository.findByUsername(userEntity.getUsername()).isPresent()) {
            throw new IllegalArgumentException("username already registered");
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRole(role);
        return userMapper.toDto(userRepository.save(userEntity));
    }

    @Override
    @Transactional
    public Long deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found: " + id);
        }
        // remove related tickets
        List<TicketEntity> ticketsPerUser = ticketRepository.findByUser_Id(id);
        ticketsPerUser.forEach(ticketEntity -> ticketRepository.deleteById(ticketEntity.getId()));
        userRepository.deleteById(id);
        return id;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}