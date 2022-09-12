package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teacher/subject")
public class TeacherSubjectController {
    @GetMapping("")
    public String showSubjectPage(Model model) {
        model.addAttribute("pageTitle", "Môn học");
        model.addAttribute("subjects", Data.subjectList);
        return "/teacher/subject";
    }

    @GetMapping("/{subjectId}")
    public String showSubjectClassPage(@PathVariable("subjectId") String subjectId, Model model) {
        model.addAttribute("pageTitle", "Lớp môn học");
        List<SubjectClassDTO> subjectClassList = new ArrayList<>();
        for (SubjectClassDTO subjectClass : Data.subjectClassList) {
            if (subjectClass.getSubjectId().equals(subjectId)) {
                subjectClassList.add(subjectClass);
            }
        }
        model.addAttribute("subjectClasses", subjectClassList);
        return "/teacher/subject-class";
    }

    @GetMapping("/{subjectId}/class/{classId}")
    public String showSubjectClassGroupPage(@PathVariable("subjectId") String subjectId,
                                            @PathVariable("classId") String subjectClassId,
                                            Model model) {
        model.addAttribute("pageTitle", "Nhóm thực hành");
        List<SubjectClassGroupDTO> subjectClassGroupList = new ArrayList<>();
        for (SubjectClassGroupDTO subjectClassGroup : Data.subjectClassGroupList) {
            if (subjectClassGroup.getSubjectClassId().equals(subjectClassId)) {
                subjectClassGroupList.add(subjectClassGroup);
            }
        }
        model.addAttribute("subjectClassGroups", subjectClassGroupList);
        return "/teacher/subject-class-group";
    }

    @GetMapping("/{subjectId}/class/{classId}/group/{groupId}")
    public String showStudentOfGroupPage(@PathVariable("subjectId") String subjectId,
                                         @PathVariable("classId") String classId,
                                         @PathVariable("groupId") String groupId,
                                         Model model) {
        model.addAttribute("pageTitle", "Sinh viên trong nhóm thực hành");
        List<String> studentIdList = new ArrayList<>();
        for (StudentOfGroupDTO studentOfGroup: Data.studentOfGroupList)
            if (studentOfGroup.getSubjectClassGroupId().equals(groupId))
                studentIdList.add(studentOfGroup.getStudentId());
        List<StudentDTO> studentListInGroup = new ArrayList<>();
        for (String studentId: studentIdList) {
            StudentDTO student = new StudentDTO();
            for (StudentDTO studentDTO: Data.studentList) {
                if (studentDTO.getStudentId().equals(studentId)) {
                    student = studentDTO;
                    break;
                }
            }
            studentListInGroup.add(student);
        }
        model.addAttribute("students", studentListInGroup);
        return "/teacher/student-of-group";
    }

    @GetMapping("/{subjectId}/add")
    public String showAddSubjectClassForm(@PathVariable("subjectId") String subjectId, Model model) {
        model.addAttribute("pageTitle", "Thêm lớp môn học");
        SubjectClassDTO subjectClass = new SubjectClassDTO();
        model.addAttribute("subjectClass", subjectClass);
        return "/teacher/subject-class-add";
    }

    @PostMapping("/{subjectId}/add")
    public String addSubjectClass(@PathVariable("subjectId") String subjectId, @ModelAttribute("subjectClass") SubjectClassDTO subjectClass) {
        subjectClass.setSubjectId(subjectId);
        boolean isExisted = false;
        for (SubjectClassDTO subjectClassDTO: Data.subjectClassList)
            if (subjectClassDTO.getSubjectClassId().equals(subjectClass.getSubjectClassId())) {
                isExisted = true;
                break;
            }
        if (isExisted)
            return "redirect:/error";
        Data.subjectClassList.add(subjectClass);
        return "redirect:/teacher/subject/{subjectId}";
    }

    private Optional<SubjectClassDTO> findSubjectClassById(String subjectClassId) {
        SubjectClassDTO subjectClass = new SubjectClassDTO();
        boolean isExisted = false;
        for (SubjectClassDTO subjectClassDTO: Data.subjectClassList) {
            if (subjectClassDTO.getSubjectClassId().equals(subjectClassId)) {
                subjectClass = subjectClassDTO;
                isExisted = true;
                break;
            }
        }
        return isExisted ? Optional.of(subjectClass) : Optional.empty();
    }

    @GetMapping("/{subjectId}/class/{classId}/edit")
    public String showEditClassPage(@PathVariable("subjectId") String subjectId,
                                    @PathVariable("classId") String classId,
                                    Model model) {
        model.addAttribute("pageTitle", "Chỉnh sửa lớp");
        Optional<SubjectClassDTO> foundSubjectClass = findSubjectClassById(subjectId);
        if (foundSubjectClass.isEmpty())
            return "redirect:/error";
        model.addAttribute("subjectClass", foundSubjectClass.get());
        return "/teacher/subject-class-edit";
    }

    @PostMapping("/{subjectId}/class/{classId}/edit")
    public String editClass(@PathVariable("subjectId") String subjectId,
                            @PathVariable("classId") String classId,
                            @ModelAttribute("subjectClass") SubjectClassDTO subjectClass) {
        Optional<SubjectClassDTO> foundSubjectClass = findSubjectClassById(subjectId);
        if (foundSubjectClass.isEmpty())
            return "redirect:/error";
        SubjectClassDTO newSubjectClass = foundSubjectClass.get();
        newSubjectClass.setSubjectClassName(subjectClass.getSubjectClassName());
        return "redirect:/teacher/subject/{subjectId}";
    }

    @GetMapping("/{subjectId}/class/{classId}/add")
    public String showAddSubjectClassGroupForm(@PathVariable("subjectId") String subjectId,
                                               @PathVariable("classId") String classId,
                                               Model model) {
        model.addAttribute("pageTitle", "Thêm nhóm thực hành");
        SubjectClassGroupDTO subjectClassGroup = new SubjectClassGroupDTO();
        model.addAttribute("subjectClassGroup", subjectClassGroup);
        return "/teacher/subject-class-group-add";
    }

    @PostMapping("/{subjectId}/class/{classId}/add")
    public String addGroup(@PathVariable("subjectId") String subjectId,
                           @PathVariable("classId") String classId,
                           @ModelAttribute("subjectClassGroup") SubjectClassGroupDTO subjectClassGroup) {
        subjectClassGroup.setSubjectClassId(classId);
        boolean isExisted = false;
        for (SubjectClassGroupDTO subjectClassGroupDTO: Data.subjectClassGroupList)
            if (subjectClassGroupDTO.getSubjectClassGroupId().equals(subjectClassGroup.getSubjectClassGroupId())) {
                isExisted = true;
                break;
            }
        if (isExisted)
            return "redirect:/error";
        Data.subjectClassGroupList.add(subjectClassGroup);
        return "redirect:/teacher/subject/{subjectId}/class/{classId}/add";
    }

    @GetMapping("/{subjectId}/class/{classId}/group/{groupId}/edit")
    public String showAddStudentToGroupPage(@PathVariable("subjectId") String subjectId,
                                            @PathVariable("classId") String classId,
                                            @PathVariable("groupId") String groupId,
                                            Model model) {
        model.addAttribute("pageTitle", "Thêm sinh viên vào nhóm thực hành");
        List<StudentShowDTO> studentList = new ArrayList<>();
        for (StudentDTO student: Data.studentList)
            if (student.isActive()) {
                StudentShowDTO studentShow = new StudentShowDTO();
                studentShow.setStudentId(student.getStudentId());
                studentShow.setStudentPassword(student.getStudentPassword());
                studentShow.setRole(student.getRole());
                studentShow.setStudentFirstName(student.getStudentFirstName());
                studentShow.setStudentLastName(student.getStudentLastName());
                studentShow.setActive(student.isActive());
                studentShow.setDisabledButtonAdding(Data.studentOfGroupList.contains(new StudentOfGroupDTO(student.getStudentId(), groupId)));
                studentList.add(studentShow);
            }
        model.addAttribute("students", studentList);
        return "teacher/student-of-group-edit";
    }

    @GetMapping("/{subjectId}/class/{classId}/group/{groupId}/student/{studentId}/add")
    public String addStudentToGroup(@PathVariable("subjectId") String subjectId,
                                    @PathVariable("classId") String classId,
                                    @PathVariable("groupId") String groupId,
                                    @PathVariable("studentId") String studentId) {
        Data.studentOfGroupList.add(new StudentOfGroupDTO(studentId, groupId));
        return "redirect:/teacher/subject/{subjectId}/class/{classId}/group/{groupId}/edit";
    }

    @GetMapping("/{subjectId}/class/{classId}/group/{groupId}/student/{studentId}/delete")
    public String deleteStudentFromGroup(@PathVariable("subjectId") String subjectId,
                                         @PathVariable("classId") String classId,
                                         @PathVariable("groupId") String groupId,
                                         @PathVariable("studentId") String studentId) {
        boolean isExisted = false;
        for (StudentOfGroupDTO student: Data.studentOfGroupList) {
            if (student.getStudentId().equals(studentId) && student.getSubjectClassGroupId().equals(groupId)) {
                isExisted = true;
                break;
            }
        }
        if (!isExisted) {
            return "redirect:/error";
        }
        Data.studentOfGroupList.remove(new StudentOfGroupDTO(studentId, groupId));
        return "redirect:/teacher/subject/{subjectId}/class/{classId}/group/{groupId}/edit";
    }
}
