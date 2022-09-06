package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.ContestDTO;
import ptithcm.onlinejudge.dto.GroupHasContestDTO;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.dto.UserLogin;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teacher/contest")
public class TeacherContestController {
    @GetMapping("/group")
    public String showGroupPage(Model model) {
        model.addAttribute("pageTitle", "Danh sách nhóm thực hành");
        model.addAttribute("groups", Data.subjectClassGroupList);
        return "/teacher/contest-group";
    }

    @GetMapping("/group/{groupId}")
    public String showContestInGroup(@PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Danh sách bài thực hành");
        List<ContestDTO> contestList = new ArrayList<>();
        for (ContestDTO contest : Data.contestList) {
            if (!contest.isHide() && Data.groupHasContestList.contains(new GroupHasContestDTO(contest.getContestId(), groupId))) {
                contestList.add(contest);
            }
        }
        model.addAttribute("contests", contestList);
        return "/teacher/contest-of-group";
    }

    @GetMapping("/group/{groupId}/add")
    public String showAddContestForm(@PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Thêm bài thực hành");
        ContestDTO contest = new ContestDTO();
        model.addAttribute("contest", contest);
        return "/teacher/contest-of-group-add";
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
        contest.setAuthor(foundTeacher.get());
        contest.setHide(false);

        Data.contestList.add(contest);
        Data.groupHasContestList.add(new GroupHasContestDTO(contest.getContestId(), groupId));
        return "redirect:/teacher/contest/group/{groupId}";
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

    @GetMapping("/{contestId}/group/{groupId}/delete")
    public String deleteContest(@PathVariable("contestId") String contestId, @PathVariable("groupId") String groupId) {
        Optional<ContestDTO> foundContest = findContestById(contestId);
        if (foundContest.isEmpty())
            return "redirect:/error";
        ContestDTO contest = foundContest.get();
        contest.setHide(false);
        return "redirect:/teacher/contest/group/{groupId}";
    }
}
