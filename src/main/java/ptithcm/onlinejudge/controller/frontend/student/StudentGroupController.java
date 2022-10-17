package ptithcm.onlinejudge.controller.frontend.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.mapper.SubjectClassGroupMapper;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.SubjectClassGroupManagementService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/student/group")
public class StudentGroupController {
    @Autowired
    private SubjectClassGroupMapper subjectClassGroupMapper;
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;

    @GetMapping("")
    public String showGroupsHaveStudent(Model model, HttpSession session) {
        String studentId = session.getAttribute("user").toString();
        model.addAttribute("pageTitle", "Nhóm thực hành");
        ResponseObject getGroupsHaveStudentResponse = subjectClassGroupManagementService.getGroupsHaveStudent(studentId);
        if (!getGroupsHaveStudentResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        List<SubjectClassGroupDTO> groups = ((List<SubjectClassGroup>) getGroupsHaveStudentResponse.getData()).stream().map(item -> subjectClassGroupMapper.entityToDTO(item)).toList();
        model.addAttribute("groups", groups);
        return "/student/group";
    }

    @PostMapping("")
    public String searchGroupsHaveStudent(Model model, HttpSession session, @RequestParam("keyword") String keyword) {
        String studentId = session.getAttribute("user").toString();
        model.addAttribute("pageTitle", "Nhóm thực hành");
        ResponseObject searchGroupsHaveStudent = subjectClassGroupManagementService.searchGroupsHaveStudent(studentId, keyword);
        if (!searchGroupsHaveStudent.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        List<SubjectClassGroupDTO> groups = ((List<SubjectClassGroup>) searchGroupsHaveStudent.getData()).stream().map(item -> subjectClassGroupMapper.entityToDTO(item)).toList();
        model.addAttribute("groups", groups);
        return "/student/group";
    }
}
