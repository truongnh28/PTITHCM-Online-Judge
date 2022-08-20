package com.onlinejudge.springthymeleaf.controller;

import com.onlinejudge.springthymeleaf.data.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProblemsController{
    @GetMapping("/problems")
    public String getProblems(Model model) {
        model.addAttribute("problems", Data.problemList);
        return "problems";
    }
}
