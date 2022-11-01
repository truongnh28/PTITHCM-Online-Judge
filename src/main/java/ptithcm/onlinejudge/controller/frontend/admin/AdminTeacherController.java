package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.mapper.TeacherMapper;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.TeacherManagementService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/teacher")
public class AdminTeacherController {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TeacherManagementService teacherManagementService;
    @GetMapping("")
    public String showTeacherManagementPage(Model model) {
        return "redirect:/admin/teacher/page/1";
    }

    @GetMapping("/page/{page}")
    public String showTeacherPaginationPage(@PathVariable("page") int page, @Param("keyword") String keyword, Model model) {
        model.addAttribute("pageTitle", "Giáo viên");
        Map<String, Object> response = (Map<String, Object>) teacherManagementService.getAllTeachersExceptAdmin(page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) teacherManagementService.searchTeachersByKeyWordExceptAdmin(keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<TeacherDTO> teachers = getTeachers(response).stream().map(item -> teacherMapper.entityToDTO(item)).toList();
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        model.addAttribute("teacherAdd", new TeacherDTO());
        model.addAttribute("teachers", teachers);
        model.addAttribute("pageUrlPrefix", "/admin/teacher");
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        return "/admin/teacher/teacher";
    }

    @PostMapping("/add")
    public String addTeacher(@ModelAttribute("teacherAdd") TeacherDTO teacher) {
        ResponseObject responseAddTeacher = teacherManagementService.addTeacher(teacher);
        if (!responseAddTeacher.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/teacher";
    }

    // edit
    @PostMapping("/edit")
    public String editTeacher(TeacherDTO teacherDTO) {
        ResponseObject editTeacherResponse = teacherManagementService.editTeacher(teacherDTO);
        if (!editTeacherResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/teacher";
    }

    // lock
    @GetMapping("/{id}/lock")
    public String lockTeacher(@PathVariable("id") String teacherId) {
        ResponseObject lockTeacherResponse = teacherManagementService.lockTeacher(teacherId);
        if (!lockTeacherResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/teacher";
    }

    // unlock
    @GetMapping("/{id}/unlock")
    public String unlockTeacher(@PathVariable("id") String teacherId) {
        ResponseObject lockTeacherResponse = teacherManagementService.unlockTeacher(teacherId);
        if (!lockTeacherResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/teacher";
    }

    // reset password
    @GetMapping("/{id}/reset")
    public String resetPasswordTeacher(@PathVariable("id") String teacherId) {
        ResponseObject lockTeacherResponse = teacherManagementService.resetPasswordTeacher(teacherId);
        if (!lockTeacherResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/teacher";
    }

    private List<Teacher> getTeachers(Map<String, Object> response) {
        return (List<Teacher>) response.getOrDefault("data", null);
    }
}
