package com.execution;

import com.dto.ExecutionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DockerRunner {

    public ExecutionResult run(String lang, String code,
                               String input, long timeLimitMs) {

        long start = System.currentTimeMillis();

        try {
            Path dir = Files.createTempDirectory("oj");
            Files.write(dir.resolve(lang.equals("JAVA") ? "Main.java" : "main.cpp"), code.getBytes());
            Files.write(dir.resolve("input.txt"), input.getBytes());

            String image = lang.equals("JAVA") ? "java-runner" : "cpp-runner";

            ProcessBuilder pb = new ProcessBuilder(
                    "docker", "run", "--rm",
                    "--network", "none",
                    "--cpus", "0.5",
                    "-m", "256m",
                    "--memory-swap", "256m",
                    "--pids-limit", "64",
                    "-v", dir + ":/code",
                    image
            );

            Process p = pb.start();

            boolean finished = p.waitFor(timeLimitMs, TimeUnit.MILLISECONDS);

            if (!finished) {
                p.destroyForcibly();
                return new ExecutionResult("", timeLimitMs, 0, "TLE");
            }

            long time = System.currentTimeMillis() - start;
            int exitCode = p.exitValue();

            String stdout = new String(p.getInputStream().readAllBytes());
            String stderr = new String(p.getErrorStream().readAllBytes());

            if (exitCode == 100) {
                return new ExecutionResult(stderr, time, 0, "CE");
            }
            if (exitCode != 0) {
                return new ExecutionResult(stderr, time, 0, "RE");
            }

            long memoryKb = parseMemory(stderr);

            return new ExecutionResult(stdout, time, memoryKb, "AC");

        } catch (Exception e) {
            return new ExecutionResult(e.getMessage(), 0, 0, "RE");
        }
    }

    private long parseMemory(String stderr) {
        try {
            return Long.parseLong(stderr.trim());
        } catch (Exception e) {
            return 0;
        }
    }


    public String runGuest(String lang, String code, String input) {
        try {
            Path dir = Files.createTempDirectory("guest");

            if (lang.equalsIgnoreCase("JAVA"))
                Files.write(dir.resolve("Main.java"), code.getBytes());
            else
                Files.write(dir.resolve("main.cpp"), code.getBytes());

            Files.write(dir.resolve("input.txt"), input.getBytes());

            String image = lang.equalsIgnoreCase("JAVA") ? "java-runner" : "cpp-runner";

            ProcessBuilder pb = new ProcessBuilder(
                    "docker", "run", "--rm",
                    "--network", "none",
                    "--cpus", "0.25",
                    "-m", "128m",
                    "-v", dir.toAbsolutePath() + ":/code",
                    image
            );

            Process p = pb.start();
            p.waitFor(3, TimeUnit.SECONDS);

            String stdout = new String(p.getInputStream().readAllBytes());
            String stderr = new String(p.getErrorStream().readAllBytes());
            log.info(stderr);
            return stdout;


        } catch (Exception e) {
            return "ERROR";
        }
    }

}
