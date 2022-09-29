package ptithcm.onlinejudge.controller.frontend.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ptithcm.onlinejudge.dto.ContestDTO;
import ptithcm.onlinejudge.dto.ContestDetailDTO;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.ContestManagementService;

import java.util.List;

@Controller
@RequestMapping("/student/contest")
public class StudentContestController {
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestManagementService contestManagementService;

    @GetMapping("")
    public String showContestsPage(Model model) {
        model.addAttribute("pageTitle", "Danh sách bài thực hành");
        List<ContestDTO> contests = ((List<Contest>) contestManagementService.getAllContestActiveSortByDate().getData())
                .stream().map(item -> contestMapper.entityToDTO(item)).toList();
        model.addAttribute("contests", contests);
        return "/student/contest";
    }

    @GetMapping("/{contestId}")
    public String showContestPage(@PathVariable("contestId") String contestId, Model model) {
        model.addAttribute("pageTitle", "Thông tin bài thực hành");
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDetailDTO contest = contestMapper.entityToDetailDTO((Contest) getContestByIdResponse.getData());
        model.addAttribute("contest", contest);
        return "/student/contest-detail";
    }
}
