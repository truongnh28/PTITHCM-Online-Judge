package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.*;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.mapper.SubjectClassGroupMapper;
import ptithcm.onlinejudge.services.ContestManagementService;
import ptithcm.onlinejudge.services.SubjectClassGroupManagement;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teacher/contest")
public class TeacherContestController {
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestManagementService contestManagementService;
    @Autowired
    private SubjectClassGroupMapper subjectClassGroupMapper;
    @Autowired
    private SubjectClassGroupManagement subjectClassGroupManagement;
    @GetMapping("/group")
    public String showGroupPage(Model model) {
        model.addAttribute("pageTitle", "Danh sách nhóm thực hành");
        model.addAttribute("groups", Data.subjectClassGroupList);
//        List<SubjectClassGroup> subjectClassGroupEntities = (List<SubjectClassGroup>) subjectClassGroupManagement.getAllSubjectClassGroup().getData();
//        List<SubjectClassGroupDTO> subjectClassGroupList = subjectClassGroupEntities.stream().map(item -> subjectClassGroupMapper.entityToDTO(item)).toList();
//        model.addAttribute("groups", subjectClassGroupList);
        return "/teacher/contest-group";
    }

    @GetMapping("/group/{groupId}")
    public String showContestInGroup(@PathVariable("groupId") String groupId, Model model, HttpSession session) {
        model.addAttribute("pageTitle", "Danh sách bài thực hành");
        List<ContestDTO> contestList = new ArrayList<>();
        for (ContestDTO contest : Data.contestList) {
            if (!contest.isHide() && Data.groupHasContestList.contains(new GroupHasContestDTO(contest.getContestId(), groupId))) {
                contestList.add(contest);
            }
        }
        model.addAttribute("contests", contestList);
//        UserLogin userLogin = (UserLogin) session.getAttribute("user");
//        List<Contest> contestEntities = (List<Contest>) contestManagementService.getAllContestActiveCreatedByTeacher(userLogin.getUsername()).getData();
//        List<ContestDTO> contestList = contestEntities.stream().map(item -> contestMapper.entityToDTO(item)).toList();
//        model.addAttribute("contests", contestList);
        return "/teacher/contest-of-group";
    }

    @GetMapping("/group/{groupId}/add")
    public String showAddContestForm(@PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Thêm bài thực hành");
        ContestDTO contest = new ContestDTO();
        model.addAttribute("contest", contest);
        return "/teacher/contest-of-group-add";
    }

    @GetMapping("/group/{groupId}/choose")
    public String showChooseContestPage(@PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Không có tiêu đề");
        List<ContestDTO> contestList = Data.contestList;
        model.addAttribute("contests", contestList);
        return "/teacher/contest-of-group-choose-contest";
    }

    @GetMapping("/{contestId}")
    public String showContestInformation(@PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Thông tin bài thực hành");
        Optional<ContestDTO> foundContest = findContestById(contestId);
        if (foundContest.isEmpty())
            return "redirect:/error";
        model.addAttribute("contest", foundContest.get());
        return "/teacher/contest-information";
    }

    @GetMapping("/{contestId}/group/{groupId}")
    public String showContestClonePage(@PathVariable("contestId") String contestId, @PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Clone bài thực hành");
        ContestDTO contest = new ContestDTO();
        model.addAttribute("contest", contest);
        return "/teacher/contest-of-group-clone";
    }

    @GetMapping("/{contestId}/problem")
    public String showProblemOfContestPage(@PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Danh sách bài tập");
        List<ProblemDTO> problemList = new ArrayList<>();
        for (ContestHasProblemDTO contestHasProblem: Data.contestHasProblemList) {
            if (contestHasProblem.getContestId().equals(contestId)) {
                Optional<ProblemDTO> foundProblem = findProblemById(contestHasProblem.getProblemId());
                if (foundProblem.isEmpty())
                    return "redirect:/error";
                problemList.add(foundProblem.get());
            }
        }
        model.addAttribute("problems", problemList);
        return "/teacher/contest-information-problem";
    }

    @GetMapping("/{contestId}/group/{groupId}/edit")
    public String showEditContestPage(@PathVariable("contestId") String contestId, @PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Cập nhật bài thực hành");
        Optional<ContestDTO> foundContest = findContestById(contestId);
        if (foundContest.isEmpty())
            return "redirect:/error";
        model.addAttribute("contest", foundContest.get());
        return "/teacher/contest-of-group-edit";
    }

    @GetMapping("/{contestId}/group/{groupId}/delete")
    public String deleteContest(@PathVariable("contestId") String contestId, @PathVariable("groupId") String groupId) {
        Optional<ContestDTO> foundContest = findContestById(contestId);
        if (foundContest.isEmpty())
            return "redirect:/error";
        ContestDTO contest = foundContest.get();
        contest.setHide(false);
        return "redirect:/teacher/contest/group/{groupId}";
    }

    @GetMapping("/{contestId}/problem/edit")
    public String addProblemToContestPage(@PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Danh sách bài tập");
        List<ProblemShowDTO> problemShowList = new ArrayList<>();
        for (ProblemDTO problem: Data.problemList) {
            if (problem.isHide())
                continue;
            ProblemShowDTO problemShow = new ProblemShowDTO();
            problemShow.setId(problem.getId());
            problemShow.setProblemUrl(problem.getProblemUrl());
            problemShow.setProblemScore(problem.getProblemScore());
            problemShow.setProblemName(problem.getProblemName());
            problemShow.setTeacher(problem.getTeacher());
            problemShow.setLevel(problem.getLevel());
            problemShow.setProblemTimeLimit(problem.getProblemTimeLimit());
            problemShow.setProblemMemoryLimit(problem.getProblemMemoryLimit());
            problemShow.setDisabledButtonAdding(Data.contestHasProblemList.contains(new ContestHasProblemDTO(contestId, problem.getId())));
            problemShowList.add(problemShow);
        }
        model.addAttribute("problems", problemShowList);
        return "/teacher/contest-of-group-edit-problem";
    }

    @GetMapping("/{contestId}/problem/{problemId}/add")
    public String addProblemToContest(@PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId) {
        Data.contestHasProblemList.add(new ContestHasProblemDTO(contestId, problemId));
        return "redirect:/teacher/contest/{contestId}/problem/edit";
    }

    @GetMapping("/{contestId}/problem/{problemId}/delete")
    public String deleteProblemFromContest(@PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId) {
        Data.contestHasProblemList.remove(new ContestHasProblemDTO(contestId, problemId));
        return "redirect:/teacher/contest/{contestId}/problem/edit";
    }

    private Optional<ProblemDTO> findProblemById(String problemId) {
        boolean foundProblem = false;
        ProblemDTO problem = new ProblemDTO();
        for (ProblemDTO problemDTO: Data.problemList) {
            if (problemDTO.getId().equals(problemId)) {
                problem = problemDTO;
                foundProblem = true;
            }
        }
        return foundProblem ? Optional.of(problem) : Optional.empty();
    }

    private Optional<TeacherDTO> findTeacherById(String username) {
        boolean foundTeacher = false;
        TeacherDTO teacher = new TeacherDTO();
        for (TeacherDTO teacherDTO : Data.teacherList) {
            if (teacherDTO.getTeacherId().equals(username)) {
                teacher = teacherDTO;
                foundTeacher = true;
            }
        }
        return (foundTeacher) ? Optional.of(teacher) : Optional.empty();
    }

    private Optional<ContestDTO> findContestById(String contestId) {
        boolean foundContest = false;
        ContestDTO contest = new ContestDTO();
        for (ContestDTO contestDTO: Data.contestList) {
            if (contestDTO.getContestId().equals(contestId)) {
                contest = contestDTO;
                foundContest = true;
            }
        }
        return (foundContest) ? Optional.of(contest) : Optional.empty();
    }

    @PostMapping("/group/{groupId}/add")
    public String addContest(@PathVariable("groupId") String groupId,
                             @ModelAttribute("contest") ContestDTO contest,
                             HttpSession session) {
        String username = ((UserLogin) session.getAttribute("user")).getUsername();
        Optional<TeacherDTO> foundTeacher = findTeacherById(username);
        if (foundTeacher.isEmpty())
            return "redirect:/error";
        contest.setTeacher(foundTeacher.get());
        contest.setHide(false);

        Data.contestList.add(contest);
        Data.groupHasContestList.add(new GroupHasContestDTO(contest.getContestId(), groupId));
        return "redirect:/teacher/contest/group/{groupId}";
    }

    @PostMapping("/{contestId}/group/{groupId}/edit")
    public String editContest(@PathVariable("contestId") String contestId, @PathVariable("groupId") String groupId, @ModelAttribute ContestDTO newContest) {
        Optional<ContestDTO> foundContest = findContestById(contestId);
        if (foundContest.isEmpty())
            return "redirect:/error";
        ContestDTO contest = foundContest.get();
        contest.setContestName(newContest.getContestName());
        contest.setContestStart(newContest.getContestStart());
        contest.setContestEnd(newContest.getContestEnd());
        return "redirect:/teacher/contest/group/{groupId}";
    }
}
