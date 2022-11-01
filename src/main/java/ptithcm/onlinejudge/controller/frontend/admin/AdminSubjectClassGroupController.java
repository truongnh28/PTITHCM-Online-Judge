package ptithcm.onlinejudge.controller.frontend.admin;

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
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.SubjectClassGroupManagementService;
import ptithcm.onlinejudge.services.SubjectClassManagementService;
import ptithcm.onlinejudge.services.SubjectManagementService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/subject/{subjectId}/class/{classId}/group")
public class AdminSubjectClassGroupController {
    @Autowired
    private SubjectManagementService subjectManagementService;
    @Autowired
    private SubjectClassManagementService subjectClassManagementService;
    @Autowired
    private SubjectClassGroupMapper subjectClassGroupMapper;
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;

    @GetMapping("")
    public String showGroupPage(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId) {
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group/page/1";
    }

    @GetMapping("/page/{page}")
    public String showGroupPage(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("page") int page, @Param("keyword") String keyword, Model model) {
        if (!checkValid(subjectId, classId))
            return "redirect:/error";
        Map<String, Object> res = (Map<String, Object>) subjectClassGroupManagementService.getAllGroupsOfClass(classId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            res = (Map<String, Object>) subjectClassGroupManagementService.searchGroupOfClassByKeyword(classId, keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<SubjectClassGroupDTO> groups = getGroups(res).stream().map(item -> subjectClassGroupMapper.entityToDTO(item)).collect(Collectors.toList());
        int currentPage = (int) res.getOrDefault("currentPage", 0);
        int totalPages = (int) res.getOrDefault("totalPages", 0);
        String pageUrlPrefix = String.format("/admin/subject/%s/class/%s/group", subjectId, classId);
        model.addAttribute("pageTitle", "Nhóm thực hành");
        SubjectClassGroupDTO groupAdd = new SubjectClassGroupDTO();
        groupAdd.setGroupId(classId + "-");
        model.addAttribute("groupAdd", groupAdd);
        SubjectClass subjectClass = (SubjectClass) subjectClassManagementService.getClassById(classId).getData();
        model.addAttribute("className", subjectClass.getSubjectClassName());
        model.addAttribute("groups", groups);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        return "/admin/subject/subject-class-group";
    }

    @PostMapping("/add")
    public String addGroup(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @ModelAttribute("groupAdd") SubjectClassGroupDTO group) {
        if (!checkValid(subjectId, classId))
            return "redirect:/error";
        ResponseObject addGroupResponse = subjectClassGroupManagementService.addGroupToClass(classId, group);
        if (!addGroupResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group";
    }

    @PostMapping("/edit")
    public String editGroup(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, SubjectClassGroupDTO group) {
        if (!checkValid(subjectId, classId))
            return "redirect:/error";
        ResponseObject editGroupResponse = subjectClassGroupManagementService.editGroup(group);
        if (!editGroupResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group";
    }

    @GetMapping("/{groupId}/lock")
    public String lockGroup(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("groupId") String groupId) {
        if (!checkValid(subjectId, classId))
            return "redirect:/error";
        ResponseObject lockGroupResponse = subjectClassGroupManagementService.lockGroup(groupId);
        if (!lockGroupResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group";
    }

    @GetMapping("/{groupId}/unlock")
    public String unlockGroup(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("groupId") String groupId) {
        if (!checkValid(subjectId, classId))
            return "redirect:/error";
        ResponseObject lockGroupResponse = subjectClassGroupManagementService.unlockGroup(groupId);
        if (!lockGroupResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/subject/{subjectId}/class/{classId}/group";
    }

    private boolean checkValid(String subjectId, String classId) {
        ResponseObject getSubjectResponse = subjectManagementService.getSubjectById(subjectId);
        ResponseObject getClassResponse = subjectClassManagementService.getClassById(classId);
        return getSubjectResponse.getStatus().equals(HttpStatus.OK) && getClassResponse.getStatus().equals(HttpStatus.OK);
    }

    private List<SubjectClassGroup> getGroups(Map<String, Object> res) {
        return (List<SubjectClassGroup>) res.getOrDefault("data", null);
    }
}
