package com.execution;

import com.dto.ExecutionResult;

public interface LanguageExecutor {
    ExecutionResult execute(String code, String input, long timeLimitMs);
}
