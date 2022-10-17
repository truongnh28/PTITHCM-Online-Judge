package ptithcm.onlinejudge.controller.frontend.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import ptithcm.onlinejudge.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.model.adapter.GetStatusResponse;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.Submission;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.ContestManagementService;
import ptithcm.onlinejudge.services.ProblemManagementService;
import ptithcm.onlinejudge.services.SubmissionManagementService;
import ptithcm.onlinejudge.services.SubmitService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/student/group/{groupId}/contest/{contestId}/submit")
public class StudentSubmitController {
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestManagementService contestManagementService;
    @Autowired
    private SubmitService submitService;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemManagementService problemManagementService;
    @Autowired
    private SubmissionManagementService submissionManagementService;
    @GetMapping("/{problemId}")
    public String showSubmitPage(@PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId, Model model) {
        model.addAttribute("pageTitle", "Nộp bài");
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDetailDTO contest = contestMapper.entityToDetailDTO((Contest) getContestByIdResponse.getData());
        ResponseObject responseGetProblemById = problemManagementService.getProblemById(problemId);
        if (!responseGetProblemById.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ProblemDTO problemDetails = problemMapper.entityToDTO((Problem) responseGetProblemById.getData());
        model.addAttribute("problemDetails", problemDetails);
        model.addAttribute("contest", contest);
        return "/student/submit";
    }

    @PostMapping("/{problemId}")
    public String submitCode(@PathVariable("groupId") String groupId,
                             @PathVariable("contestId") String contestId,
                             @PathVariable("problemId") String problemId,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam("language") String language,
                             HttpSession session) throws InterruptedException {
        String studentId = session.getAttribute("user").toString();
        ResponseObject submitProblemResponse = submitService.submitProblemFromController(studentId, problemId, contestId, language, file);
        if (!submitProblemResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        Submission submission = (Submission) submitProblemResponse.getData();
        updateVerdict(submission);
        return "redirect:/student/contest/{contestId}/submission/" + submission.getId();
    }

    @Async
    public void updateVerdict(Submission submission) throws InterruptedException {
        String id = submission.getId();
        while(true) {
            ResponseObject getStatusResponse = submitService.getStatusAdapter(id);
            GetStatusResponse status = (GetStatusResponse) getStatusResponse.getData();
            if (!(status.getStatus().equals("queued") || status.getStatus().equals("judging"))) {
                submissionManagementService.getSubmissionById(id);
                break;
            }
        }

    }
}
