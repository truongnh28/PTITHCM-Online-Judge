package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.mapper.SubjectClassGroupMapper;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.services.SubjectClassGroupManagementService;
import ptithcm.onlinejudge.services.SubjectClassManagementService;

import java.util.List;

@Controller
@RequestMapping("/teacher/class/{classId}/group")
public class TeacherGroupController {
    @Autowired
    private SubjectClassManagementService subjectClassManagementService;
    @Autowired
    private SubjectClassGroupMapper subjectClassGroupMapper;
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;

    @GetMapping("")
    public String showGroupPage(@PathVariable("classId") String classId, Model model) {
        model.addAttribute("pageTitle", "Danh sách nhóm thực hành");
        SubjectClass subjectClass = (SubjectClass) subjectClassManagementService.getClassById(classId).getData();
        List<SubjectClassGroupDTO> groups = ((List<SubjectClassGroup>) subjectClassGroupManagementService.getGroupsOfClassActive(classId).getData())
                .stream().map(item -> subjectClassGroupMapper.entityToDTO(item)).toList();
        model.addAttribute("className", subjectClass.getSubjectClassName());
        model.addAttribute("groups", groups);
        return "/teacher/contest/group-of-class";
    }

    @PostMapping("")
    public String searchGroup(@PathVariable("classId") String classId, Model model, @RequestParam("keyword") String keyword) {
        model.addAttribute("pageTitle", "Danh sách nhóm thực hành");
        SubjectClass subjectClass = (SubjectClass) subjectClassManagementService.getClassById(classId).getData();
        List<SubjectClassGroupDTO> groups = ((List<SubjectClassGroup>) subjectClassGroupManagementService.searchGroupOfClassActive(classId, keyword).getData())
                .stream().map(item -> subjectClassGroupMapper.entityToDTO(item)).toList();
        model.addAttribute("className", subjectClass.getSubjectClassName());
        model.addAttribute("groups", groups);
        return "/teacher/contest/group-of-class";
    }
}
