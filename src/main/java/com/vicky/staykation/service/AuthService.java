package com.vicky.staykation.service;

import com.vicky.staykation.dto.AuthResponse;
import com.vicky.staykation.dto.LoginRequest;
import com.vicky.staykation.dto.RegisterRequest;
import com.vicky.staykation.model.User;
import com.vicky.staykation.repository.UserRepository;
import com.vicky.staykation.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setContactNumber(request.getContactNumber());
        user.setRole(request.getRole() != null
                ? request.getRole() : "GUEST");
        user.setHost(user.getRole().equals("HOST"));
        user = userRepository.save(user);

        String token = jwtUtil.generateToken(
                user.getEmail(), user.getId(), user.getRole());
        return new AuthResponse(token, user.getEmail(),
                user.getName(), user.getRole(), user.getId());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(), user.getId(), user.getRole());
        return new AuthResponse(token, user.getEmail(),
                user.getName(), user.getRole(), user.getId());
    }
}