package ptithcm.onlinejudge.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import ptithcm.onlinejudge.dto.Login;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ptithcm.onlinejudge.helper.SHA256Helper;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.repository.StudentRepository;
import ptithcm.onlinejudge.repository.TeacherRepository;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @GetMapping("/")
    public String toPageLogin(Model model) {
        Login login = new Login();
        model.addAttribute("user", login);
        return "login";
    }
    @PostMapping("/login-to-other")
    public String loginWithUsernameAndPassword(@ModelAttribute("user") Login user, HttpSession session) {
        Optional<Teacher> foundTeacher = teacherRepository.findById(user.getUsername());
        Optional<Student> foundStudent = studentRepository.findById(user.getUsername());
        if (foundTeacher.isPresent()) {
            Teacher teacher = foundTeacher.get();
            if (!teacher.getPassword().equals(SHA256Helper.hash(user.getPassword())))
                return "redirect:/error";
            session.setAttribute("user", user);
            return "redirect:/teacher/problem";
        }
        if (foundStudent.isPresent()) {
            Student student = foundStudent.get();
            if (!student.getPassword().equals(SHA256Helper.hash(user.getPassword())))
                return "redirect:/error";
            session.setAttribute("user", user);
            return "redirect:/student/contest";
        }
        return "redirect:/error";
    }
}
