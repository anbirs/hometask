package com.example.hometask.controller;

import com.example.hometask.data.ApiResponse;
import com.example.hometask.data.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/users")
@Tag(name = "Users", description = "User Management")
public interface UserController {
    @PostMapping("/register/customer")
    @Operation(summary = "Register new Customer", description = "Create new user with customer role")
    ResponseEntity<ApiResponse<Long>> register(@RequestBody User user);

    @PostMapping("/register/admin")
    @Operation(summary = "Register new Admin", description = "Create new user with admin role")
    ResponseEntity<ApiResponse<Long>> registerAdmin(@RequestBody User user);

    @GetMapping
    @Operation(summary = "Get all User", description = "Get all registered users. This must not expose passwords")
    ResponseEntity<ApiResponse<List<User>>> getAllUsers();

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User", description = "Remove the User from the storage")
    ResponseEntity<ApiResponse<Long>> deleteUser(@PathVariable Long id);

}
