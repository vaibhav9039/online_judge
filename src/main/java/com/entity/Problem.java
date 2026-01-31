package com.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ElementCollection
    private List<String> tags;

    private long timeLimitMs = 2000;

    private long memoryLimitKb = 256 * 1024;

    private String difficulty;

    private Long createdByUserId;
}
