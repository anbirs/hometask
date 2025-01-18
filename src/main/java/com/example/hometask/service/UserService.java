package com.example.hometask.service;

import com.example.hometask.data.User;

import java.util.List;

public interface UserService {

     User registerUser(User user);
     User findUserByUsername(String username);
     Long deleteUser(Long id);
     List<User> getAllUsers();
}