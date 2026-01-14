package com.service;

import com.dto.SubmissionRequest;
import com.dto.SubmissionResponse;
import com.dto.TestCaseResult;
import com.entity.Problem;
import com.entity.Submission;
import com.repository.ProblemRepository;
import com.repository.SubmissionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {

    private final SubmissionRepository repo;
    private final CodeExecutionService executor;
    private final JudgeService judgeService;
    private final ProblemRepository problemRepository;

    public SubmissionService(SubmissionRepository repo,
                             CodeExecutionService executor,
                             JudgeService judgeService, ProblemRepository problemRepository) {
        this.repo = repo;
        this.executor = executor;
        this.judgeService = judgeService;
        this.problemRepository = problemRepository;
    }

    public SubmissionResponse submit(SubmissionRequest req) {

        Problem problem = problemRepository.findById(req.getProblemId()).orElseThrow();
        Submission s = new Submission();
        s.setProblemId(req.getProblemId());
        s.setLanguage(req.getLanguage());
        s.setCode(req.getCode());
        s.setStatus("RUNNING");
        s.setTimeLimitMs(problem.getTimeLimitMs());
        repo.save(s);
        runAsync(s);
        return new SubmissionResponse(s.getId(), "RUNNING");
    }
    @Async
    void runAsync(Submission s) {
        List<TestCaseResult> results = judgeService.judge(s);

        int passed = 0;
        long totalTime = 0;
        long maxMemory = 0;
        String finalStatus = "AC";

        for (TestCaseResult r : results) {
            totalTime += r.getTimeMs();
            if (r.getMemoryKb() > maxMemory) maxMemory = r.getMemoryKb();

            switch (r.getStatus()) {
                case "CE": finalStatus = "CE"; break;
                case "TLE": if (!finalStatus.equals("CE")) finalStatus = "TLE"; break;
                case "RE": if (!finalStatus.equals("CE") && !finalStatus.equals("TLE")) finalStatus = "RE"; break;
                case "AC": passed++; break;
            }
        }

        s.setPassed(passed);
        s.setTotal(results.size());
        s.setExecutionTimeMs(totalTime);
        s.setMemoryKb(maxMemory);
        s.setStatus(finalStatus);

        // Save submission after execution
        repo.save(s);
    }

    public Submission getSubmission(Long id) {
        return repo.findById(id).orElseThrow();
    }
}
