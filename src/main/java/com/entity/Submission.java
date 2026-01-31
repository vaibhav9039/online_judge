package com.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long problemId;
    private String language;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String code;
    private int passed;
    private int total;
    private String status;
    private long executionTimeMs;
    private long memoryKb;
    private long timeLimitMs;
}
