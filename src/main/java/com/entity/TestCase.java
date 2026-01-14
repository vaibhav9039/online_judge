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
public class TestCase {
    @Id
    @GeneratedValue
    private Long id;

    private Long problemId;

    @Lob
    private String inputData;

    @Lob
    private String expectedOutput;
}
