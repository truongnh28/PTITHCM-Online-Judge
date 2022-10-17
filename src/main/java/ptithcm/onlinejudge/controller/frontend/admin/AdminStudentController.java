package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/admin/student")
public class AdminStudentController {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StudentManagementService studentManagementService;

    @GetMapping("")
    public String showStudentManagementPage(Model model) {
        model.addAttribute("pageTitle", "Sinh viên");
        List<StudentDTO> students = ((List<Student>) studentManagementService.getAllStudent().getData())
                .stream().map(item -> studentMapper.entityToDTO(item)).toList();
        model.addAttribute("students", students);
        model.addAttribute("studentAdd", new StudentDTO());
        return "/admin/student/student";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute("studentAdd") StudentDTO student) {
        ResponseObject addStudentResponse = studentManagementService.addStudent(student);
        if (!addStudentResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/student";
    }

    @PostMapping("")
    public String searchStudent(Model model, @RequestParam("keyword") String keyword) {
        model.addAttribute("pageTitle", "Sinh viên");
        List<StudentDTO> students = ((List<Student>) studentManagementService.getAllStudent().getData())
                .stream().map(item -> studentMapper.entityToDTO(item)).toList();
        model.addAttribute("students", students);
        model.addAttribute("studentAdd", new StudentDTO());
        return "/admin/student/student";
    }

    // show edit page
    @GetMapping("/{id}/edit")
    public String showEditStudentPage(@PathVariable("id") String studentId, Model model) {
        model.addAttribute("pageTitle", "Chỉnh sửa sinh viên");
        ResponseObject getStudentByIdResponse = studentManagementService.getStudentById(studentId);
        if (!getStudentByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        StudentDTO student = studentMapper.entityToDTO((Student) getStudentByIdResponse.getData());
        model.addAttribute("student", student);
        return "/admin/student/student-edit";
    }

    // edit
    @PostMapping("/{id}/edit")
    public String editStudent(@PathVariable("id") String studentId, @ModelAttribute("student") StudentDTO student) {
        ResponseObject editStudentResponse = studentManagementService.editStudent(studentId, student);
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
}
