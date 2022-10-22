package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.SubjectDTO;
import ptithcm.onlinejudge.mapper.SubjectMapper;
import ptithcm.onlinejudge.model.entity.Subject;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.SubjectManagementService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/subject")
public class AdminSubjectController {
    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private SubjectManagementService subjectManagementService;

    // SUBJECT PAGE
    @GetMapping("")
    public String showSubjectPage() {
        return "redirect:/admin/subject/page/1";
    }

    @GetMapping("/page/{page}")
    public String showSubjectPagePagination(@PathVariable("page") int page, @Param("keyword") String keyword, Model model) {
        model.addAttribute("pageTitle", "Môn học");
        Map<String, Object> response = (Map<String, Object>) subjectManagementService.getAllSubject(page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) subjectManagementService.searchByIdOrName(keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<SubjectDTO> subjects = getSubjects(response).stream().map(item -> subjectMapper.entityToDTO(item)).toList();
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        model.addAttribute("subjectAdd", new SubjectDTO());
        model.addAttribute("subjects", subjects);
        model.addAttribute("pageUrlPrefix", "/admin/subject");
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        return "/admin/subject/subject";
    }

    // ADD SUBJECT
    @PostMapping("/add")
    public String addSubject(@ModelAttribute("subjectAdd") SubjectDTO subject) {
        ResponseObject addSubjectResponse = subjectManagementService.addSubject(subject);
        if (!addSubjectResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject";
    }

    // EDIT SUBJECT
    @PostMapping("/edit")
    public String editSubject(SubjectDTO subject) {
        ResponseObject editSubjectResponse = subjectManagementService.editSubject(subject);
        if (!editSubjectResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject";
    }

    // LOCK SUBJECT
    @GetMapping("/{id}/lock")
    public String lockSubject(@PathVariable("id") String id) {
        ResponseObject lockSubjectResponse = subjectManagementService.lockSubject(id);
        if (!lockSubjectResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject";
    }

    // UNLOCK SUBJECT
    @GetMapping("/{id}/unlock")
    public String unlockSubject(@PathVariable("id") String id) {
        ResponseObject unlockSubjectResponse = subjectManagementService.unlockSubject(id);
        if (!unlockSubjectResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject";
    }

    private List<Subject> getSubjects(Map<String, Object> response) {
        return (List<Subject>) response.getOrDefault("data", null);
    }
}
