package com.sahil.cardrescue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sahil.cardrescue.dto.AuthResponse;
import com.sahil.cardrescue.dto.LoginRequest;
import com.sahil.cardrescue.dto.RegisterRequest;
import com.sahil.cardrescue.dto.UserResponse;
import com.sahil.cardrescue.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        String token = service.login(request);
        return new AuthResponse(token);
    }
}