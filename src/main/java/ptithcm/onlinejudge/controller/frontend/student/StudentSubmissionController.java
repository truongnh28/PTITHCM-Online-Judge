package ptithcm.onlinejudge.controller.frontend.student;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ptithcm.onlinejudge.data.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ptithcm.onlinejudge.dto.SubmissionDetailDTO;

@Controller
@RequestMapping("/student/submission")
public class StudentSubmissionController {
    @GetMapping("")
    public String getSubmissions(Model model) {
        model.addAttribute("pageTitle", "Bài nộp");
        model.addAttribute("submissionList", Data.submissionList);
        return "/student/submission";
    }

    @GetMapping("/{codeId}")
    public String getSubmissionCode(@PathVariable("codeId") String codeId, Model model) {
        model.addAttribute("pageTitle", "Chi tiết bài nộp");
        SubmissionDetailDTO submissionCode = new SubmissionDetailDTO();
        for (SubmissionDetailDTO submissionDetailDTO : Data.submissionCodeList) {
            if (submissionDetailDTO.getSourceCodeId().equals(codeId)) {
                submissionCode = submissionDetailDTO;
                break;
            }
        }
        model.addAttribute("submissionCode", submissionCode);
        return "student/submission-detail";
    }
}
