package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.User;

@Controller
public class UserController {
  @GetMapping("/")
  public String toLogin(Model model) {
    User user = new User();
    model.addAttribute("user", user);
    return "login";
  }

  @PostMapping("/login-form")
  public ModelAndView loginWithUsernameAndPassword(@ModelAttribute("user") User user) {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("success");
    modelAndView.addObject("user", user);
    return modelAndView;
  }
}
