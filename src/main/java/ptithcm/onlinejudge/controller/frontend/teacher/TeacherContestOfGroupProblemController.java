package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.ProblemDTO;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/class/{classId}/group/{groupId}/contest/{contestId}/problem")
public class TeacherContestOfGroupProblemController {
    @Autowired
    private SubjectClassManagementService subjectClassManagementService;
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;
    @Autowired
    private ContestManagementService contestManagementService;
    @Autowired
    private ContestHasProblemManagementService contestHasProblemManagementService;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemManagementService problemManagementService;

    @GetMapping("")
    public String getProblems(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        if (!contestManagementService.getContestById(contestId).getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest/{contestId}/problem/page/1";
    }

    @GetMapping("/page/{page}")
    public String getProblemsOfContest(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("page") int page, @Param("keyword") String keyword, @PathVariable("contestId") String contestId, Model model) {
        if (!checkValid(classId, groupId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Bài tập");
        ResponseObject getProblemsActiveOfContestResponse = problemManagementService.getAllProblemsActiveOfContest(contestId);
        if (!getProblemsActiveOfContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ResponseObject getContestById = contestManagementService.getContestById(contestId);
        if (!getContestById.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        Contest contest = (Contest) getContestById.getData();
        List<ProblemDTO> currProblems = ((List<Problem>) getProblemsActiveOfContestResponse.getData()).stream().map(item -> problemMapper.entityToDTO(item)).toList();
        Map<String, Object> response = (Map<String, Object>) problemManagementService.getAllProblemsActiveNotInContest(contestId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) problemManagementService.searchAllProblemsActiveNotInContest(contestId, keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<ProblemDTO> problems = getProblems(response).stream().map(item -> problemMapper.entityToDTO(item)).collect(Collectors.toList());
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = String.format("/teacher/class/%s/group/%s/contest/%s/problem", classId, groupId, contestId);
        model.addAttribute("currProblems", currProblems);
        model.addAttribute("problems", problems);
        model.addAttribute("contestName", contest.getContestName());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
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

    private boolean checkValid(String classId, String groupId) {
        return subjectClassManagementService.getClassById(classId).getStatus().equals(HttpStatus.OK) && subjectClassGroupManagementService.getGroupById(groupId).getStatus().equals(HttpStatus.OK);
    }

    private List<Problem> getProblems(Map<String, Object> response) {
        return (List<Problem>) response.getOrDefault("data", null);
    }
}
