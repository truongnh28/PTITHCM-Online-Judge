package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.ContestDTO;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.ContestManagementService;
import ptithcm.onlinejudge.services.SubjectClassGroupManagementService;
import ptithcm.onlinejudge.services.SubjectClassManagementService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/class/{classId}/group/{groupId}/contest")
public class TeacherContestOfGroupController {
    @Autowired
    private SubjectClassManagementService subjectClassManagementService;
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestManagementService contestManagementService;

    @GetMapping("")
    public String showGroupContestTeacherOwn(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest/page/1";
    }

    @GetMapping("/page/{page}")
    public String showContestsOfGroupTeacherOwn(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("page") int page, @Param("keyword") String keyword, Model model, HttpSession session) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        String teacherId = session.getAttribute("user").toString();
        model.addAttribute("pageTitle", "Danh sách bài thực hành");
        SubjectClassGroup group = (SubjectClassGroup) subjectClassGroupManagementService.getGroupById(groupId).getData();
        Map<String, Object> response = (Map<String, Object>) contestManagementService.getAllContestsCreateByTeacher(teacherId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) contestManagementService.searchAllContestsCreateByTeacher(teacherId, keyword.trim(), page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<ContestDTO> contests = getContest(response).stream().map(item -> contestMapper.entityToDTO(item)).collect(Collectors.toList());
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = String.format("/teacher/class/%s/group/%s/contest", classId, groupId);
        model.addAttribute("contestAdd", new ContestDTO());
        model.addAttribute("contests", contests);
        model.addAttribute("groupName", group.getSubjectClassGroupName());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        return "/teacher/contest/contest-of-group";
    }

    @PostMapping("/add")
    public String addContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @ModelAttribute("contest") ContestDTO contest, HttpSession session) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        String teacherId = session.getAttribute("user").toString();
        ResponseObject addContestResponse = contestManagementService.addContest(contest, teacherId, groupId);
        if (!addContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest";
    }

    @GetMapping("/{contestId}/lock")
    public String lockContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        ResponseObject lockContestResponse = contestManagementService.lockContest(contestId);
        if (!lockContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest";
    }

    @GetMapping("/{contestId}/unlock")
    public String unlockContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        ResponseObject lockContestResponse = contestManagementService.unlockContest(contestId);
        if (!lockContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest";
    }

    @GetMapping("/{contestId}/edit")
    public String showEditContestPage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Chỉnh sửa thông tin bài thực hành");
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDTO contest = contestMapper.entityToDTO((Contest) getContestByIdResponse.getData());
        model.addAttribute("contest", contest);
        return "/teacher/contest/contest-of-group-edit";
    }

    @PostMapping("/{contestId}/edit")
    public String editContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @ModelAttribute("contest") ContestDTO contest) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        ResponseObject editContestResponse = contestManagementService.editContest(contest, contestId);
        if (!editContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest";
    }

    @GetMapping("/clone")
    public String showContestsActive(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest/clone/page/1";
    }

    @GetMapping("/clone/page/{page}")
    public String showContestsInSystemPage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("page") int page, @Param("keyword") String keyword, Model model) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Chọn bài thực hành");
        Map<String, Object> response = (Map<String, Object>) contestManagementService.getAllContestsActive(page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) contestManagementService.searchAllContestsActive(keyword, page).getData();
            model.addAttribute("keyword");
        }
        List<ContestDTO> contests = getContest(response).stream().map(item -> contestMapper.entityToDTO(item)).collect(Collectors.toList());
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = String.format("/teacher/class/%s/group/%s/contest/clone", classId, groupId);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        model.addAttribute("contests", contests);
        return "/teacher/contest/contest-of-group-clone";
    }

    @GetMapping("/{contestId}/clone")
    public String showClonePage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        if (!contestManagementService.getContestById(contestId).getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Clone bài thực hành");
        model.addAttribute("contest", new ContestDTO());
        return "/teacher/contest/contest-of-group-clone-save";
    }

    @PostMapping("/{contestId}/clone")
    public String cloneContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @ModelAttribute("contest") ContestDTO contest, HttpSession session) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        if (!contestManagementService.getContestById(contestId).getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        String teacherId = session.getAttribute("user").toString();
        ResponseObject cloneContestResponse = contestManagementService.cloneContest(contest, teacherId, contestId, groupId);
        if (!cloneContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest";
    }

    private boolean checkValid(String classId, String groupId) {
        return subjectClassManagementService.getClassById(classId).getStatus().equals(HttpStatus.OK) && subjectClassGroupManagementService.getGroupById(groupId).getStatus().equals(HttpStatus.OK);
    }

    private List<Contest> getContest(Map<String, Object> res) {
        return (List<Contest>) res.getOrDefault("data", null);
    }
}
