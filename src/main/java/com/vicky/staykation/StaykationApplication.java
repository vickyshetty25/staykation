package com.vicky.staykation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
@EnableMethodSecurity
public class StaykationApplication {
    public static void main(String[] args) {
        SpringApplication.run(StaykationApplication.class, args);
    }
}