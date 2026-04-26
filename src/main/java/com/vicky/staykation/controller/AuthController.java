package com.vicky.staykation.controller;

import com.vicky.staykation.dto.AuthResponse;
import com.vicky.staykation.dto.LoginRequest;
import com.vicky.staykation.dto.PasswordResetRequest;
import com.vicky.staykation.dto.RegisterRequest;
import com.vicky.staykation.service.AuthService;
import com.vicky.staykation.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService,
                          PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody Map<String, String> body) {
        String token = passwordResetService
                .generateResetToken(body.get("email"));
        return ResponseEntity.ok(Map.of(
                "message", "Reset token generated",
                "resetToken", token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody PasswordResetRequest request) {
        passwordResetService.resetPassword(
                request.getResetToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of(
                "message", "Password reset successfully"));
    }
}