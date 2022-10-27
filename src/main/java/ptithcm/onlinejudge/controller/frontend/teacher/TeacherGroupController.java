package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
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
import java.util.Map;
import java.util.stream.Collectors;

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
    public String showGroup(@PathVariable("classId") String classId) {
        if (!checkValid(classId))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/page/1";
    }

    @GetMapping("/page/{page}")
    public String showGroupPage(@PathVariable("classId") String classId, @PathVariable("page") int page, @Param("keyword") String keyword, Model model) {
        if (!checkValid(classId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Danh sách nhóm thực hành");
        SubjectClass subjectClass = (SubjectClass) subjectClassManagementService.getClassById(classId).getData();
        Map<String, Object> response = (Map<String, Object>) subjectClassGroupManagementService.getGroupsOfClassActive(classId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) subjectClassGroupManagementService.searchGroupOfClassActive(classId, keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = String.format("/teacher/class/%s/group", classId);
        List<SubjectClassGroupDTO> groups = getGroups(response).stream().map(item -> subjectClassGroupMapper.entityToDTO(item)).collect(Collectors.toList());
        model.addAttribute("className", subjectClass.getSubjectClassName());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        model.addAttribute("groups", groups);
        return "/teacher/contest/group-of-class";
    }

    private boolean checkValid(String classId) {
        return subjectClassManagementService.getClassById(classId).getStatus().equals(HttpStatus.OK);
    }

    private List<SubjectClassGroup> getGroups(Map<String, Object> res) {
        return (List<SubjectClassGroup>) res.getOrDefault("data", null);
    }
}
