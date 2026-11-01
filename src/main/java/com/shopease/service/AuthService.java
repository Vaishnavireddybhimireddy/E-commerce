package com.shopease.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopease.controller.RegisterRequest;
import com.shopease.entity.AppUser;
import com.shopease.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        AppUser user = new AppUser(
            request.getUsername(),
            passwordEncoder.encode(request.getPassword()),
            "USER"
        );

        userRepository.save(user);

        return "Registration successful";
    }
}