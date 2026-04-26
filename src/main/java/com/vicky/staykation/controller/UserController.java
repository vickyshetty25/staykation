package com.vicky.staykation.controller;

import com.vicky.staykation.model.User;
import com.vicky.staykation.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getProfile() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return ResponseEntity.ok(userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    @PutMapping("/become-host")
    public ResponseEntity<?> becomeHost() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setHost(true);
        user.setRole("HOST");
        userRepository.save(user);
        return ResponseEntity.ok(Map.of(
                "message", "Congratulations! You are now a host.",
                "role", "HOST"
        ));
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateProfile(
            @RequestBody Map<String, String> body) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (body.containsKey("name")) user.setName(body.get("name"));
        if (body.containsKey("contactNumber"))
            user.setContactNumber(body.get("contactNumber"));
        if (body.containsKey("bankName"))
            user.setBankName(body.get("bankName"));
        if (body.containsKey("bankAccountNumber"))
            user.setBankAccountNumber(body.get("bankAccountNumber"));
        if (body.containsKey("ifscCode"))
            user.setIfscCode(body.get("ifscCode"));

        return ResponseEntity.ok(userRepository.save(user));
    }
}