package com.service;

import com.dto.ExecutionResult;
import com.dto.TestCaseResult;
import com.entity.Submission;
import com.entity.TestCase;
import com.execution.ExecutorFactory;
import com.execution.LanguageExecutor;
import com.repository.TestCaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class JudgeService {

    private final ExecutorFactory executorFactory;
    private final TestCaseRepository testRepo;

    private final ExecutorService pool =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public JudgeService(ExecutorFactory executorFactory,
                        TestCaseRepository testRepo) {
        this.executorFactory = executorFactory;
        this.testRepo = testRepo;
    }

    public List<TestCaseResult> judge(Submission s) {

        LanguageExecutor executor =
                executorFactory.getExecutor(s.getLanguage());

        List<TestCase> tests =
                testRepo.findByProblemId(s.getProblemId());

        List<CompletableFuture<TestCaseResult>> futures =
                tests.stream().map(tc ->
                        CompletableFuture.supplyAsync(() -> {
                            ExecutionResult r =
                                    executor.execute(
                                            s.getCode(),
                                            tc.getInputData(),
                                            s.getTimeLimitMs()
                                    );

                            return new TestCaseResult(
                                    tc.getId(),
                                    r.getStatus(),
                                    r.getTimeMs(),
                                    r.getMemoryKb()
                            );
                        }, pool)
                ).toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }
}

