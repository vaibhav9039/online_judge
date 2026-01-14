package com.controller;

import com.dto.GuestRunRequest;
import com.dto.GuestRunResponse;
import com.execution.DockerRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guest")
public class GuestCompilerController {

    private final DockerRunner dockerRunner;

    public GuestCompilerController(DockerRunner dockerRunner) {
        this.dockerRunner = dockerRunner;
    }

    @PostMapping("/run")
    public ResponseEntity<GuestRunResponse> run(@RequestBody GuestRunRequest req) {
        String output = dockerRunner.runGuest(
                req.getLanguage(),
                req.getCode(),
                req.getInput()
        );
        return ResponseEntity.ok(new GuestRunResponse(output));
    }
}
