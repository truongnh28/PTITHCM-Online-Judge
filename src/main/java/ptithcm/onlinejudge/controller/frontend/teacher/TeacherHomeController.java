package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher/home")
public class TeacherHomeController {
    @GetMapping("")
    public String showHomePage(Model model) {
        model.addAttribute("pageTitle", "Trang chủ");
        return "/teacher/home";
    }
}
