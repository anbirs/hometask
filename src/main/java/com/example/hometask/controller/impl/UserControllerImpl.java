package com.example.hometask.controller.impl;

import com.example.hometask.controller.UserController;
import com.example.hometask.data.ApiResponse;
import com.example.hometask.data.User;
import com.example.hometask.repository.entity.Role;
import com.example.hometask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<ApiResponse<Long>> register(User user) {
        User registeredUser = userService.registerUser(user, Role.ROLE_CUSTOMER);
        return ResponseEntity.ok(ApiResponse.success(registeredUser.getId()));
    }

    @Override
    public ResponseEntity<ApiResponse<Long>> registerAdmin(User user) {
        User registeredUser = userService.registerUser(user, Role.ROLE_ADMIN);
        return ResponseEntity.ok(ApiResponse.success(registeredUser.getId()));
    }

    @Override
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
    }

    @Override
    public ResponseEntity<ApiResponse<Long>> deleteUser(Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.deleteUser(id)));
    }
}
