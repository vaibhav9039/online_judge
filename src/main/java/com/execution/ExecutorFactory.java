package com.execution;

import org.springframework.stereotype.Component;

@Component
public class ExecutorFactory {

    private final JavaExecutor javaExecutor;
    private final CppExecutor cppExecutor;

    public ExecutorFactory(JavaExecutor javaExecutor, CppExecutor cppExecutor) {
        this.javaExecutor = javaExecutor;
        this.cppExecutor = cppExecutor;
    }

    public LanguageExecutor getExecutor(String language) {
        return switch (language.toUpperCase()) {
            case "JAVA" -> javaExecutor;
            case "CPP" -> cppExecutor;
            default -> throw new IllegalArgumentException("Unsupported language");
        };
    }
}
