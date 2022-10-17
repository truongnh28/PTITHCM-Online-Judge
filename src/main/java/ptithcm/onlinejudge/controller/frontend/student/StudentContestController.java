package ptithcm.onlinejudge.controller.frontend.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.ContestDetailDTO;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.ContestManagementService;

import java.util.List;

@Controller
@RequestMapping("/student/group/{groupId}/contest")
public class StudentContestController {
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestManagementService contestManagementService;

    @GetMapping("")
    public String showContestsPage(@PathVariable("groupId") String groupId, Model model) {
        model.addAttribute("pageTitle", "Bài thực hành");
        List<ContestDetailDTO> contests = ((List<Contest>) contestManagementService.getContestActiveSortByDate().getData())
                .stream().map(item -> contestMapper.entityToDetailDTO(item)).toList();
        model.addAttribute("contests", contests);
        return "/student/contest";
    }

    @PostMapping("")
    public String searchContests(@PathVariable("groupId") String groupId, Model model, @RequestParam("keyword") String keyword) {
        model.addAttribute("pageTitle", "Bài thực hành");
        List<ContestDetailDTO> contests = ((List<Contest>) contestManagementService.searchContestsActiveSortByDate(keyword).getData())
                .stream().map(item -> contestMapper.entityToDetailDTO(item)).toList();
        model.addAttribute("contests", contests);
        return "/student/contest";
    }

    @GetMapping("/{contestId}")
    public String showContestPage(@PathVariable("groupId") String groupId, @PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Thông tin bài thực hành");
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDetailDTO contest = contestMapper.entityToDetailDTO((Contest) getContestByIdResponse.getData());
        model.addAttribute("contest", contest);
        return "/student/contest-detail";
    }
}
