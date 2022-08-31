package ptithcm.onlinejudge.controller.frontend.student;

import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.SubmissionDetailDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SubmissionCodeController{
    @GetMapping("/submission/{codeId}")
    public String getSubmissionCode(@PathVariable("codeId") String codeId, Model model) {
        SubmissionDetailDTO submissionCode = new SubmissionDetailDTO();
        for (SubmissionDetailDTO submissionDetailDTO : Data.submissionCodeList) {
            if (submissionDetailDTO.getSourceCodeId().equals(codeId)) {
                submissionCode = submissionDetailDTO;
                break;
            }
        }
        model.addAttribute("submissionCode", submissionCode);
        return "/student/submission-code";
    }
}
