package ptithcm.onlinejudge.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.dto.UserLogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ptithcm.onlinejudge.helper.SHA256Helper;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.repository.TeacherRepository;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    private TeacherRepository teacherRepository;
    @GetMapping("/")
    public String toPageLogin(Model model) {
        UserLogin userLogin = new UserLogin();
        model.addAttribute("user", userLogin);
        return "login";
    }
    @PostMapping("/login-to-other")
    public String loginWithUsernameAndPassword(@ModelAttribute("user") UserLogin user, HttpSession session) {
        Optional<Teacher> foundTeacher = teacherRepository.findById(user.getUsername());
        if (foundTeacher.isPresent()) {
            Teacher teacher = foundTeacher.get();
            SHA256Helper sha256Helper = new SHA256Helper();
            if (!teacher.getPassword().equals(sha256Helper.hash(user.getPassword())))
                return "redirect:/error";
            session.setAttribute("user", user);
            return "redirect:/teacher/problem";
        }
        return "redirect:/error";
    }
}
