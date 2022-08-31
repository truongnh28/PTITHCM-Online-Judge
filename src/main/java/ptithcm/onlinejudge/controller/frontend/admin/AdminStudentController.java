package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.StudentDTO;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminStudentController {
    @GetMapping("admin/students")
    public String getStudentList(Model model) {
        List<StudentDTO> studentList = new ArrayList<>();
        for (StudentDTO student: Data.studentList) {
            if (student.isActive())
                studentList.add(student);
        }
        model.addAttribute("students", studentList);
        return "/admin/students";
    }
}
