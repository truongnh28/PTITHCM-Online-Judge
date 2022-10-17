package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
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
import ptithcm.onlinejudge.services.TeacherManagementService;

import java.util.List;
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

    @GetMapping("")
    public String showTeachersOwnClass(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, Model model) {
        model.addAttribute("pageTitle", "Giáo viên quản lý lớp");
        List<TeacherDTO> teachers = ((List<Teacher>) teacherManagementService.getTeachersOwnClass(classId).getData()).stream().map(item -> teacherMapper.entityToDTO(item)).collect(Collectors.toList());
        SubjectClass subjectClass = (SubjectClass) subjectClassManagementService.getClassById(classId).getData();
        model.addAttribute("className", subjectClass.getSubjectClassName());
        model.addAttribute("teachers", teachers);
        return "/admin/subject/subject-class-teacher";
    }

    @GetMapping("/{teacherId}/remove")
    public String removeTeacherFromClass(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("teacherId") String teacherId) {
        ResponseObject removeTeacherFromClassResponse = subjectClassHasTeacherService.removeTeacherFromClass(teacherId, classId);
        if (!removeTeacherFromClassResponse.getStatus().equals(HttpStatus.OK)) return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/teacher";
    }

    @PostMapping("")
    public String searchTeachersOwnClass(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("pageTitle", "Giáo viên quản lý lớp");
        List<TeacherDTO> teachers = ((List<Teacher>) teacherManagementService.searchTeachersOwnClassById(classId, keyword).getData()).stream().map(item -> teacherMapper.entityToDTO(item)).collect(Collectors.toList());
        SubjectClass subjectClass = (SubjectClass) subjectClassManagementService.getClassById(classId).getData();
        model.addAttribute("className", subjectClass.getSubjectClassName());
        model.addAttribute("teachers", teachers);
        return "/admin/subject/subject-class-teacher";
    }

    @GetMapping("/add")
    public String showAddTeacherToClassPage(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, Model model) {
        model.addAttribute("pageTitle", "Thêm giáo viên");
        List<TeacherDTO> teachers = ((List<Teacher>) teacherManagementService.getTeachersNotOwnClass(classId).getData()).stream().map(item -> teacherMapper.entityToDTO(item)).collect(Collectors.toList());
        model.addAttribute("teachers", teachers);
        return "/admin/subject/subject-class-add-teacher";
    }

    @GetMapping("/{teacherId}/add")
    public String addTeacherToClass(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("teacherId") String teacherId) {
        ResponseObject addTeacherToClassResponse = subjectClassHasTeacherService.addTeacherToClass(teacherId, classId);
        if (!addTeacherToClassResponse.getStatus().equals(HttpStatus.OK)) return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/teacher/add";
    }

    @PostMapping("/add")
    public String searchTeacherNotOwnClass(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("pageTitle", "Thêm giáo viên");
        List<TeacherDTO> teachers = ((List<Teacher>) teacherManagementService.searchTeachersNotOwnClassById(classId, keyword).getData()).stream().map(item -> teacherMapper.entityToDTO(item)).collect(Collectors.toList());
        model.addAttribute("teachers", teachers);
        return "/admin/subject/subject-class-add-teacher";
    }
}
