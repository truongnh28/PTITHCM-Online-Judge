package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.mapper.TeacherMapper;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.SubjectClassHasTeacherManagementService;
import ptithcm.onlinejudge.services.SubjectClassManagementService;
import ptithcm.onlinejudge.services.SubjectManagementService;
import ptithcm.onlinejudge.services.TeacherManagementService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/subject/{subjectId}/class/{classId}/teacher")
public class AdminSubjectClassTeacherController {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TeacherManagementService teacherManagementService;
    @Autowired
    private SubjectClassManagementService subjectClassManagementService;
    @Autowired
    private SubjectClassHasTeacherManagementService subjectClassHasTeacherService;
    @Autowired
    private SubjectManagementService subjectManagementService;

    @GetMapping("")
    public String showTeachersOwnClass(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, Model model) {
        if (!checkValid(subjectId, classId)) return "redirect:/error";
        model.addAttribute("pageTitle", "Giáo viên quản lý lớp");
        List<TeacherDTO> teachers = ((List<Teacher>) teacherManagementService.getTeachersOwnClass(classId).getData()).stream().map(item -> teacherMapper.entityToDTO(item)).collect(Collectors.toList());
        SubjectClass subjectClass = (SubjectClass) subjectClassManagementService.getClassById(classId).getData();
        model.addAttribute("className", subjectClass.getSubjectClassName());
        model.addAttribute("teachers", teachers);
        return "/admin/subject/subject-class-teacher";
    }

    @GetMapping("/{teacherId}/remove")
    public String removeTeacherFromClass(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("teacherId") String teacherId) {
        if (!checkValid(subjectId, classId)) return "redirect:/error";
        ResponseObject removeTeacherFromClassResponse = subjectClassHasTeacherService.removeTeacherFromClass(teacherId, classId);
        if (!removeTeacherFromClassResponse.getStatus().equals(HttpStatus.OK)) return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/teacher";
    }

    @GetMapping("/add")
    public String showAddTeacherToClassPage(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId) {
        if (!checkValid(subjectId, classId)) return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/teacher/add/page/1";
    }

    @GetMapping("/add/page/{page}")
    public String showAddTeacherToClassPagination(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @Param("keyword") String keyword, @PathVariable("page") int page, Model model) {
        if (!checkValid(subjectId, classId)) return "redirect:/error";
        model.addAttribute("pageTitle", "Thêm giáo viên vào nhóm quản lý");
        Map<String, Object> response = (Map<String, Object>) teacherManagementService.getTeachersNotOwnClass(classId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) teacherManagementService.searchTeachersNotOwnClassByKeyword(classId, keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        String pageUrlPrefix = String.format("/admin/subject/%s/class/%s/teacher/add", subjectId, classId);
        List<TeacherDTO> teachers = getTeachers(response).stream().map(item -> teacherMapper.entityToDTO(item)).collect(Collectors.toList());
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        model.addAttribute("teachers", teachers);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        return "/admin/subject/subject-class-add-teacher";
    }

    @GetMapping("/{teacherId}/add")
    public String addTeacherToClass(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("teacherId") String teacherId) {
        if (!checkValid(subjectId, classId)) return "redirect:/error";
        ResponseObject addTeacherToClassResponse = subjectClassHasTeacherService.addTeacherToClass(teacherId, classId);
        if (!addTeacherToClassResponse.getStatus().equals(HttpStatus.OK)) return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/teacher/add";
    }

    private boolean checkValid(String subjectId, String classId) {
        ResponseObject getSubjectResponse = subjectManagementService.getSubjectById(subjectId);
        ResponseObject getClassResponse = subjectClassManagementService.getClassById(classId);
        return getClassResponse.getStatus().equals(HttpStatus.OK) && getSubjectResponse.getStatus().equals(HttpStatus.OK);
    }

    private List<Teacher> getTeachers(Map<String, Object> response) {
        return (List<Teacher>) response.getOrDefault("data", null);
    }
}
