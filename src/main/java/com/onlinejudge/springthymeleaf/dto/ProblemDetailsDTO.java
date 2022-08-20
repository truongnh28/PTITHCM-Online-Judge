package com.onlinejudge.springthymeleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemDetailsDTO {
    private String id;
    private String title;
    private String description;
    private double timeLimit;
    private int memoryLimit;
    private int score;
}
