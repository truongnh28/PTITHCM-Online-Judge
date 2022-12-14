package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.*;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.mapper.SubjectClassGroupMapper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.model.request.ContestHasProblemRequest;
import ptithcm.onlinejudge.model.request.ContestRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.ContestHasProblemManagementService;
import ptithcm.onlinejudge.services.ContestManagementService;
import ptithcm.onlinejudge.services.ProblemManagementService;
import ptithcm.onlinejudge.services.SubjectClassGroupManagement;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/contest")
public class TeacherContestController {
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ContestHasProblemManagementService contestHasProblemManagementService;
    @Autowired
    private ContestManagementService contestManagementService;
    @Autowired
    private SubjectClassGroupMapper subjectClassGroupMapper;
    @Autowired
    private SubjectClassGroupManagement subjectClassGroupManagement;
    @Autowired
    private ProblemManagementService problemManagementService;
    @GetMapping("/group")
    public String showGroupPage(Model model) {
        model.addAttribute("pageTitle", "Danh sách nhóm thực hành");
        List<SubjectClassGroupDTO> groups = ((List<SubjectClassGroup>) subjectClassGroupManagement.getAllSubjectClassGroup().getData())
                .stream().map(item -> subjectClassGroupMapper.entityToDTO(item)).toList();
        model.addAttribute("groups", groups);
        return "/teacher/contest/contest-group";
    }

    // CONTEST OF GROUP
    @GetMapping("/group/{groupId}")
    public String showContestInGroup(@PathVariable("groupId") String groupId, Model model, HttpSession session) {
        model.addAttribute("pageTitle", "Danh sách bài thực hành");
        String teacherId = ((Login) session.getAttribute("user")).getUsername();
        List<ContestDTO> contests = ((List<Contest>) contestManagementService.getAllContestActiveCreatedByTeacher(teacherId).getData())
                .stream().map(item -> contestMapper.entityToDTO(item)).toList();
        model.addAttribute("contests", contests);
        return "/teacher/contest/contest-of-group";
    }

    @GetMapping("/group/{groupId}/add")
    public String showAddContestForm(@PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Thêm bài thực hành");
        ContestDTO contest = new ContestDTO();
        model.addAttribute("contest", contest);
        return "/teacher/contest/contest-of-group-add";
    }

    @GetMapping("/group/{groupId}/choose")
    public String showChooseContestPage(@PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Không có tiêu đề");
        List<ContestDTO> contests = ((List<Contest>) contestManagementService.getAllContestActive().getData())
                .stream().map(item -> contestMapper.entityToDTO(item)).toList();
        model.addAttribute("contests", contests);
        return "/teacher/contest/contest-of-group-choose-contest";
    }

    @GetMapping("/{contestId}")
    public String showContestInformation(@PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Thông tin bài thực hành");
        ContestDTO contest = contestMapper.entityToDTO((Contest) contestManagementService.getContestById(contestId).getData());
        model.addAttribute("contest", contest);
        return "/teacher/contest/contest-information";
    }

    @GetMapping("/{contestId}/group/{groupId}")
    public String showContestClonePage(@PathVariable("contestId") String contestId, @PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Clone bài thực hành");
        ContestDTO contest = new ContestDTO();
        model.addAttribute("contest", contest);
        return "/teacher/contest/contest-of-group-clone";
    }

    @GetMapping("/{contestId}/problem")
    public String showProblemOfContestPage(@PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Danh sách bài tập");
        List<ProblemDTO> problems = ((List<Problem>) problemManagementService.getAllProblemsOfContest(contestId).getData())
                .stream().map(item -> problemMapper.entityToDTO(item)).toList();
        model.addAttribute("problems", problems);
        return "/teacher/contest/contest-information-problem";
    }

    @GetMapping("/{contestId}/group/{groupId}/edit")
    public String showEditContestPage(@PathVariable("contestId") String contestId, @PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Cập nhật bài thực hành");
        ResponseObject findContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!findContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDTO contest = contestMapper.entityToDTO((Contest) findContestByIdResponse.getData());
        model.addAttribute("contest", contest);
        return "/teacher/contest/contest-of-group-edit";
    }

    @GetMapping("/{contestId}/group/{groupId}/delete")
    public String deleteContest(@PathVariable("contestId") String contestId, @PathVariable("groupId") String groupId) {
        ResponseObject deleteContestResponse = contestManagementService.deleteContest(contestId);
        if (!deleteContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/contest/group/{groupId}";
    }

    @GetMapping("/{contestId}/problem/edit")
    public String addProblemToContestPage(@PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Thêm bớt bài tập vào bài thực hành");
        List<ProblemShowDTO> problemShows = (List<ProblemShowDTO>) problemManagementService.getAllProblemsForAddingOrRemovingContest(contestId).getData();
        model.addAttribute("problems", problemShows);
        return "/teacher/contest/contest-of-group-edit-problem";
    }

    @GetMapping("/{contestId}/problem/{problemId}/add")
    public String addProblemToContest(@PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId) {
        ResponseObject responseAddProblemToContest = contestHasProblemManagementService.addContestProblem(new ContestHasProblemRequest(contestId, problemId));
        if (!responseAddProblemToContest.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/contest/{contestId}/problem/edit";
    }

    @GetMapping("/{contestId}/problem/{problemId}/delete")
    public String deleteProblemFromContest(@PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId) {
        ResponseObject responseDeleteProblemFromContest = contestHasProblemManagementService.deleteContestProblem(new ContestHasProblemRequest(contestId, problemId));
        if (!responseDeleteProblemFromContest.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/contest/{contestId}/problem/edit";
    }

    @PostMapping("/group/{groupId}/add")
    public String addContest(@PathVariable("groupId") String groupId,
                             @ModelAttribute("contest") ContestDTO contest,
                             HttpSession session) {
        String teacherId = ((Login) session.getAttribute("user")).getUsername();
        ResponseObject addContestResponse = contestManagementService.addContestDTOAndTeacherId(contest, teacherId);
        if (!addContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/contest/group/{groupId}";
    }

    @PostMapping("/{contestId}/group/{groupId}/edit")
    public String editContest(@PathVariable("contestId") String contestId, @PathVariable("groupId") String groupId, @ModelAttribute ContestDTO newContest) {
        ResponseObject editContestResponse = contestManagementService.editContestDTO(newContest);
        if (!editContestResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/contest/group/{groupId}";
    }
}
