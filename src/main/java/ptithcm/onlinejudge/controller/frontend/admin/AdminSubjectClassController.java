package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.SubjectClassDTO;
import ptithcm.onlinejudge.mapper.SubjectClassMapper;
import ptithcm.onlinejudge.model.entity.Subject;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.SubjectClassManagementService;
import ptithcm.onlinejudge.services.SubjectManagementService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/subject/{subjectId}/class")
public class AdminSubjectClassController {
    @Autowired
    private SubjectManagementService subjectManagementService;
    @Autowired
    private SubjectClassMapper subjectClassMapper;
    @Autowired
    private SubjectClassManagementService subjectClassManagementService;

    @GetMapping("")
    public String showSubjectClassPage(@PathVariable("subjectId") String subjectId, Model model) {
        model.addAttribute("pageTitle", "Lớp");
        List<SubjectClassDTO> classes = ((List<SubjectClass>) subjectClassManagementService.getAllClassesBySubjectId(subjectId).getData())
                .stream().map(item -> subjectClassMapper.entityToDTO(item)).collect(Collectors.toList());
        Subject subject = (Subject) subjectManagementService.getSubjectById(subjectId).getData();
        model.addAttribute("classes", classes);
        model.addAttribute("subjectName", subject.getSubjectName());
        SubjectClassDTO subjectClass = new SubjectClassDTO();
        subjectClass.setSubjectClassId(subjectId + "-");
        model.addAttribute("classAdd", subjectClass);
        return "/admin/subject/subject-class";
    }

    @PostMapping("/add")
    public String addSubject(@PathVariable("subjectId") String subjectId, @ModelAttribute("classAdd") SubjectClassDTO subjectClass) {
        ResponseObject addSubjectResponse = subjectClassManagementService.addSubjectClass(subjectId, subjectClass);
        if (!addSubjectResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class";
    }

    @PostMapping("")
    public String searchSubjectByKeyword(@PathVariable("subjectId") String subjectId, @RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("pageTitle", "Lớp");
        List<SubjectClassDTO> classes = ((List<SubjectClass>) subjectClassManagementService.searchClassesByIdOrName(subjectId, keyword).getData())
                .stream().map(item -> subjectClassMapper.entityToDTO(item)).collect(Collectors.toList());
        Subject subject = (Subject) subjectManagementService.getSubjectById(subjectId).getData();
        model.addAttribute("classes", classes);
        model.addAttribute("subjectName", subject.getSubjectName());
        SubjectClassDTO subjectClass = new SubjectClassDTO();
        subjectClass.setSubjectClassId(subjectId + "-");
        model.addAttribute("classAdd", subjectClass);
        return "/admin/subject/subject-class";
    }

    @GetMapping("/{classId}/edit")
    public String showEditClassPage(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, Model model) {
        model.addAttribute("pageTitle", "Chỉnh sửa lớp");
        ResponseObject getClassByIdResponse = subjectClassManagementService.getClassById(classId);
        if (!getClassByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        SubjectClassDTO subjectClass = subjectClassMapper.entityToDTO((SubjectClass) getClassByIdResponse.getData());
        model.addAttribute("subjectClass", subjectClass);
        return "/admin/subject/subject-class-edit";
    }

    @PostMapping("/{classId}/edit")
    public String editClass(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @ModelAttribute("subjectClass") SubjectClassDTO subjectClass) {
        ResponseObject editClassResponse = subjectClassManagementService.editSubjectClass(classId, subjectClass);
        if (!editClassResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class";
    }

    @GetMapping("/{classId}/lock")
    public String lockClass(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId) {
        ResponseObject lockClassResponse = subjectClassManagementService.lockClass(classId);
        if (!lockClassResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class";
    }

    @GetMapping("/{classId}/unlock")
    public String unlockClass(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId) {
        ResponseObject unlockClassResponse = subjectClassManagementService.unlockClass(classId);
        if (!unlockClassResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class";
    }
}
