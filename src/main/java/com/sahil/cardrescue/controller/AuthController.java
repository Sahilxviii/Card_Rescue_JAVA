package com.sahil.cardrescue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sahil.cardrescue.dto.AuthResponse;
import com.sahil.cardrescue.dto.ForgotPasswordRequest;
import com.sahil.cardrescue.dto.LoginRequest;
import com.sahil.cardrescue.dto.RegisterRequest;
import com.sahil.cardrescue.dto.ResetPasswordRequest;
import com.sahil.cardrescue.dto.UserResponse;
import com.sahil.cardrescue.service.AuthService;
import com.sahil.cardrescue.service.PasswordResetService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        String token = service.login(request);
        return new AuthResponse(token);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return passwordResetService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        return passwordResetService.resetPassword(request);
    }
}