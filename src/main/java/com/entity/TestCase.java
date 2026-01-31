package com.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long problemId;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String inputData;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String expectedOutput;
}
