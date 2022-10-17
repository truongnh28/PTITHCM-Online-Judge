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
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.ContestManagementService;
import ptithcm.onlinejudge.services.ProblemManagementService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/group/{groupId}/contest/{contestId}/problem")
public class StudentProblemController {
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestManagementService contestManagementService;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemManagementService problemManagementService;
    @GetMapping("")
    public String showProblemListPage(@PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Bài tập");
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDetailDTO contest = contestMapper.entityToDetailDTO((Contest) getContestByIdResponse.getData());
        List<ProblemDTO> problems = ((List<Problem>)problemManagementService.getAllProblemsActiveOfContest(contestId).getData())
                .stream().map(item -> problemMapper.entityToDTO(item)).collect(Collectors.toList());
        model.addAttribute("problems", problems);
        model.addAttribute("contest", contest);
        return "/student/problem";
    }

    @GetMapping("/{problemId}")
    public String showProblemDetailPage(@PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId, Model model) {
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
}
