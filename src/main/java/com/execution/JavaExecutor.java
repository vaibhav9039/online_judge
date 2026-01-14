package com.execution;

import com.dto.ExecutionResult;
import org.springframework.stereotype.Component;

@Component
public class JavaExecutor implements LanguageExecutor{
    private final DockerRunner runner;

    public JavaExecutor(DockerRunner runner) {
        this.runner = runner;
    }

    public ExecutionResult execute(String code,
                                   String input,
                                   long timeLimitMs) {
        return runner.run("JAVA", code, input, timeLimitMs);
    }
}
