package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/teacher/class")
public class TeacherClassController {
    @Autowired
    private SubjectClassMapper subjectClassMapper;

    @Autowired
    private SubjectClassManagementService subjectClassManagementService;

    @GetMapping("")
    public String showClassesPage(Model model, HttpSession session) {
        String teacherId = session.getAttribute("user").toString();
        model.addAttribute("pageTitle", "Lớp giáo viên quản lý");
        List<SubjectClassDTO> classes = ((List<SubjectClass>) subjectClassManagementService.getClassesTeacherOwnActive(teacherId).getData())
                .stream().map(item -> subjectClassMapper.entityToDTO(item)).toList();
        model.addAttribute("classes", classes);
        return "/teacher/contest/class-teacher-own";
    }

    @PostMapping("")
    public String searchClassesThatTeacherOwn(Model model, HttpSession session, @RequestParam("keyword") String keyword) {
        String teacherId = session.getAttribute("user").toString();
        model.addAttribute("pageTitle", "Lớp giáo viên quản lý");
        List<SubjectClassDTO> classes = ((List<SubjectClass>) subjectClassManagementService.searchClassesTeacherOwnActive(teacherId, keyword).getData())
                .stream().map(item -> subjectClassMapper.entityToDTO(item)).toList();
        model.addAttribute("classes", classes);
        return "/teacher/contest/class-teacher-own";
    }
}
