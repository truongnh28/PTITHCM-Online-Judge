package com.onlinejudge.springthymleaf.controller;

import com.onlinejudge.springthymleaf.data.Data;
import com.onlinejudge.springthymleaf.dto.SubmissionDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SubmissionController {
    @GetMapping("submissions")
    public String getSubmissions(Model model) {
        model.addAttribute("submissionList", Data.submissionList);
        return "submissions";
    }
}
