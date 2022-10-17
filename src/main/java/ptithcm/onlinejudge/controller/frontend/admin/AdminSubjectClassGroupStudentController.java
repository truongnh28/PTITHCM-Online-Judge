package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.mapper.StudentMapper;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.StudentManagementService;
import ptithcm.onlinejudge.services.StudentOfGroupManagement;
import ptithcm.onlinejudge.services.SubjectClassGroupManagementService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/subject/{subjectId}/class/{classId}/group/{groupId}/student")
public class AdminSubjectClassGroupStudentController {
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;

    @Autowired
    private StudentManagementService studentManagementService;

    @Autowired
    private StudentOfGroupManagement studentOfGroupManagement;

    @GetMapping("")
    public String showStudentManagementPage(@PathVariable("subjectId") String subjectId,
                                            @PathVariable("classId") String classId,
                                            @PathVariable("groupId") String groupId,
                                            Model model) {
        model.addAttribute("pageTitle", "Sinh viên nhóm");
        List<StudentDTO> students = ((List<Student>) studentManagementService.getStudentsOfGroup(groupId).getData())
                .stream().map(item -> studentMapper.entityToDTO(item)).toList();
        SubjectClassGroup group = (SubjectClassGroup) subjectClassGroupManagementService.getGroupById(groupId).getData();
        model.addAttribute("groupName", group.getSubjectClassGroupName());
        model.addAttribute("students", students);
        return "/admin/subject/student-of-group";
    }

    @PostMapping("")
    public String searchStudent(@PathVariable("subjectId") String subjectId,
                                @PathVariable("classId") String classId,
                                @PathVariable("groupId") String groupId,
                                @RequestParam("keyword") String keyword,
                                Model model) {
        model.addAttribute("pageTitle", "Sinh viên nhóm");
        List<StudentDTO> students = ((List<Student>) studentManagementService.searchStudentsOfGroupById(groupId, keyword).getData())
                .stream().map(item -> studentMapper.entityToDTO(item)).toList();
        SubjectClassGroup group = (SubjectClassGroup) subjectClassGroupManagementService.getGroupById(groupId).getData();
        model.addAttribute("groupName", group.getSubjectClassGroupName());
        model.addAttribute("students", students);
        return "/admin/subject/student-of-group";
    }

    @GetMapping("/{studentId}/remove")
    public String removeStudentFromGroup(@PathVariable("subjectId") String subjectId,
                                         @PathVariable("classId") String classId,
                                         @PathVariable("groupId") String groupId,
                                         @PathVariable("studentId") String studentId) {
        ResponseObject removeStudentFromGroupResponse = studentOfGroupManagement.deleteStudentFromGroup(studentId, groupId);
        if (!removeStudentFromGroupResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group/{groupId}/student";
    }

    @GetMapping("/add")
    public String showAddStudentToGroupPage(@PathVariable("subjectId") String subjectId,
                                            @PathVariable("classId") String classId,
                                            @PathVariable("groupId") String groupId,
                                            Model model) {
        model.addAttribute("pageTitle", "Thêm sinh viên vào nhóm");
        List<StudentDTO> students = ((List<Student>) studentManagementService.getStudentsNotInClass(classId).getData())
                .stream().map(item -> studentMapper.entityToDTO(item)).collect(Collectors.toList());
        model.addAttribute("students", students);
        return "admin/subject/student-of-group-add-student";
    }

    @PostMapping("/add")
    public String searchStudentsNotInClassById(@PathVariable("subjectId") String subjectId,
                                               @PathVariable("classId") String classId,
                                               @PathVariable("groupId") String groupId,
                                               @RequestParam("keyword") String keyword,
                                               Model model) {
        model.addAttribute("pageTitle", "Thêm sinh viên vào nhóm");
        List<StudentDTO> students = ((List<Student>) studentManagementService.searchStudentsNotInClassById(classId, keyword).getData())
                .stream().map(item -> studentMapper.entityToDTO(item)).collect(Collectors.toList());
        model.addAttribute("students", students);
        return "/admin/subject/student-of-group-add-student";
    }

    @GetMapping("/{studentId}/add")
    public String showAddStudentToGroupPage(@PathVariable("subjectId") String subjectId,
                                            @PathVariable("classId") String classId,
                                            @PathVariable("groupId") String groupId,
                                            @PathVariable("studentId") String studentId) {
        ResponseObject addStudentToGroupResponse = studentOfGroupManagement.addStudentToGroup(studentId, groupId);
        if (!addStudentToGroupResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group/{groupId}/student/add";
    }
}
