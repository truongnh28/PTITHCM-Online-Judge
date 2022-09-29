package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.dto.StudentShowDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teacher/student")
public class AdminStudentController {
    private Optional<StudentDTO> findStudentByStudentId(String studentId) {
        StudentDTO studentDTO = new StudentDTO();
        boolean isExisted = false;
        for (StudentDTO student: Data.studentList) {
            if (student.getStudentId().equals(studentId)) {
                studentDTO = student;
                isExisted = true;
                break;
            }
        }
        return (isExisted) ? Optional.of(studentDTO) : Optional.empty();
    }
    @GetMapping("")
    public String showStudentListPage(Model model) {
        model.addAttribute("pageTitle", "Danh sách sinh viên");
        List<StudentDTO> studentList = new ArrayList<>();
        for (StudentDTO student : Data.studentList)
            if (student.isActive())
                studentList.add(student);
        model.addAttribute("students", studentList);
        return "/teacher/student";
    }

    @GetMapping("/add")
    public String showStudentAddPage(Model model) {
        model.addAttribute("pageTitle", "Thêm sinh viên");
        StudentDTO student = new StudentShowDTO();
        model.addAttribute("student", student);
        return "/teacher/student-add";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute("student") StudentDTO student) {
        student.setActive(true);
        Data.studentList.add(student);
        return "redirect:/teacher/student";
    }

    @GetMapping("/{studentId}/edit")
    public String showAddStudentForm(@PathVariable("studentId") String studentId, Model model) {
        model.addAttribute("pageTitle", "Chỉnh sửa sinh viên");
        Optional<StudentDTO> foundStudent = findStudentByStudentId(studentId);
        if (foundStudent.isEmpty())
            return "redirect:/error";
        model.addAttribute("student", foundStudent.get());
        return "/teacher/student-edit";
    }

    @PostMapping("/{studentId}/edit")
    public String editStudent(@PathVariable("studentId") String studentId, @ModelAttribute("student") StudentDTO student) {
        Optional<StudentDTO> foundStudent = findStudentByStudentId(studentId);
        if (foundStudent.isEmpty())
            return "redirect:/error";
        StudentDTO newStudent = foundStudent.get();
        newStudent.setStudentFirstName(student.getStudentFirstName());
        newStudent.setStudentLastName(student.getStudentLastName());
        return "redirect:/teacher/student";
    }

    @GetMapping("/{studentId}/delete")
    public String deleteStudent(@PathVariable("studentId") String studentId) {
        Optional<StudentDTO> foundStudent = findStudentByStudentId(studentId);
        if (foundStudent.isEmpty())
            return "redirect:/error";
        StudentDTO deleteStudent = foundStudent.get();
        deleteStudent.setActive(false);
        return "redirect:/teacher/student";
    }
}
