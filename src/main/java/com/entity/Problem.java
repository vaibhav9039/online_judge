package com.entity;

import jakarta.persistence.*;
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
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String examplesJson;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String constraints;

    private long timeLimitMs = 2000;

    private long memoryLimitKb = 256 * 1024;

    private String difficulty;

    private Long createdByUserId;
}
