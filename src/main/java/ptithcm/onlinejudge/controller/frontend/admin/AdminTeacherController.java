package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.TeacherDTO;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminTeacherController {
    @GetMapping("/admin/teachers")
    public String getTeachers(Model model) {
        List<TeacherDTO> teachers = new ArrayList<>();
        for (TeacherDTO teacher: Data.teacherList) {
            if (teacher.isActive())
                teachers.add(teacher);
        }
        model.addAttribute("teachers", teachers);
        return "admin/teachers";
    }
}
