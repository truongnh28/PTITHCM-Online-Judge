package com.onlinejudge.springthymeleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionCodeDTO {
    private String codeId;
    private String sourceCode;
    private int timeExec;
    private int memoryUsed;
    private String username;
    private String language;
    private String status;
}
