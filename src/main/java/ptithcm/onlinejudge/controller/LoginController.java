package ptithcm.onlinejudge.controller;

import ptithcm.onlinejudge.dto.UserLogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @GetMapping("/")
    public String toPageLogin(Model model) {
        UserLogin userLogin = new UserLogin();
        model.addAttribute("user", userLogin);
        return "login";
    }
    @PostMapping("/login-to-problem")
    public String loginWithUsernameAndPassword(@ModelAttribute("user") UserLogin user, HttpSession session) {
        session.setAttribute("user", user);
        return "redirect:/problems";
    }
}
