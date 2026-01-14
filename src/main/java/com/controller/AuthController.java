package com.controller;

import com.dto.LoginRequest;
import com.dto.RegisterRequest;
import com.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterRequest> register(@RequestBody RegisterRequest request) {
        RegisterRequest registerRequest = authService.register(request);
        return ResponseEntity.ok(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginRequest request) {
        Map<String,String> map = new HashMap<>();
        String token = authService.login(request);
        map.put("token",token);
        return ResponseEntity.ok(map);
    }
}
