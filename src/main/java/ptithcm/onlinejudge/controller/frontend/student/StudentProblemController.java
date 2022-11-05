package ptithcm.onlinejudge.controller.frontend.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ptithcm.onlinejudge.dto.ContestDetailDTO;
import ptithcm.onlinejudge.dto.ProblemDTO;
import ptithcm.onlinejudge.dto.ProblemSolvedDTO;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.Submission;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.SubmissionRepository;
import ptithcm.onlinejudge.services.ContestManagementService;
import ptithcm.onlinejudge.services.ProblemManagementService;
import ptithcm.onlinejudge.services.SubjectClassGroupManagementService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/group/{groupId}/contest/{contestId}/problem")
public class StudentProblemController {
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestManagementService contestManagementService;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemManagementService problemManagementService;

    @GetMapping("")
    public String showProblemListPage(@PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        if (!isValid(groupId, contestId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Bài tập");
        String studentId = session.getAttribute("user").toString();
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDetailDTO contest = contestMapper.entityToDetailDTO((Contest) getContestByIdResponse.getData());
        List<ProblemSolvedDTO> problems = ((List<Problem>)problemManagementService.getAllProblemsActiveOfContest(contestId).getData())
                .stream().map(item -> {
                    ProblemSolvedDTO problemSolvedDTO = problemMapper.entityToSolvedDTO(item);
                    List<Submission> submissions = submissionRepository.problemSolvedByStudent(contestId, item.getId(), studentId);
                    problemSolvedDTO.setSolved(false);
                    if (!submissions.isEmpty()) problemSolvedDTO.setSolved(true);
                    return problemSolvedDTO;
                }).collect(Collectors.toList());
        model.addAttribute("problems", problems);
        model.addAttribute("contest", contest);
        return "/student/problem";
    }

    @GetMapping("/{problemId}")
    public String showProblemDetailPage(@PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId, Model model, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        if (!isValid(groupId, contestId))
            return "redirect:/error";
        model.addAttribute("pageTitle", problemId);
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDetailDTO contest = contestMapper.entityToDetailDTO((Contest) getContestByIdResponse.getData());
        ResponseObject getProblemByIdResponse = problemManagementService.getProblemById(problemId);
        if (!getProblemByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ProblemDTO problem = problemMapper.entityToDTO((Problem) getProblemByIdResponse.getData());
        model.addAttribute("problemDetail", problem);
        model.addAttribute("contest", contest);
        return "/student/problem-detail";
    }

    private boolean isExpired(HttpSession session) {
        return session.getAttribute("user") == null;
    }

    private boolean isValid(String groupId, String contestId) {
        return subjectClassGroupManagementService.getGroupById(groupId).getStatus().equals(HttpStatus.OK) && contestManagementService.getContestById(contestId).getStatus().equals(HttpStatus.OK);
    }
}
