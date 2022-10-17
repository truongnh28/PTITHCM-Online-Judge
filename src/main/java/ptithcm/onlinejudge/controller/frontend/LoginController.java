package ptithcm.onlinejudge.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import ptithcm.onlinejudge.dto.LoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ptithcm.onlinejudge.helper.SHA256Helper;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.StudentRepository;
import ptithcm.onlinejudge.repository.TeacherRepository;
import ptithcm.onlinejudge.services.LoginService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;
    @GetMapping("/")
    public String toPageLogin(Model model) {
        LoginDTO loginDTO = new LoginDTO();
        model.addAttribute("user", loginDTO);
        return "login";
    }
    @PostMapping("/login-to-other")
    public String loginWithUsernameAndPassword(@ModelAttribute("user") LoginDTO user, HttpSession session) {
        ResponseObject loginResponse = loginService.login(user);
        if (loginResponse.getStatus().equals(HttpStatus.OK)) {
            Object data = loginResponse.getData();
            session.setAttribute("user", user.getUsername());
            if (data.getClass().equals(Student.class)) {
                session.setAttribute("role", "student");
                return "redirect:/student/home";
            }
            if (data.getClass().equals(Teacher.class)) {
                Teacher teacher = (Teacher) data;
                if (teacher.getRole().getId() == 1) {
                    session.setAttribute("role", "admin");
                    return "redirect:/admin/home";
                } else {
                    session.setAttribute("role", "teacher");
                    return "redirect:/teacher/home";
                }
            }
        }
        return "redirect:/error";
    }
}
