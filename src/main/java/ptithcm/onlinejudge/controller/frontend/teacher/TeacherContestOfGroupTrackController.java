package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ptithcm.onlinejudge.dto.*;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.mapper.SubmissionMapper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.Submission;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/class/{classId}/group/{groupId}/contest/{contestId}")
public class TeacherContestOfGroupTrackController {
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private SubjectClassManagementService subjectClassManagementService;
    @Autowired
    private SubjectClassGroupManagementService subjectClassGroupManagementService;
    @Autowired
    private ContestManagementService contestManagementService;
    @Autowired
    private ProblemManagementService problemManagementService;
    @Autowired
    private SubmissionManagementService submissionManagementService;

    @Autowired
    private StatisticService statisticService;

    @GetMapping("/track")
    public String showContestPage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model) {
        if (!isValid(classId, groupId, contestId))
            return "redirect:/error";
        model.addAttribute("pageTitle", contestId);
        ContestDetailDTO contest = contestMapper.entityToDetailDTO(getContestById(contestId));
        model.addAttribute("contest", contest);
        return "/teacher/contest/contest-of-group-track";
    }

    @GetMapping("/problem/track")
    public String showProblemPage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model) {
        if (!isValid(classId, groupId, contestId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Bài tập");
        ContestDetailDTO contest = contestMapper.entityToDetailDTO(getContestById(contestId));
        List<ProblemDTO> problems = getProblemsByContest(contestId).stream().map(item -> problemMapper.entityToDTO(item)).toList();
        model.addAttribute("problems", problems);
        model.addAttribute("contest", contest);
        return "/teacher/contest/contest-of-group-problem-track";
    }

    @GetMapping("/problem/{problemId}/track")
    public String showProblemDetailPage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId, Model model) {
        if (!isValid(classId, groupId, contestId, problemId))
            return "redirect:/error";
        model.addAttribute("pageTitle", problemId);
        ContestDetailDTO contest = contestMapper.entityToDetailDTO(getContestById(contestId));
        ProblemDTO problem = problemMapper.entityToDTO(getProblemById(problemId));
        model.addAttribute("contest", contest);
        model.addAttribute("problemDetail", problem);
        return "/teacher/contest/contest-of-group-problem-detail-track";
    }

    @GetMapping("/submission/track")
    public String showSubmissions(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId) {
        if (!isValid(classId, groupId, contestId))
            return "redirect:/error";
        return "redirect:/teacher/class/{classId}/group/{groupId}/contest/{contestId}/submission/track/page/1";
    }

    @GetMapping("/submission/track/page/{page}")
    public String showSubmissionPage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @Param("keyword") String keyword, @PathVariable("page") int page, Model model) {
        if (!isValid(classId, groupId, contestId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Bài nộp");
        ContestDetailDTO contest = contestMapper.entityToDetailDTO(getContestById(contestId));
        Map<String, Object> response = (Map<String, Object>) submissionManagementService.getSubmissionsOfContest(contestId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) submissionManagementService.searchSubmissionsOfContestByKeyword(contestId, keyword, page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<SubmissionDTO> submissions = getSubmissions(response).stream().map(item -> submissionMapper.entityToDTO(item)).collect(Collectors.toList());
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = String.format("/teacher/class/%s/group/%s/contest/%s/submission/track", classId, groupId, contestId);
        model.addAttribute("submissions", submissions);
        model.addAttribute("contest", contest);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        return "/teacher/contest/contest-of-group-submission-track";
    }

    @GetMapping("/submission/{submissionId}/track")
    public String showSubmissionDetailPage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, @PathVariable("submissionId") String submissionId, Model model) {
        if (!isValidSubmission(classId, groupId, contestId, submissionId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Thông tin bài nộp");
        ContestDetailDTO contest = contestMapper.entityToDetailDTO(getContestById(contestId));
        SubmissionDetailDTO submission = submissionMapper.entityToDetailDTO(getSubmissionById(submissionId));
        model.addAttribute("contest", contest);
        model.addAttribute("submission", submission);
        return "/teacher/contest/contest-of-group-submission-detail-track";
    }

    @GetMapping("/statistic/track")
    public String showStatisticPage(@PathVariable("classId") String classId, @PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model) {
        if (!isValid(classId, groupId, contestId))
            return "redirect:/error";
        model.addAttribute("pageTitle", "Thống kê");
        ContestDetailDTO contest = contestMapper.entityToDetailDTO(getContestById(contestId));
        model.addAttribute("contest", contest);
        Map<String, Object> dataStatistic = getDataStatistic(contestId);
        Long sumACs = (Long) dataStatistic.getOrDefault("SumACs", null);
        Long sumSubs = (Long) dataStatistic.getOrDefault("SumSubs", null);
        Long sumStudentACs = (Long) dataStatistic.getOrDefault("SumStudentACs", null);
        Long sumStudentSubs = (Long) dataStatistic.getOrDefault("SumStudentSubs", null);
        if (sumACs == null || sumSubs == null || sumStudentSubs == null || sumStudentACs == null)
            return "redirect:/error";
        model.addAttribute("sumACs", sumACs);
        model.addAttribute("sumSubs", sumSubs);
        model.addAttribute("sumStudentACs", sumStudentACs);
        model.addAttribute("sumStudentSubs", sumStudentSubs);
        List<StatisticDTO> statistics = (List<StatisticDTO>) dataStatistic.getOrDefault("Detail", null);
        if (statistics == null)
            return "redirect:/error";
        model.addAttribute("statistics", statistics);
        return "/teacher/contest/contest-of-group-statistic-track";
    }

    private boolean isValid(String classId, String groupId, String contestId) {
        ResponseObject getClassByIdResponse = subjectClassManagementService.getClassById(classId);
        if (!getClassByIdResponse.getStatus().equals(HttpStatus.OK))
            return false;
        ResponseObject getGroupByIdResponse = subjectClassGroupManagementService.getGroupById(groupId);
        if (!getGroupByIdResponse.getStatus().equals(HttpStatus.OK))
            return false;
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        return getContestByIdResponse.getStatus().equals(HttpStatus.OK);
    }

    private boolean isValid(String classId, String groupId, String contestId, String problemId) {
        if (!isValid(classId, groupId, contestId))
            return false;
        ResponseObject getProblemByIdResponse = problemManagementService.getProblemById(problemId);
        return getProblemByIdResponse.getStatus().equals(HttpStatus.OK);
    }

    private boolean isValidSubmission(String classId, String groupId, String contestId, String submissionId) {
        if (!isValid(classId, groupId, contestId))
            return false;
        ResponseObject getSubmissionByIdResponse = submissionManagementService.getSubmissionById(submissionId);
        return getSubmissionByIdResponse.getStatus().equals(HttpStatus.OK);
    }

    private List<Submission> getSubmissions(Map<String, Object> response) {
        return (List<Submission>) response.getOrDefault("data", null);
    }

    private Map<String, Object> getDataStatistic(String contestId) {
        return (Map<String, Object>) statisticService.statisticContest(contestId).getData();
    }

    private Submission getSubmissionById(String submissionId) {
        return (Submission) submissionManagementService.getSubmissionById(submissionId).getData();
    }

    private Contest getContestById(String contestId) {
        return (Contest) contestManagementService.getContestById(contestId).getData();
    }

    private List<Problem> getProblemsByContest(String contestId) {
        return (List<Problem>) problemManagementService.getAllProblemsActiveOfContest(contestId).getData();
    }

    private Problem getProblemById(String problemId) {
        return (Problem) problemManagementService.getProblemById(problemId).getData();
    }
}
