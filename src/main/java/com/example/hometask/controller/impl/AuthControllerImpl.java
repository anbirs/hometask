package com.example.hometask.controller.impl;

import com.example.hometask.config.JwtTokenProvider;
import com.example.hometask.controller.AuthController;
import com.example.hometask.data.ApiResponse;
import com.example.hometask.data.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<ApiResponse<String>> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(authentication);

            return ResponseEntity.ok(ApiResponse.success(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid username or password"));
        }
    }
}
