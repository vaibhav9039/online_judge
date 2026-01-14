package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseResult {
    private Long testCaseId;
    private String status;
    private long timeMs;
    private long memoryKb;
}
