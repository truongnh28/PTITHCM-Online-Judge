package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.dto.StudentShowDTO;
import ptithcm.onlinejudge.dto.SubjectClassDTO;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.dto.SubjectDTO;
import ptithcm.onlinejudge.mapper.StudentMapper;
import ptithcm.onlinejudge.mapper.SubjectClassGroupMapper;
import ptithcm.onlinejudge.mapper.SubjectClassMapper;
import ptithcm.onlinejudge.mapper.SubjectMapper;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.entity.StudentOfGroup;
import ptithcm.onlinejudge.model.entity.Subject;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.model.request.StudentOfGroupRequest;
import ptithcm.onlinejudge.model.request.SubjectClassGroupRequest;
import ptithcm.onlinejudge.model.request.SubjectClassRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.StudentManagementService;
import ptithcm.onlinejudge.services.StudentOfGroupManagement;
import ptithcm.onlinejudge.services.SubjectClassGroupManagement;
import ptithcm.onlinejudge.services.SubjectClassManagement;
import ptithcm.onlinejudge.services.SubjectManagement;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/subject")
public class TeacherSubjectController {
    @Autowired
    private SubjectManagement subjectManagement;

    @Autowired
    private SubjectClassManagement subjectClassManagement;

    @Autowired
    private SubjectClassGroupManagement subjectClassGroupManagement;

    @Autowired
    private StudentOfGroupManagement studentOfGroupManagement;

    @Autowired
    private StudentManagementService studentManagementService;
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private SubjectClassMapper subjectClassMapper;
    @Autowired
    private SubjectClassGroupMapper subjectClassGroupMapper;

    @Autowired
    private StudentMapper studentMapper;

    // SUBJECT
    @GetMapping("")
    public String showSubjectPage(Model model) {
        model.addAttribute("pageTitle", "Môn học");
        List<SubjectDTO> subjects = ((List<Subject>) subjectManagement.getAllSubject().getData())
                .stream().map(item -> subjectMapper.entityToDTO(item)).toList();
        model.addAttribute("subjects", subjects);
        return "/teacher/subject/subject";
    }


    // SUBJECT CLASS
    @GetMapping("/{subjectId}")
    public String showSubjectClassPage(@PathVariable("subjectId") String subjectId, Model model) {
        model.addAttribute("pageTitle", "Lớp môn học");
        List<SubjectClassDTO> subjectClasses = ((List<SubjectClass>) subjectClassManagement.getAllSubjectClassBySubjectId(subjectId).getData())
                .stream().map(item -> subjectClassMapper.entityToDTO(item)).collect(Collectors.toList());
        model.addAttribute("subjectClasses", subjectClasses);
        return "/teacher/subject/subject-class";
    }

    // SUBJECT CLASS ADD
    @GetMapping("/{subjectId}/add")
    public String showAddSubjectClassPage(@PathVariable("subjectId") String subjectId, Model model) {
        model.addAttribute("pageTitle", "Thêm lớp môn học");
        SubjectClassDTO subjectClass = new SubjectClassDTO();
        model.addAttribute("subjectClass", subjectClass);
        return "/teacher/subject/subject-class-add";
    }

    @PostMapping("/{subjectId}/add")
    public String addSubjectClass(@PathVariable("subjectId") String subjectId, @ModelAttribute("subjectClass") SubjectClassDTO subjectClass) {
        ResponseObject responseObject = subjectClassManagement.addSubjectClass(new SubjectClassRequest(subjectClass.getSubjectClassId(), subjectClass.getSubjectClassName(), subjectId));
        if (!responseObject.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/subject/{subjectId}";
    }

    // SUBJECT CLASS EDIT
    @GetMapping("/{subjectId}/class/{classId}/edit")
    public String showEditSubjectClassPage(@PathVariable("subjectId") String subjectId,
                                    @PathVariable("classId") String classId,
                                    Model model) {
        model.addAttribute("pageTitle", "Chỉnh sửa lớp");
        ResponseObject responseObject = subjectClassManagement.findSubjectClassBySubjectClassId(classId);
        if (!responseObject.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        SubjectClass entity = (SubjectClass) responseObject.getData();
        model.addAttribute("subjectClass", subjectClassMapper.entityToDTO(entity));
        return "/teacher/subject/subject-class-edit";
    }

    @PostMapping("/{subjectId}/class/{classId}/edit")
    public String editSubjectClass(@PathVariable("subjectId") String subjectId,
                            @PathVariable("classId") String classId,
                            @ModelAttribute("subjectClass") SubjectClassDTO subjectClass) {
        ResponseObject responseObject = subjectClassManagement.editSubjectClass(new SubjectClassRequest(classId, subjectClass.getSubjectClassName(), subjectId));
        if (!responseObject.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/subject/{subjectId}";
    }

    // SUBJECT CLASS GROUP
    @GetMapping("/{subjectId}/class/{classId}")
    public String showSubjectClassGroupPage(@PathVariable("subjectId") String subjectId,
                                            @PathVariable("classId") String subjectClassId,
                                            Model model) {
        model.addAttribute("pageTitle", "Nhóm thực hành");
        List<SubjectClassGroupDTO> subjectClassGroups = ((List<SubjectClassGroup>) subjectClassGroupManagement.getAllSubjectClassGroupOfSubjectClass(subjectClassId).getData())
                .stream().map(item -> subjectClassGroupMapper.entityToDTO(item)).collect(Collectors.toList());
        model.addAttribute("subjectClassGroups", subjectClassGroups);
        return "/teacher/subject/subject-class-group";
    }

    // SUBJECT CLASS GROUP ADD
    @GetMapping("/{subjectId}/class/{classId}/add")
    public String showAddSubjectClassGroupForm(@PathVariable("subjectId") String subjectId,
                                               @PathVariable("classId") String classId,
                                               Model model) {
        model.addAttribute("pageTitle", "Thêm nhóm thực hành");
        SubjectClassGroupDTO subjectClassGroup = new SubjectClassGroupDTO();
        model.addAttribute("subjectClassGroup", subjectClassGroup);
        return "/teacher/subject/subject-class-group-add";
    }

    @PostMapping("/{subjectId}/class/{classId}/add")
    public String addGroup(@PathVariable("subjectId") String subjectId,
                           @PathVariable("classId") String classId,
                           @ModelAttribute("subjectClassGroup") SubjectClassGroupDTO subjectClassGroup) {
        ResponseObject responseObject = subjectClassGroupManagement.addSubjectClassGroup(new SubjectClassGroupRequest(subjectClassGroup.getSubjectClassGroupId(), subjectClassGroup.getSubjectClassGroupName(), classId));
        if (!responseObject.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/subject/{subjectId}/class/{classId}";
    }

    // SUBJECT CLASS GROUP EDIT
    @GetMapping("/{subjectId}/class/{classId}/group/{groupId}/edit")
    public String showEditGroupPage(@PathVariable("subjectId") String subjectId,
                                    @PathVariable("classId") String classId,
                                    @PathVariable("groupId") String groupId,
                                    Model model) {
        model.addAttribute("pageTitle", "Chỉnh sửa nhóm thực hành");
        ResponseObject responseObject = subjectClassGroupManagement.getSubjectClassGroupById(groupId);
        if (!responseObject.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        SubjectClassGroupDTO subjectClassGroup = subjectClassGroupMapper.entityToDTO((SubjectClassGroup) responseObject.getData());
        model.addAttribute("group", subjectClassGroup);
        return "/teacher/subject/subject-class-group-edit";
    }

    @PostMapping("/{subjectId}/class/{classId}/group/{groupId}/edit")
    public String editGroup(@PathVariable("subjectId") String subjectId,
                            @PathVariable("classId") String classId,
                            @PathVariable("groupId") String groupId,
                            @ModelAttribute("group") SubjectClassGroupDTO group) {
        ResponseObject responseObject = subjectClassGroupManagement.editSubjectClassGroup(new SubjectClassGroupRequest(groupId, group.getSubjectClassGroupName(), classId));
        if (!responseObject.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/subject/{subjectId}/class/{classId}";
    }

    // STUDENT OF GROUP

    @GetMapping("/{subjectId}/class/{classId}/group/{groupId}/student")
    public String showStudentOfGroupPage(@PathVariable("subjectId") String subjectId,
                                         @PathVariable("classId") String classId,
                                         @PathVariable("groupId") String groupId,
                                         Model model) {
        model.addAttribute("pageTitle", "Sinh viên trong nhóm thực hành");
        List<StudentDTO> students = ((List<StudentOfGroup>) studentOfGroupManagement.getStudentOfGroupByGroupId(groupId).getData()).stream().map(item -> studentMapper.entityToDTO(item.getStudent())).collect(Collectors.toList());
        model.addAttribute("students", students);
        return "/teacher/subject/student-of-group";
    }

    @GetMapping("/{subjectId}/class/{classId}/group/{groupId}/student/edit")
    public String showAddStudentToGroupPage(@PathVariable("subjectId") String subjectId,
                                            @PathVariable("classId") String classId,
                                            @PathVariable("groupId") String groupId,
                                            Model model) {
        model.addAttribute("pageTitle", "Thêm sinh viên vào nhóm thực hành");
        List<Student> activeStudentList = ((List<Student>) studentManagementService.getAllStudent().getData())
                .stream().filter(item -> item.getActive() == (byte) 1).toList();
        List<StudentShowDTO> studentShowList = activeStudentList.stream().map(student -> {
            StudentShowDTO studentShowDTO = studentMapper.entityToShowDTO(student);
            ResponseObject responseCheckStudentInClass = studentOfGroupManagement.checkStudentInSubjectClass(student.getId(), classId);
            if ((boolean) responseCheckStudentInClass.getData()) {
                ResponseObject responseCheckStudentInGroup = studentOfGroupManagement.checkStudentInGroup(student.getId(), groupId);
                if ((boolean) responseCheckStudentInGroup.getData()) {
                    studentShowDTO.setDisabledButtonAdding(true);
                    studentShowDTO.setDisabledButtonRemoving(false);
                    return studentShowDTO;
                }
                studentShowDTO.setDisabledButtonRemoving(true);
                studentShowDTO.setDisabledButtonAdding(true);
                return studentShowDTO;
            }
            studentShowDTO.setDisabledButtonAdding(false);
            studentShowDTO.setDisabledButtonRemoving(true);
            return studentShowDTO;
        }).collect(Collectors.toList());
        model.addAttribute("students", studentShowList);
        return "/teacher/subject/student-of-group-student";
    }

    @GetMapping("/{subjectId}/class/{classId}/group/{groupId}/student/{studentId}/add")
    public String addStudentToGroup(@PathVariable("subjectId") String subjectId,
                                    @PathVariable("classId") String classId,
                                    @PathVariable("groupId") String groupId,
                                    @PathVariable("studentId") String studentId) {
        ResponseObject responseObject = studentOfGroupManagement.addStudentOfGroup(new StudentOfGroupRequest(studentId, groupId));
        if (!responseObject.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/subject/{subjectId}/class/{classId}/group/{groupId}/student/edit";
    }

    @GetMapping("/{subjectId}/class/{classId}/group/{groupId}/student/{studentId}/delete")
    public String deleteStudentFromGroup(@PathVariable("subjectId") String subjectId,
                                         @PathVariable("classId") String classId,
                                         @PathVariable("groupId") String groupId,
                                         @PathVariable("studentId") String studentId) {
        ResponseObject responseObject = studentOfGroupManagement.deleteStudentOfGroup(new StudentOfGroupRequest(studentId, groupId));
        if (!responseObject.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/subject/{subjectId}/class/{classId}/group/{groupId}/student/edit";
    }
}
