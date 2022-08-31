package ptithcm.onlinejudge.controller.frontend;

import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.dto.TeacherDTO;
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
    @PostMapping("/login-to-other")
    public String loginWithUsernameAndPassword(@ModelAttribute("user") UserLogin user, HttpSession session) {
        boolean isStudent = false, isTeacher = false;
        for (StudentDTO student: Data.studentList) {
            if (student.getStudentId().equals(user.getUsername()) && student.getStudentPassword().equals(user.getPassword())) {
                isStudent = true;
                break;
            }
        }
        for (TeacherDTO teacher: Data.teacherList) {
            if (teacher.getTeacherId().equals(user.getUsername()) && teacher.getTeacherPassword().equals(user.getPassword())) {
                isTeacher = true;
                break;
            }
        }
        if (isTeacher) {
            session.setAttribute("user", user);
            return "redirect:/admin/problems";
        }
        if (isStudent) {
            session.setAttribute("user", user);
            return "redirect:/student/problems";
        }
        return "redirect:/error";
    }
}
