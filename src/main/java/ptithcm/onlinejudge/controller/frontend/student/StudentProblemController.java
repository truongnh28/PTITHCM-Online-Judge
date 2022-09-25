package ptithcm.onlinejudge.controller.frontend.student;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ptithcm.onlinejudge.data.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ptithcm.onlinejudge.dto.ProblemDTO;

@Controller
@RequestMapping("/student/problem")
public class StudentProblemController {
    @GetMapping("")
    public String showProblemListPage(Model model) {
        model.addAttribute("pageTitle", "Bài tập");
        model.addAttribute("problems", Data.problemList);
        return "/student/problem";
    }

    @GetMapping("/{problemId}")
    public String showProblemDetailPage(@PathVariable("problemId") String problemId, Model model) {
        model.addAttribute("pageTitle", problemId);
        ProblemDTO problemDetail = new ProblemDTO();
        for (ProblemDTO problem: Data.problemList) {
            if (problem.getProblemId().equals(problemId)) {
                problemDetail = problem;
                break;
            }
        }
        model.addAttribute("problemDetail", problemDetail);
        return "/student/problem-detail";
    }
}
