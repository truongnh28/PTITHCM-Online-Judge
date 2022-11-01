package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.*;
import ptithcm.onlinejudge.dto.LoginDTO;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.mapper.SubjectClassGroupMapper;
import ptithcm.onlinejudge.mapper.SubjectClassMapper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.model.request.ContestHasProblemRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/class")
public class TeacherClassController {
    @Autowired
    private SubjectClassMapper subjectClassMapper;

    @Autowired
    private SubjectClassManagementService subjectClassManagementService;

    @GetMapping("")
    public String showClasses() {
        return "redirect:/teacher/class/page/1";
    }

    @GetMapping("/page/{page}")
    public String showClassesPage(Model model, @PathVariable("page") int page, @Param("keyword") String keyword, HttpSession session) {
        String teacherId = session.getAttribute("user").toString();
        model.addAttribute("pageTitle", "Lớp giáo viên quản lý");
        Map<String, Object> response = (Map<String, Object>) subjectClassManagementService.getClassesTeacherOwnActive(teacherId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) subjectClassManagementService.searchClassesTeacherOwnActive(teacherId, keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<SubjectClassDTO> classes = getClasses(response).stream().map(item -> subjectClassMapper.entityToDTO(item)).collect(Collectors.toList());
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = "/teacher/class";
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        model.addAttribute("classes", classes);
        return "/teacher/contest/class-teacher-own";
    }

    private List<SubjectClass> getClasses(Map<String, Object> response) {
        return (List<SubjectClass>) response.getOrDefault("data", null);
    }
}
