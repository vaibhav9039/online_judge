package com.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Submission {
    @Id
    @GeneratedValue
    private Long id;
    private Long problemId;
    private String language;
    @Lob
    private String code;
    private int passed;
    private int total;
    private String status;
    private long executionTimeMs;
    private long memoryKb;
    private long timeLimitMs;
}
