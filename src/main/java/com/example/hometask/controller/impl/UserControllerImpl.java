package com.example.hometask.controller.impl;

import com.example.hometask.controller.UserController;
import com.example.hometask.data.User;
import com.example.hometask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<Long> register(User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser.getId());    }

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<Long> deleteUser(Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
