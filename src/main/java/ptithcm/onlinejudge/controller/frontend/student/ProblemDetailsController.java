package ptithcm.onlinejudge.controller.frontend.student;

import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.ProblemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProblemDetailsController {
    @GetMapping("/problem/{problemId}")
    public String getProblemDetails(@PathVariable("problemId") String problemId, Model model) {
        ProblemDTO problemDetails = new ProblemDTO();
        for (ProblemDTO problem: Data.problemList) {
            if (problem.getProblemId().equals(problemId)) {
                problemDetails = problem;
                break;
            }
        }
        model.addAttribute("problemDetails", problemDetails);
        return "/student/problem";
    }
}
