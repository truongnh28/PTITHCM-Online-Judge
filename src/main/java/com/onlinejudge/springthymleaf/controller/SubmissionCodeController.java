package com.onlinejudge.springthymleaf.controller;

import com.onlinejudge.springthymleaf.data.Data;
import com.onlinejudge.springthymleaf.dto.SubmissionCodeDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SubmissionCodeController{
    @GetMapping("/submission/{codeId}")
    public String getSubmissionCode(@PathVariable("codeId") String codeId, Model model) {
        SubmissionCodeDTO submissionCode = new SubmissionCodeDTO();
        for (SubmissionCodeDTO submissionCodeDTO: Data.submissionCodeList) {
            if (submissionCodeDTO.getCodeId().equals(codeId)) {
                submissionCode = submissionCodeDTO;
                break;
            }
        }
        model.addAttribute("submissionCode", submissionCode);
        return "submission-code";
    }
}
