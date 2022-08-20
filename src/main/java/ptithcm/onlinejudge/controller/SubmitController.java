package ptithcm.onlinejudge.controller;

import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class SubmitController{
    @GetMapping("/submit/{problemId}")
    public String gotoSubmit(@PathVariable("problemId") String problemId, Model model) {
        ProblemDetailsDTO problemDetails = new ProblemDetailsDTO();
        for (ProblemDetailsDTO problemDetailsDTO: Data.problemDetailsList) {
            if (problemDetailsDTO.getId().equals(problemId)) {
                problemDetails = problemDetailsDTO;
                break;
            }
        }
        SubmitDTO submit = new SubmitDTO();
        model.addAttribute("problemDetails", problemDetails);
        model.addAttribute("submit", submit);
        return "submit";
    }

    private String randomId(int n) {
        String AlphaNumericString = "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    @PostMapping("/submit-code/{problemId}")
    public String submitCode(@PathVariable("problemId") String problemId, @ModelAttribute("submit") SubmitDTO submitDTO, HttpSession session) {
        SubmissionDTO submissionDTO = new SubmissionDTO();
        submissionDTO.setCodeId(randomId(10));
        submissionDTO.setProblemId(problemId);
        submissionDTO.setStatus("Accepted");
        submissionDTO.setTimeSubmit(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")));
        submissionDTO.setTimeExecute(15);
        submissionDTO.setLanguage(submitDTO.getLanguage());
        UserLogin userLogin = (UserLogin) session.getAttribute("user");
        submissionDTO.setUsername(userLogin.getUsername());
        SubmissionCodeDTO submissionCodeDTO = new SubmissionCodeDTO();
        submissionCodeDTO.setCodeId(submissionDTO.getCodeId());
        submissionCodeDTO.setStatus(submissionDTO.getStatus());
        submissionCodeDTO.setLanguage(submissionDTO.getLanguage());
        submissionCodeDTO.setSourceCode(submitDTO.getSourceCode());
        submissionCodeDTO.setTimeExec(submissionDTO.getTimeExecute());
        submissionCodeDTO.setUsername(submissionDTO.getUsername());
        submissionCodeDTO.setMemoryUsed(10);
        Data.submissionList.add(0, submissionDTO);
        Data.submissionCodeList.add(0, submissionCodeDTO);
        return "redirect:/submissions";
    }
}
