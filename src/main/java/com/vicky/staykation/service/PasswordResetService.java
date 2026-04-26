package com.vicky.staykation.service;

import com.vicky.staykation.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    public PasswordResetService(UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
    }

    public String generateResetToken(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String token = UUID.randomUUID().toString();
        // Store token in Redis with 15 min expiry
        redisTemplate.opsForValue().set(
                "reset:" + token, email, 15, TimeUnit.MINUTES);

        // In production: send via email (SendGrid/SES)
        System.out.println("Password reset token for " + email + ": " + token);
        return token;
    }

    public void resetPassword(String token, String newPassword) {
        String email = redisTemplate.opsForValue().get("reset:" + token);
        if (email == null) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        userRepository.findByEmail(email).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        });

        redisTemplate.delete("reset:" + token);
    }
}