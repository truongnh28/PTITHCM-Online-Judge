package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/admin/subject")
public class AdminSubjectController {
    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private SubjectManagementService subjectManagementService;

    // SUBJECT PAGE
    @GetMapping("")
    public String showSubjectPage(Model model) {
        model.addAttribute("pageTitle", "Môn học");
        List<SubjectDTO> subjects = ((List<Subject>) subjectManagementService.getAllSubject().getData())
                .stream().map(item -> subjectMapper.entityToDTO(item)).toList();
        model.addAttribute("subjectAdd", new SubjectDTO());
        model.addAttribute("subjects", subjects);
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

    // SEARCH SUBJECT
    @PostMapping("")
    public String searchSubject(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("pageTitle", "Môn học");
        List<SubjectDTO> subjects = ((List<Subject>) subjectManagementService.searchByIdOrName(keyword).getData())
                .stream().map(item -> subjectMapper.entityToDTO(item)).toList();
        model.addAttribute("subjectAdd", new SubjectDTO());
        model.addAttribute("subjects", subjects);
        return "/admin/subject/subject";
    }

    // EDIT PAGE
    @GetMapping("/{id}/edit")
    public String showEditSubjectPage(@PathVariable("id") String id, Model model) {
        model.addAttribute("pageTitle", "Chỉnh sửa môn học");
        ResponseObject getSubjectByIdResponse = subjectManagementService.getSubjectById(id);
        if (!getSubjectByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        SubjectDTO subject = subjectMapper.entityToDTO((Subject) getSubjectByIdResponse.getData());
        model.addAttribute("subject", subject);
        return "/admin/subject/subject-edit";
    }

    // EDIT SUBJECT
    @PostMapping("/{id}/edit")
    public String editSubject(@PathVariable("id") String id, @ModelAttribute("subject") SubjectDTO subject) {
        ResponseObject editSubjectResponse = subjectManagementService.editSubject(id, subject);
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
}
