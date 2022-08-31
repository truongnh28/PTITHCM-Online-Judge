package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.TeacherDTO;

@Controller
public class AdminAddTeacherController {
    @GetMapping("/add-teacher")
    public String getAddTeacherPage(Model model) {
        TeacherDTO teacher = new TeacherDTO();
        model.addAttribute("teacher", teacher);
        return "admin/add-teacher";
    }
    @PostMapping("/admin/teacher/add")
    public String addTeacher(@ModelAttribute("teacher") TeacherDTO teacher) {
        teacher.setActive(true);
        Data.teacherList.add(teacher);
        return "redirect:/admin/teachers";
    }
}
