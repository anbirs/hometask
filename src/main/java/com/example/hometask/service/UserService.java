package com.example.hometask.service;

import com.example.hometask.data.User;
import com.example.hometask.repository.entity.Role;

import java.util.List;

public interface UserService {

     User registerUser(User user, Role role);
     Long deleteUser(Long id);
     List<User> getAllUsers();
}