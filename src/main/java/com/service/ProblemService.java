package com.service;

import com.entity.Problem;
import com.repository.ProblemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemService {


    private final ProblemRepository repo;

    public ProblemService(ProblemRepository repo) {
        this.repo = repo;
    }

    public Problem createProblem(Problem p) {
        return repo.save(p);
    }

    public Problem updateProblem(Long id, Problem p) {
        Problem existing = repo.findById(id).orElseThrow();
        existing.setTitle(p.getTitle());
        existing.setDescription(p.getDescription());
        existing.setTimeLimitMs(p.getTimeLimitMs());
        existing.setMemoryLimitKb(p.getMemoryLimitKb());
        existing.setDifficulty(p.getDifficulty());
        return repo.save(existing);
    }

    public void deleteProblem(Long id) {
        repo.deleteById(id);
    }

    public Problem getProblem(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<Problem> getAll() {
        return repo.findAll();
    }
}
