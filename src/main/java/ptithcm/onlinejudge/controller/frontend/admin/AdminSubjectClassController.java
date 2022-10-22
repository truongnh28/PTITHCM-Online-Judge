package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
import java.util.Map;
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
    public String showClassOfSubject(@PathVariable("subjectId") String subjectId) {
        if (!checkSubjectId(subjectId))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/page/1";
    }

    @GetMapping("/page/{page}")
    public String showSubjectClassPage(@PathVariable("subjectId") String subjectId, @PathVariable("page") int page, @Param("keyword") String keyword, Model model) {
        if (!checkSubjectId(subjectId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Lớp");
        Map<String, Object> response = (Map<String, Object>) subjectClassManagementService.getAllClassesOfSubject(subjectId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) subjectClassManagementService.searchClassesOfSubjectByKeyword(subjectId, keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = String.format("/admin/subject/%s/class", subjectId);
        List<SubjectClassDTO> classes = getSubjectClasses(response).stream().map(item -> subjectClassMapper.entityToDTO(item)).collect(Collectors.toList());
        Subject subject = (Subject) subjectManagementService.getSubjectById(subjectId).getData();
        model.addAttribute("classes", classes);
        model.addAttribute("subjectName", subject.getSubjectName());
        SubjectClassDTO subjectClass = new SubjectClassDTO();
        subjectClass.setSubjectClassId(subjectId + "-");
        model.addAttribute("classAdd", subjectClass);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        return "/admin/subject/subject-class";
    }

    @PostMapping("/add")
    public String addSubject(@PathVariable("subjectId") String subjectId, @ModelAttribute("classAdd") SubjectClassDTO subjectClass) {
        if (!checkSubjectId(subjectId))
            return "redirect:/error";
        ResponseObject addSubjectResponse = subjectClassManagementService.add(subjectId, subjectClass);
        if (!addSubjectResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class";
    }

    @PostMapping("/edit")
    public String editClass(@PathVariable("subjectId") String subjectId, SubjectClassDTO subjectClass) {
        if (!checkSubjectId(subjectId))
            return "redirect:/error";
        ResponseObject editClassResponse = subjectClassManagementService.edit(subjectClass);
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

    private boolean checkSubjectId(String subjectId) {
        ResponseObject responseObject = subjectManagementService.getSubjectById(subjectId);
        return responseObject.getStatus().equals(HttpStatus.OK);
    }

    private List<SubjectClass> getSubjectClasses(Map<String, Object> response) {
        return (List<SubjectClass>) response.getOrDefault("data", null);
    }
}
