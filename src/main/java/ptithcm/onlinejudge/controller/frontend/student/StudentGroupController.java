package ptithcm.onlinejudge.controller.frontend.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.mapper.SubjectClassGroupMapper;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.StudentManagementService;
import ptithcm.onlinejudge.services.SubjectClassGroupManagementService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/group")
public class StudentGroupController {
    @Autowired
    private SubjectClassGroupMapper subjectClassGroupMapper;
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;

    @GetMapping("")
    public String showGroups(HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        return "redirect:/student/group/page/1";
    }

    @GetMapping("/page/{page}")
    public String showGroupsHaveStudent(Model model, @PathVariable("page") int page, @Param("keyword") String keyword, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        String studentId = session.getAttribute("user").toString();
        model.addAttribute("pageTitle", "Nhóm thực hành");
        Map<String, Object> response = (Map<String, Object>) subjectClassGroupManagementService.getGroupsHaveStudent(studentId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) subjectClassGroupManagementService.searchGroupsHaveStudent(studentId, keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<SubjectClassGroupDTO> groups = getGroups(response).stream().map(item -> subjectClassGroupMapper.entityToDTO(item)).collect(Collectors.toList());
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = "/student/group";
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        model.addAttribute("groups", groups);
        return "/student/group";
    }

    private boolean isExpired(HttpSession session) {
        return session.getAttribute("user") == null;
    }

    private List<SubjectClassGroup> getGroups(Map<String, Object> response) {
        return (List<SubjectClassGroup>) response.getOrDefault("data", null);
    }
}
