package com.service;

import com.entity.Problem;
import com.exception.NotFoundException;
import com.repository.ProblemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Problem existing = repo.findById(id).orElse(null);
        if (existing == null) {
            throw new NotFoundException("Problem not found");
        }
        existing.setTitle(p.getTitle());
        existing.setDescription(p.getDescription());
        existing.setTimeLimitMs(p.getTimeLimitMs());
        existing.setMemoryLimitKb(p.getMemoryLimitKb());
        existing.setDifficulty(p.getDifficulty());
        existing.setConstraints(p.getConstraints());
        existing.setExamplesJson(p.getExamplesJson());
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

    public Page<Problem> getAllPaginated(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (title == null || title.isBlank()) {
            return repo.findAll(pageable);
        }
        return repo.findByTitleContainingIgnoreCase(title, pageable);
    }
}
