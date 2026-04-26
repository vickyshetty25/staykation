package com.vicky.staykation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name= "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "GUEST"; // Guest, Host, Admin

    private String contactNumber;
    private String addressProof;
    private String idProof;
    private String bankName;
    private String bankAccountNumber;
    private String ifscCode;

    @Column(name = "is_host")
    private boolean isHost = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
