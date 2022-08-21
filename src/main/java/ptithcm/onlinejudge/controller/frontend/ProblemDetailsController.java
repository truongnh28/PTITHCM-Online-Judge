package ptithcm.onlinejudge.controller.frontend;

import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.ProblemDetailsDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProblemDetailsController {
    @GetMapping("/problem/{problemId}")
    public String getProblemDetails(@PathVariable("problemId") String problemId, Model model) {
        ProblemDetailsDTO problemDetails = new ProblemDetailsDTO();
        for (ProblemDetailsDTO problem: Data.problemDetailsList) {
            if (problem.getId().equals(problemId)) {
                problemDetails = problem;
                break;
            }
        }
        model.addAttribute("problemDetails", problemDetails);
        return "problem";
    }
}