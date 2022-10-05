package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.mapper.TeacherMapper;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.TeacherManagementService;

import java.util.List;
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
        model.addAttribute("pageTitle", "Giáo viên");
        List<TeacherDTO> teachers = ((List<Teacher>) teacherManagementService.getAllTeachersExceptAdmin().getData())
                .stream().map(item -> teacherMapper.entityToDTO(item)).collect(Collectors.toList());
        TeacherDTO teacherAdd = new TeacherDTO();
        model.addAttribute("teachers", teachers);
        model.addAttribute("teacherAdd", teacherAdd);
        return "/admin/teacher/teacher";
    }

    @PostMapping("/add")
    public String addTeacher(@ModelAttribute("teacherAdd") TeacherDTO teacher) {
        ResponseObject responseAddTeacher = teacherManagementService.addTeacher(teacher);
        if (!responseAddTeacher.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/teacher";
    }

    @PostMapping("")
    public String showTeacherAfterSearching(Model model, @RequestParam("keyword") String keyword) {
        model.addAttribute("pageTitle", "Giáo viên");
        List<TeacherDTO> teachers = ((List<Teacher>) teacherManagementService.searchTeachersByKeyWordExceptAdmin(keyword).getData())
                .stream().map(item -> teacherMapper.entityToDTO(item)).toList();
        TeacherDTO teacherAdd = new TeacherDTO();
        model.addAttribute("keyword", keyword);
        model.addAttribute("teachers", teachers);
        model.addAttribute("teacherAdd", teacherAdd);
        return "/admin/teacher/teacher";
    }

    // edit

    @GetMapping("/{id}/edit")
    public String showEditTeacherPage(@PathVariable("id") String teacherId, Model model) {
        ResponseObject getTeacherByIdResponse = teacherManagementService.getTeacherById(teacherId);
        if (!getTeacherByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        TeacherDTO teacherDTO = teacherMapper.entityToDTO((Teacher) getTeacherByIdResponse.getData());
        model.addAttribute("teacher", teacherDTO);
        return "/admin/teacher/teacher-edit";
    }

    @PostMapping("/{id}/edit")
    public String editTeacher(@PathVariable("id") String teacherId, @ModelAttribute("teacher") TeacherDTO teacherDTO) {
        ResponseObject editTeacherResponse = teacherManagementService.editTeacher(teacherId, teacherDTO);
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
}
