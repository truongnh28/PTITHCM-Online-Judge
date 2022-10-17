package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.mapper.SubjectClassGroupMapper;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.SubjectClassGroupManagementService;
import ptithcm.onlinejudge.services.SubjectClassManagementService;

import java.util.List;

@Controller
@RequestMapping("/admin/subject/{subjectId}/class/{classId}/group")
public class AdminSubjectClassGroupController {
    @Autowired
    private SubjectClassManagementService subjectClassManagementService;
    @Autowired
    private SubjectClassGroupMapper subjectClassGroupMapper;
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;

    @GetMapping("")
    public String showGroupPage(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, Model model) {
        model.addAttribute("pageTitle", "Nhóm thực hành");
        List<SubjectClassGroupDTO> groups = ((List<SubjectClassGroup>) subjectClassGroupManagementService.getAllGroupsOfClass(classId).getData())
                .stream().map(item -> subjectClassGroupMapper.entityToDTO(item)).toList();
        SubjectClassGroupDTO groupAdd = new SubjectClassGroupDTO();
        groupAdd.setGroupId(classId + "-");
        model.addAttribute("groupAdd", groupAdd);
        SubjectClass subjectClass = (SubjectClass) subjectClassManagementService.getClassById(classId).getData();
        model.addAttribute("className", subjectClass.getSubjectClassName());
        model.addAttribute("groups", groups);
        return "/admin/subject/subject-class-group";
    }

    @PostMapping("/add")
    public String addGroup(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @ModelAttribute("groupAdd") SubjectClassGroupDTO group) {
        ResponseObject addGroupResponse = subjectClassGroupManagementService.addGroupToClass(classId, group);
        if (!addGroupResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group";
    }

    @PostMapping("")
    public String searchGroup(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("pageTitle", "Nhóm thực hành");
        List<SubjectClassGroupDTO> groups = ((List<SubjectClassGroup>) subjectClassGroupManagementService.searchGroupByIdOrName(classId, keyword).getData())
                .stream().map(item -> subjectClassGroupMapper.entityToDTO(item)).toList();
        SubjectClassGroupDTO groupAdd = new SubjectClassGroupDTO();
        groupAdd.setGroupId(classId + "-");
        model.addAttribute("groupAdd", groupAdd);
        SubjectClass subjectClass = (SubjectClass) subjectClassManagementService.getClassById(classId).getData();
        model.addAttribute("className", subjectClass.getSubjectClassName());
        model.addAttribute("groups", groups);
        return "/admin/subject/subject-class-group";
    }

    @GetMapping("/{groupId}/edit")
    public String showEditGroupPage(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Chỉnh sửa nhóm thực hành");
        ResponseObject getGroupByIdResponse = subjectClassGroupManagementService.getGroupById(groupId);
        if (!getGroupByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        SubjectClassGroupDTO group = subjectClassGroupMapper.entityToDTO((SubjectClassGroup) getGroupByIdResponse.getData());
        model.addAttribute("group", group);
        return "/admin/subject/subject-class-group-edit";
    }

    @PostMapping("/{groupId}/edit")
    public String editGroup(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @ModelAttribute("group") SubjectClassGroupDTO group) {
        ResponseObject editGroupResponse = subjectClassGroupManagementService.editGroup(groupId, group);
        if (!editGroupResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group";
    }

    @GetMapping("/{groupId}/lock")
    public String lockGroup(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("groupId") String groupId) {
        ResponseObject lockGroupResponse = subjectClassGroupManagementService.lockGroup(groupId);
        if (!lockGroupResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group";
    }

    @GetMapping("/{groupId}/unlock")
    public String unlockGroup(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("groupId") String groupId) {
        ResponseObject lockGroupResponse = subjectClassGroupManagementService.unlockGroup(groupId);
        if (!lockGroupResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group";
    }
}
