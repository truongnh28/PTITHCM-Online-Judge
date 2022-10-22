package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.mapper.StudentMapper;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/subject/{subjectId}/class/{classId}/group/{groupId}/student")
public class AdminSubjectClassGroupStudentController {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private SubjectManagementService subjectManagementService;
    @Autowired
    private SubjectClassManagementService subjectClassManagementService;
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;
    @Autowired
    private StudentManagementService studentManagementService;
    @Autowired
    private StudentOfGroupManagement studentOfGroupManagement;

    @GetMapping("")
    public String showStudentOfGroup(@PathVariable("subjectId") String subjectId,
                                     @PathVariable("classId") String classId,
                                     @PathVariable("groupId") String groupId) {
        if (!checkValid(subjectId, classId, groupId))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group/{groupId}/student/page/1";
    }

    @GetMapping("/page/{page}")
    public String showStudentManagementPage(@PathVariable("subjectId") String subjectId,
                                            @PathVariable("classId") String classId,
                                            @PathVariable("groupId") String groupId,
                                            @PathVariable("page") int page,
                                            @Param("keyword") String keyword,
                                            Model model) {
        if (!checkValid(subjectId, classId, groupId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Sinh viên của nhóm");
        Map<String, Object> response = (Map<String, Object>) studentManagementService.getStudentsOfGroup(groupId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) studentManagementService.searchStudentsOfGroupByKeyword(groupId, keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<StudentDTO> students = getStudents(response).stream().map(item -> studentMapper.entityToDTO(item)).collect(Collectors.toList());
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = String.format("/admin/subject/%s/class/%s/group/%s/student", subjectId, classId, groupId);
        SubjectClassGroup group = (SubjectClassGroup) subjectClassGroupManagementService.getGroupById(groupId).getData();
        model.addAttribute("groupName", group.getSubjectClassGroupName());
        model.addAttribute("students", students);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        return "/admin/subject/student-of-group";
    }

    @GetMapping("/{studentId}/remove")
    public String removeStudentFromGroup(@PathVariable("subjectId") String subjectId,
                                         @PathVariable("classId") String classId,
                                         @PathVariable("groupId") String groupId,
                                         @PathVariable("studentId") String studentId) {
        if (!checkValid(subjectId, classId, groupId))
            return "redirect:/error";
        ResponseObject removeStudentFromGroupResponse = studentOfGroupManagement.deleteStudentFromGroup(studentId, groupId);
        if (!removeStudentFromGroupResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group/{groupId}/student";
    }

    @GetMapping("/add")
    public String showAddStudentToGroup(@PathVariable("subjectId") String subjectId,
                                        @PathVariable("classId") String classId,
                                        @PathVariable("groupId") String groupId) {
        if (!checkValid(subjectId, classId, groupId))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group/{groupId}/student/add/page/1";
    }

    @GetMapping("/add/page/{page}")
    public String showAddStudentToGroupPage(@PathVariable("subjectId") String subjectId,
                                            @PathVariable("classId") String classId,
                                            @PathVariable("groupId") String groupId,
                                            @PathVariable("page") int page,
                                            @Param("keyword") String keyword,
                                            Model model) {
        if (!checkValid(subjectId, classId, groupId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Thêm sinh viên vào nhóm");
        Map<String, Object> response = (Map<String, Object>) studentManagementService.getStudentsNotInClass(classId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) studentManagementService.searchStudentsNotInClassByKeyword(classId, keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<StudentDTO> students = getStudents(response).stream().map(item -> studentMapper.entityToDTO(item)).collect(Collectors.toList());
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = String.format("/admin/subject/%s/class/%s/group/%s/student/add", subjectId, classId, groupId);
        model.addAttribute("students", students);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        return "admin/subject/student-of-group-add-student";
    }

    @GetMapping("/{studentId}/add")
    public String showAddStudentToGroupPage(@PathVariable("subjectId") String subjectId,
                                            @PathVariable("classId") String classId,
                                            @PathVariable("groupId") String groupId,
                                            @PathVariable("studentId") String studentId) {
        if (!checkValid(subjectId, classId, groupId))
            return "redirect:/error";
        ResponseObject addStudentToGroupResponse = studentOfGroupManagement.addStudentToGroup(studentId, groupId);
        if (!addStudentToGroupResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group/{groupId}/student/add";
    }

    private boolean checkValid(String subjectId, String classId, String groupId) {
        ResponseObject getSubjectRes = subjectManagementService.getSubjectById(subjectId);
        ResponseObject getClassRes = subjectClassManagementService.getClassById(classId);
        ResponseObject getGroupRes = subjectClassGroupManagementService.getGroupById(groupId);
        return getSubjectRes.getStatus().equals(HttpStatus.OK) && getClassRes.getStatus().equals(HttpStatus.OK) && getGroupRes.getStatus().equals(HttpStatus.OK);
    }

    private List<Student> getStudents(Map<String, Object> response) {
        return (List<Student>) response.getOrDefault("data", null);
    }
}
