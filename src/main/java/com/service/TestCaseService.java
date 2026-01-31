package com.service;


import com.entity.TestCase;
import com.entity.Problem;
import com.exception.NotFoundException;
import com.repository.TestCaseRepository;
import com.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestCaseService {

   @Autowired
   private TestCaseRepository testRepo;

   @Autowired
   private ProblemRepository problemRepo;

    public TestCase createTestCase(TestCase t) {
        Long problemId = t.getProblemId();
        Problem p = problemRepo.findById(problemId)
                .orElseThrow(() -> new NotFoundException("Problem not found"));
        return testRepo.save(t);
    }

    public List<TestCase> getProblemTestCases(Long problemId) {
        return testRepo.findByProblemId(problemId);
    }

    public TestCase getTestCase(Long id) {
        return testRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Test case not found"));
    }

    public TestCase updateTestCase(Long id, TestCase updated) {
        TestCase existing = getTestCase(id);

        existing.setInputData(updated.getInputData());
        existing.setExpectedOutput(updated.getExpectedOutput());

        return testRepo.save(existing);
    }

    public void deleteTestCase(Long id) {
        testRepo.deleteById(id);
    }
}

