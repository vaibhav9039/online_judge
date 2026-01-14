package com.service;

import com.dto.ExecutionResult;
import com.entity.Submission;
import com.entity.TestCase;
import com.execution.ExecutorFactory;
import com.execution.LanguageExecutor;
import com.repository.TestCaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeExecutionService {

    private final ExecutorFactory executorFactory;
    private final TestCaseRepository testRepo;

    public CodeExecutionService(ExecutorFactory executorFactory,
                                TestCaseRepository testRepo) {
        this.executorFactory = executorFactory;
        this.testRepo = testRepo;
    }

    public void execute(Submission s) {
        List<TestCase> tests = testRepo.findByProblemId(s.getProblemId());
        int passed = 0;
        long totalTime = 0;
        long maxMemory = 0;

        String finalStatus = "AC";

        LanguageExecutor executor = executorFactory.getExecutor(s.getLanguage());

        for (TestCase t : tests) {
            ExecutionResult result = executor.execute(s.getCode(), t.getInputData(), s.getTimeLimitMs());

            totalTime += result.getTimeMs();
            if (result.getMemoryKb() > maxMemory) maxMemory = result.getMemoryKb();

            switch (result.getStatus()) {
                case "CE": finalStatus = "CE"; break;
                case "TLE": if (!finalStatus.equals("CE")) finalStatus = "TLE"; break;
                case "RE": if (!finalStatus.equals("CE") && !finalStatus.equals("TLE")) finalStatus = "RE"; break;
                case "AC": passed++; break;
                default: break;
            }
        }

        s.setPassed(passed);
        s.setTotal(tests.size());
        s.setExecutionTimeMs(totalTime);
        s.setMemoryKb(maxMemory);
        s.setStatus(finalStatus);
    }
}
