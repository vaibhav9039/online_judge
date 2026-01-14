package com.service;

import com.config.JwtUtil;
import com.dto.LoginRequest;
import com.dto.RegisterRequest;
import com.entity.User;
import com.exception.InvalidCredentialsException;
import com.exception.NotFoundException;
import com.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository repo, BCryptPasswordEncoder encoder, JwtUtil jwtUtil) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public RegisterRequest register(RegisterRequest req) {
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole("USER");
        repo.save(user);
        return req;
    }

    public String login(LoginRequest req) {
        User user = repo.findByUsername(req.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return jwtUtil.generateToken(user.getUsername(), Collections.singletonList(user.getRole()));
    }

}
