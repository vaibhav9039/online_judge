package com.controller;

import com.entity.TestCase;
import com.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testcases")
public class TestCaseController {

    @Autowired
    private TestCaseService service;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public TestCase create(
            @RequestBody TestCase t
    ) {
        return service.createTestCase(t);
    }

    @GetMapping("/problem/{problemId}")
    public List<TestCase> getByProblem(@PathVariable Long problemId) {
        return service.getProblemTestCases(problemId);
    }

    @GetMapping("/{id}")
    public TestCase get(@PathVariable Long id) {
        return service.getTestCase(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TestCase update(@PathVariable Long id, @RequestBody TestCase t) {
        return service.updateTestCase(id, t);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.deleteTestCase(id);
    }
}
