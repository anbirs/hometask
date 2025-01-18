package com.example.hometask.controller;

import com.example.hometask.data.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserController {
    @PostMapping("/register")
    ResponseEntity<Long> register(@RequestBody User user);

    @GetMapping
    ResponseEntity<List<User>> getAllUsers();

    @DeleteMapping("/{id}")
    ResponseEntity<Long> deleteUser(@PathVariable Long id);

}
