package com.execution;

import org.springframework.stereotype.Component;

import com.dto.ExecutionResult;

@Component
public class CppExecutor implements LanguageExecutor {

    private final DockerRunner dockerRunner;

    public CppExecutor(DockerRunner dockerRunner) {
        this.dockerRunner = dockerRunner;
    }

    @Override
    public ExecutionResult execute(String code, String input, long timeLimitMs) {
        return dockerRunner.run(
                "CPP",
                code,
                input,
                timeLimitMs
        );
    }
}
