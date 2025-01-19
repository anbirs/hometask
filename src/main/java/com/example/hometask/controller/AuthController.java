package com.example.hometask.controller;

import com.example.hometask.data.ApiResponse;
import com.example.hometask.data.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/auth")
@Tag(name = "Auth", description = "Authentication")
public interface AuthController {

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Registered user log in")
    ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest);

}
