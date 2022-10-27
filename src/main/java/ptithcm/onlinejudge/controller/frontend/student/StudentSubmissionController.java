package ptithcm.onlinejudge.controller.frontend.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ptithcm.onlinejudge.dto.ContestDetailDTO;
import ptithcm.onlinejudge.dto.SubmissionDTO;
import ptithcm.onlinejudge.dto.SubmissionDetailDTO;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.mapper.SubmissionMapper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Submission;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.ContestManagementService;
import ptithcm.onlinejudge.services.SubjectClassGroupManagementService;
import ptithcm.onlinejudge.services.SubmissionManagementService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/group/{groupId}/contest/{contestId}/submission")
public class StudentSubmissionController {
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestManagementService contestManagementService;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private SubmissionManagementService submissionManagementService;

    @GetMapping("")
    public String showSubmissions(@PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        if (!isValid(groupId, contestId))
            return "redirect:/error";
        return "redirect:/student/group/{groupId}/contest/{contestId}/submission/page/1";
    }
    @GetMapping("/page/{page}")
    public String showSubmissionsPage(@PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @Param("keyword") String keyword, @PathVariable("page") int page, Model model, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        if (!isValid(groupId, contestId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Danh sách bài nộp");
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDetailDTO contest = contestMapper.entityToDetailDTO((Contest) getContestByIdResponse.getData());
        Map<String, Object> response = (Map<String, Object>) submissionManagementService.getSubmissionsOfContest(contestId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) submissionManagementService.searchSubmissionsOfContestByKeyword(contestId, keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<SubmissionDTO> submissions = getSubmissions(response).stream().map(item -> submissionMapper.entityToDTO(item)).collect(Collectors.toList());
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = String.format("/student/group/%s/contest/%s/submission", groupId, contestId);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        model.addAttribute("submissions", submissions);
        model.addAttribute("contest", contest);
        return "/student/submission";
    }

    @GetMapping("/{submissionId}")
    public String showSubmissionDetail(@PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @PathVariable("submissionId") String submissionId, Model model, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        if (!isValid(groupId, contestId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Bài nộp");
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDetailDTO contest = contestMapper.entityToDetailDTO((Contest) getContestByIdResponse.getData());
        ResponseObject responseObject = submissionManagementService.getSubmissionById(submissionId);
        if (!responseObject.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        SubmissionDetailDTO submission = submissionMapper.entityToDetailDTO((Submission) responseObject.getData());
        model.addAttribute("submission", submission);
        model.addAttribute("contest", contest);
        return "student/submission-detail";
    }

    private boolean isExpired(HttpSession session) {
        return session.getAttribute("user") == null;
    }

    private boolean isValid(String groupId, String contestId) {
        return subjectClassGroupManagementService.getGroupById(groupId).getStatus().equals(HttpStatus.OK) && contestManagementService.getContestById(contestId).getStatus().equals(HttpStatus.OK);
    }

    private List<Submission> getSubmissions(Map<String, Object> res) {
        return (List<Submission>) res.getOrDefault("data", null);
    }
}
