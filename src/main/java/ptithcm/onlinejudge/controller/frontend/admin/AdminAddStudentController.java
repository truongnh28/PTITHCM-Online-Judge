package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.StudentDTO;

@Controller
public class AdminAddStudentController {
    @GetMapping("/add-student")
    public String getAddStudentPage(Model model) {
        StudentDTO student = new StudentDTO();
        model.addAttribute("student", student);
        return "admin/add-student";
    }
    @PostMapping("/admin/student/add")
    public String addStudent(@ModelAttribute("student") StudentDTO student) {
        student.setActive(true);
        student.setRole(Data.roleList.get(1));
        Data.studentList.add(student);
        return "redirect:/admin/students";
    }
}
