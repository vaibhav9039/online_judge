package com.controller;

import com.execution.DockerRunner;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guest")
public class GuestCompilerController {

    private final DockerRunner dockerRunner;

    public GuestCompilerController(DockerRunner dockerRunner) {
        this.dockerRunner = dockerRunner;
    }

    @PostMapping("/run")
    public String run(@RequestParam String language,
                      @RequestBody String code,
                      @RequestParam(required = false) String input) {

        return dockerRunner.runGuest(language, code, input == null ? "" : input);
    }
}
