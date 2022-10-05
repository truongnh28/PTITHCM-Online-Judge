package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/home")
public class AdminHomeController {
    @GetMapping("")
    public String showAdminHomePage(Model model) {
        model.addAttribute("pageTitle", "Trang chủ");
        return "/admin/home";
    }
}
