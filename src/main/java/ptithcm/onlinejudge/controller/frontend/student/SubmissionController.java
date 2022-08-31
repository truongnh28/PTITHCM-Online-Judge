package ptithcm.onlinejudge.controller.frontend.student;

import ptithcm.onlinejudge.data.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SubmissionController {
    @GetMapping("submissions")
    public String getSubmissions(Model model) {
        model.addAttribute("submissionList", Data.submissionList);
        return "/student/submissions";
    }
}
