package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/teacher/class/{classId}/group/{groupId}/contest")
public class TeacherContestOfGroupController {
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestManagementService contestManagementService;

    @GetMapping("")
    public String showContestsOfGroupTeacherOwn(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, Model model, HttpSession session) {
        String teacherId = session.getAttribute("user").toString();
        model.addAttribute("pageTitle", "Danh sách bài thực hành");
        SubjectClassGroup group = (SubjectClassGroup) subjectClassGroupManagementService.getGroupById(groupId).getData();
        List<ContestDTO> contests = ((List<Contest>) contestManagementService.getAllContestsCreateByTeacher(teacherId).getData()).stream().map(item -> contestMapper.entityToDTO(item)).toList();
        model.addAttribute("contestAdd", new ContestDTO());
        model.addAttribute("contests", contests);
        model.addAttribute("groupName", group.getSubjectClassGroupName());
        return "/teacher/contest/contest-of-group";
    }

    @PostMapping("")
    public String searchContestsOfGroupTeacherOwn(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @RequestParam("keyword") String keyword, Model model, HttpSession session) {
        String teacherId = session.getAttribute("user").toString();
        model.addAttribute("pageTitle", "Danh sách bài thực hành");
        SubjectClassGroup group = (SubjectClassGroup) subjectClassGroupManagementService.getGroupById(groupId).getData();
        List<ContestDTO> contests = ((List<Contest>) contestManagementService.searchAllContestsCreateByTeacher(teacherId, keyword).getData()).stream().map(item -> contestMapper.entityToDTO(item)).toList();
        model.addAttribute("contests", contests);
        model.addAttribute("contestAdd", new ContestDTO());
        model.addAttribute("groupName", group.getSubjectClassGroupName());
        return "/teacher/contest/contest-of-group";
    }

    @PostMapping("/add")
    public String addContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @ModelAttribute("contest") ContestDTO contest, HttpSession session) {
        String teacherId = session.getAttribute("user").toString();
        ResponseObject addContestResponse = contestManagementService.addContest(contest, teacherId, groupId);
        if (!addContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest";
    }

    @GetMapping("/{contestId}/lock")
    public String lockContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId) {
        ResponseObject lockContestResponse = contestManagementService.lockContest(contestId);
        if (!lockContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest";
    }

    @GetMapping("/{contestId}/unlock")
    public String unlockContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId) {
        ResponseObject lockContestResponse = contestManagementService.unlockContest(contestId);
        if (!lockContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest";
    }

    @GetMapping("/{contestId}/edit")
    public String showEditContestPage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model) {
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
        ResponseObject editContestResponse = contestManagementService.editContest(contest, contestId);
        if (!editContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest";
    }

    @GetMapping("/clone")
    public String showContestsInSystemPage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Chọn bài thực hành");
        List<ContestDTO> contests = ((List<Contest>) contestManagementService.getAllContestsActive().getData()).stream().map(item -> contestMapper.entityToDTO(item)).toList();
        model.addAttribute("contests", contests);
        return "/teacher/contest/contest-of-group-clone";
    }

    @PostMapping("/clone")
    public String searchContestsInSystem(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, Model model, @RequestParam("keyword") String keyword) {
        model.addAttribute("pageTitle", "Chọn bài thực hành");
        List<ContestDTO> contests = ((List<Contest>) contestManagementService.searchAllContestsActive(keyword).getData()).stream().map(item -> contestMapper.entityToDTO(item)).toList();
        model.addAttribute("contests", contests);
        return "/teacher/contest/contest-of-group-clone";
    }

    @GetMapping("/{contestId}/clone")
    public String showClonePage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Clone bài thực hành");
        model.addAttribute("contest", new ContestDTO());
        return "/teacher/contest/contest-of-group-clone-save";
    }

    @PostMapping("/{contestId}/clone")
    public String cloneContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @ModelAttribute("contest") ContestDTO contest, HttpSession session) {
        String teacherId = session.getAttribute("user").toString();
        ResponseObject cloneContestResponse = contestManagementService.cloneContest(contest, teacherId, contestId, groupId);
        if (!cloneContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest";
    }
}
