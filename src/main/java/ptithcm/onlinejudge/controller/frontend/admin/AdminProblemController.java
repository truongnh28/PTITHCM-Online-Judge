package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.ProblemDTO;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminProblemController {
    @GetMapping("/admin/problems")
    public String getProblems(Model model) {
        List<ProblemDTO> problemList = new ArrayList<>();
        for (ProblemDTO problem: Data.problemList) {
            if (!problem.isHide())
                problemList.add(problem);
        }
        model.addAttribute("problems", problemList);
        return "/admin/problems";
    }
}
