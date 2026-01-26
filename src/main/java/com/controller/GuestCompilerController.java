package com.controller;

import com.config.GuestRateLimiter;
import com.dto.GuestRunRequest;
import com.dto.GuestRunResponse;
import com.execution.DockerRunner;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guest")
public class GuestCompilerController {

    private final DockerRunner dockerRunner;
    private final GuestRateLimiter guestRateLimiter;

    public GuestCompilerController(DockerRunner dockerRunner, GuestRateLimiter guestRateLimiter) {
        this.dockerRunner = dockerRunner;
        this.guestRateLimiter = guestRateLimiter;
    }

    @PostMapping("/run")
    public ResponseEntity<GuestRunResponse> run(@RequestBody GuestRunRequest req,
                                                HttpServletRequest request) {

        if (req.getLanguage() == null || req.getCode() == null) {
            return ResponseEntity.badRequest()
                    .body(new GuestRunResponse("Missing code or language"));
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        if (!guestRateLimiter.allow(ip)) {
            return ResponseEntity.status(429)
                    .body(new GuestRunResponse("Too many requests"));
        }

        String output = dockerRunner.runGuest(
                req.getLanguage(),
                req.getCode(),
                req.getInput() != null ? req.getInput() : ""
        );

        return ResponseEntity.ok(new GuestRunResponse(output));
    }
}
