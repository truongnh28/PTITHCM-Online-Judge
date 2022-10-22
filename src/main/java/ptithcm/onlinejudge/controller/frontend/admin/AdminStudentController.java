package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.mapper.StudentMapper;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.StudentManagementService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/student")
public class AdminStudentController {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StudentManagementService studentManagementService;

    @GetMapping("")
    public String showStudentManagementPage() {
        return "redirect:/admin/student/page/1";
    }

    @GetMapping("/page/{page}")
    public String showStudentPaginationPage(@PathVariable("page") int page, @Param("keyword") String keyword, Model model) {
        model.addAttribute("pageTitle", "Sinh viên");
        Map<String, Object> response = (Map<String, Object>) studentManagementService.getAllStudent(page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) studentManagementService.searchStudentById(keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<StudentDTO> students = getStudents(response).stream().map(item -> studentMapper.entityToDTO(item)).toList();
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        model.addAttribute("studentAdd", new StudentDTO());
        model.addAttribute("students", students);
        model.addAttribute("pageUrlPrefix", "/admin/student");
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        return "/admin/student/student";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute("studentAdd") StudentDTO student) {
        ResponseObject addStudentResponse = studentManagementService.addStudent(student);
        if (!addStudentResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/student";
    }

    // edit
    @PostMapping("/edit")
    public String editStudent(StudentDTO student) {
        ResponseObject editStudentResponse = studentManagementService.editStudent(student);
        if (!editStudentResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/student";
    }

    @GetMapping("/{id}/reset")
    public String resetPasswordStudent(@PathVariable("id") String studentId) {
        ResponseObject resetPasswordStudentResponse = studentManagementService.resetPassword(studentId);
        if (!resetPasswordStudentResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/student";
    }

    @GetMapping("/{id}/lock")
    public String lockStudent(@PathVariable("id") String studentId) {
        ResponseObject lockStudentResponse = studentManagementService.lockStudent(studentId);
        if (!lockStudentResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/student";
    }

    @GetMapping("/{id}/unlock")
    public String unlockStudent(@PathVariable("id") String studentId) {
        ResponseObject unlockStudentResponse = studentManagementService.unlockStudent(studentId);
        if (!unlockStudentResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/student";
    }

    private List<Student> getStudents(Map<String, Object> response) {
        return (List<Student>) response.getOrDefault("data", null);
    }
}
