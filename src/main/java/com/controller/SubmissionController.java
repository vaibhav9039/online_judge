package com.controller;

import com.dto.SubmissionRequest;
import com.dto.SubmissionResponse;
import com.entity.Submission;
import com.service.SubmissionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submission")
public class SubmissionController {
    private final SubmissionService service;

    public SubmissionController(SubmissionService service) {
        this.service = service;
    }

    @PostMapping
    public SubmissionResponse submit(@RequestBody SubmissionRequest request) {
        return service.submit(request);
    }

    @GetMapping("/{id}")
    public Submission getStatus(@PathVariable Long id) {
        return service.getSubmission(id);
    }

}
