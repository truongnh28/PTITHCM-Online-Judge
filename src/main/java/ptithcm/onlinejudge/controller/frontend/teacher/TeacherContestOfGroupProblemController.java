package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.ProblemDTO;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.ContestHasProblemManagementService;
import ptithcm.onlinejudge.services.ContestManagementService;
import ptithcm.onlinejudge.services.ProblemManagementService;

import java.util.List;

@Controller
@RequestMapping("/teacher/class/{classId}/group/{groupId}/contest/{contestId}/problem")
public class TeacherContestOfGroupProblemController {
    @Autowired
    private ContestManagementService contestManagementService;
    @Autowired
    private ContestHasProblemManagementService contestHasProblemManagementService;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemManagementService problemManagementService;

    @GetMapping("")
    public String getProblemsOfContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Bài tập");
        ResponseObject getProblemsActiveOfContestResponse = problemManagementService.getAllProblemsActiveOfContest(contestId);
        if (!getProblemsActiveOfContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ResponseObject getContestById = contestManagementService.getContestById(contestId);
        if (!getContestById.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        Contest contest = (Contest) getContestById.getData();
        ResponseObject getProblemsActiveNotInContestResponse = problemManagementService.getAllProblemsActiveNotInContest(contestId);
        List<ProblemDTO> currProblems = ((List<Problem>) getProblemsActiveOfContestResponse.getData()).stream().map(item -> problemMapper.entityToDTO(item)).toList();
        List<ProblemDTO> problems = ((List<Problem>) getProblemsActiveNotInContestResponse.getData()).stream().map(item -> problemMapper.entityToDTO(item)).toList();
        model.addAttribute("currProblems", currProblems);
        model.addAttribute("problems", problems);
        model.addAttribute("contestName", contest.getContestName());
        return "/teacher/contest/contest-of-group-problem";
    }

    @PostMapping("")
    public String searchProblems(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model, @RequestParam("keyword") String keyword) {
        model.addAttribute("pageTitle", "Bài tập");
        ResponseObject getProblemsActiveOfContestResponse = problemManagementService.getAllProblemsActiveOfContest(contestId);
        if (!getProblemsActiveOfContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ResponseObject getContestById = contestManagementService.getContestById(contestId);
        if (!getContestById.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        Contest contest = (Contest) getContestById.getData();
        ResponseObject searchProblemsActiveNotInContestResponse = problemManagementService.searchAllProblemsActiveNotInContest(contestId, keyword);
        List<ProblemDTO> currProblems = ((List<Problem>) getProblemsActiveOfContestResponse.getData()).stream().map(item -> problemMapper.entityToDTO(item)).toList();
        List<ProblemDTO> problems = ((List<Problem>) searchProblemsActiveNotInContestResponse.getData()).stream().map(item -> problemMapper.entityToDTO(item)).toList();
        model.addAttribute("currProblems", currProblems);
        model.addAttribute("problems", problems);
        model.addAttribute("contestName", contest.getContestName());
        return "/teacher/contest/contest-of-group-problem";
    }

    @GetMapping("/{problemId}/remove")
    public String removeProblemFromContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId) {
        ResponseObject removeProblemFromContestResponse = contestHasProblemManagementService.deleteProblemFromContest(contestId, problemId);
        if (!removeProblemFromContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest/{contestId}/problem";
    }

    @GetMapping("/{problemId}/add")
    public String addProblemToContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId) {
        ResponseObject addProblemToContestResponse = contestHasProblemManagementService.addProblemToContest(contestId, problemId);
        if (!addProblemToContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest/{contestId}/problem";
    }

    @GetMapping("/{problemId}/info")
    public String showProblemInfoPage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId, Model model) {
        model.addAttribute("pageTitle", "Chi tiết bài tập");
        ResponseObject getProblemByIdResponse = problemManagementService.getProblemById(problemId);
        if (!getProblemByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ProblemDTO problem = problemMapper.entityToDTO((Problem) getProblemByIdResponse.getData());
        model.addAttribute("problem", problem);
        return "/teacher/contest/contest-of-group-problem-info";
    }

    @GetMapping("/clone")
    public String showProblemListForClonePage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Danh sách bài tập");
        ResponseObject getProblemsActiveOfContestResponse = problemManagementService.getAllProblemsActiveOfContest(contestId);
        if (!getProblemsActiveOfContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        List<ProblemDTO> problems = ((List<Problem>) getProblemsActiveOfContestResponse.getData()).stream().map(item -> problemMapper.entityToDTO(item)).toList();
        ResponseObject getContestById = contestManagementService.getContestById(contestId);
        if (!getContestById.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        Contest contest = (Contest) getContestById.getData();
        model.addAttribute("problems", problems);
        model.addAttribute("contestName", contest.getContestName());
        return "/teacher/contest/contest-of-group-clone-problem";
    }

    @GetMapping("/{problemId}/info/clone")
    public String showProblemInfoClonePage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId, Model model) {
        model.addAttribute("pageTitle", "Chi tiết bài tập");
        ResponseObject getProblemByIdResponse = problemManagementService.getProblemById(problemId);
        if (!getProblemByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ProblemDTO problem = problemMapper.entityToDTO((Problem) getProblemByIdResponse.getData());
        model.addAttribute("problem", problem);
        return "/teacher/contest/contest-of-group-clone-problem-info";
    }
}
