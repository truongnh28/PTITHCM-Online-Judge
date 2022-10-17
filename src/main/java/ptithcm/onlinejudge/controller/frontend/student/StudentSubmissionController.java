package ptithcm.onlinejudge.controller.frontend.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ptithcm.onlinejudge.data.Data;
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
import ptithcm.onlinejudge.services.SubmissionManagementService;
import ptithcm.onlinejudge.services.SubmitService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/group/{groupId}/contest/{contestId}/submission")
public class StudentSubmissionController {
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestManagementService contestManagementService;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private SubmissionManagementService submissionManagementService;
    @GetMapping("")
    public String showSubmissionsPage(@PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Danh sách bài nộp");
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDetailDTO contest = contestMapper.entityToDetailDTO((Contest) getContestByIdResponse.getData());
        List<SubmissionDTO> submissions = ((List<Submission>) submissionManagementService.getAllSubmission().getData())
                .stream().map(item -> submissionMapper.entityToDTO(item)).collect(Collectors.toList());
        model.addAttribute("submissions", submissions);
        model.addAttribute("contest", contest);
        return "/student/submission";
    }

    @GetMapping("/{submissionId}")
    public String showSubmissionDetail(@PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @PathVariable("submissionId") String submissionId, Model model) {
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
}
