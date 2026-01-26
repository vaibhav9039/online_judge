package com.service;

import com.config.JwtUtil;
import com.dto.AuthResponse;
import com.dto.LoginRequest;
import com.dto.RegisterRequest;
import com.entity.RefreshToken;
import com.entity.User;
import com.exception.InvalidCredentialsException;
import com.exception.NotFoundException;
import com.repository.RefreshTokenRepository;
import com.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository repoRefresh;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthService(UserRepository repo, BCryptPasswordEncoder encoder, JwtUtil jwtUtil, RefreshTokenRepository repoRefresh, TokenBlacklistService tokenBlacklistService) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.repoRefresh = repoRefresh;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public RegisterRequest register(RegisterRequest req) {
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole("USER");
        repo.save(user);
        return req;
    }

    public AuthResponse login(LoginRequest req) {

        User user = repo.findByUsername(req.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String accessToken = jwtUtil.generateAccessToken(
                user.getUsername(),
                List.of(user.getRole())
        );

        String refreshToken = UUID.randomUUID().toString();

        RefreshToken rt = new RefreshToken();
        rt.setToken(refreshToken);
        rt.setUsername(user.getUsername());
        rt.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));

        repoRefresh.save(rt);

        return new AuthResponse(accessToken, refreshToken);
    }

    public String refresh(String refreshToken) {
        RefreshToken rt = repoRefresh.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

        if (rt.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidCredentialsException("Refresh token expired");
        }

        User user = repo.findByUsername(rt.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        return jwtUtil.generateAccessToken(
                user.getUsername(),
                List.of(user.getRole())
        );
    }


    @Transactional
    public void logout(String token, String username) {

        Date expiry = jwtUtil.extractExpiration(token);

        long ttlMs = expiry.getTime() - System.currentTimeMillis();

        if (ttlMs > 0) {
            tokenBlacklistService.blacklist(token, ttlMs);
        }

        try {
            repoRefresh.deleteByUsername(username);
        } catch (Exception e) {
            // log warning
            System.out.println("Warning: could not delete refresh token for " + username + e);
        }
    }



}
