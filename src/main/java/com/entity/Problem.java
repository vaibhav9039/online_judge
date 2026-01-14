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
public class Problem {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Lob
    private String description;

    private long timeLimitMs = 2000;

    private long memoryLimitKb = 256 * 1024;

    private String difficulty;

    private Long createdByUserId;
}
