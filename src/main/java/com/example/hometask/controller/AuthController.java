package com.example.hometask.controller;

import com.example.hometask.data.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {
    @PostMapping("/register")
    ResponseEntity<User> register(@RequestBody User user);
}
