package ptithcm.onlinejudge.controller.frontend.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.ContestDetailDTO;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.ContestManagementService;
import ptithcm.onlinejudge.services.SubjectClassGroupManagementService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/group/{groupId}/contest")
public class StudentContestController {
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestManagementService contestManagementService;

    @GetMapping("")
    public String showContest(@PathVariable("groupId") String groupId, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        if (!isValid(groupId))
            return "redirect:/error";
        return "redirect:/student/group/{groupId}/contest/page/1";
    }

    @GetMapping("/page/{page}")
    public String showContestsPage(@PathVariable("groupId") String groupId, @PathVariable("page") int page, @Param("keyword") String keyword, Model model, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        if (!isValid(groupId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Bài thực hành");
        Map<String, Object> response = (Map<String, Object>) contestManagementService.getContestActiveSortByDate(page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) contestManagementService.searchContestsActiveSortByDate(keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<ContestDetailDTO> contests = getContests(response).stream().map(item -> contestMapper.entityToDetailDTO(item)).collect(Collectors.toList());
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = String.format("/student/group/%s/contest", groupId);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        model.addAttribute("contests", contests);
        return "/student/contest";
    }

    @GetMapping("/{contestId}")
    public String showContestPage(@PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        model.addAttribute("pageTitle", "Thông tin bài thực hành");
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDetailDTO contest = contestMapper.entityToDetailDTO((Contest) getContestByIdResponse.getData());
        model.addAttribute("contest", contest);
        return "/student/contest-detail";
    }

    private boolean isExpired(HttpSession session) {
        return session.getAttribute("user") == null;
    }

    private boolean isValid(String groupId) {
        return subjectClassGroupManagementService.getGroupById(groupId).getStatus().equals(HttpStatus.OK);
    }

    private List<Contest> getContests(Map<String, Object> res) {
        return (List<Contest>) res.getOrDefault("data", null);
    }
}
