package com.controller;

import com.entity.Problem;
import com.service.ProblemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemService service;
    public ProblemController(ProblemService service) {
        this.service = service;
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Problem create(@RequestBody Problem p) {
        return service.createProblem(p);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Problem update(@PathVariable Long id, @RequestBody Problem p) {
        return service.updateProblem(id, p);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.deleteProblem(id);
    }

    @GetMapping
    public List<Problem> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Problem get(@PathVariable Long id) {
        return service.getProblem(id);
    }
}
