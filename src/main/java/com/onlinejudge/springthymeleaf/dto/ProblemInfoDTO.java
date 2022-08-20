package com.onlinejudge.springthymeleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemInfoDTO {
    private String id;
    private String title;
    private boolean solved;
}
