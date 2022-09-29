package ptithcm.onlinejudge.controller.frontend.student;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.model.adapter.GetStatusResponse;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.Submission;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.ContestManagementService;
import ptithcm.onlinejudge.services.ProblemManagementService;
import ptithcm.onlinejudge.services.StorageFileService;
import ptithcm.onlinejudge.services.SubmitService;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/contest/{contestId}/submit")
public class StudentSubmitController {
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestManagementService contestManagementService;
    @Autowired
    private SubmitService submitService;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemManagementService problemManagementService;
    @GetMapping("/{problemId}")
    public String showSubmitPage(@PathVariable("contestId") String contestId, @PathVariable("problemId") String problemId, Model model) {
        model.addAttribute("pageTitle", "Nộp bài");
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDetailDTO contest = contestMapper.entityToDetailDTO((Contest) getContestByIdResponse.getData());
        ResponseObject responseGetProblemById = problemManagementService.getProblemById(problemId);
        if (!responseGetProblemById.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ProblemDTO problemDetails = problemMapper.entityToDTO((Problem) responseGetProblemById.getData());
        model.addAttribute("problemDetails", problemDetails);
        model.addAttribute("contest", contest);
        return "/student/submit";
    }

    @PostMapping("/{problemId}")
    public String submitCode(@PathVariable("contestId") String contestId,
                             @PathVariable("problemId") String problemId,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam("language") String language,
                             HttpSession session) throws InterruptedException {
        String studentId = ((Login) session.getAttribute("user")).getUsername();
        ResponseObject getContestByIdResponse = contestManagementService.getContestById(contestId);
        if (!getContestByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ContestDetailDTO contest = contestMapper.entityToDetailDTO((Contest) getContestByIdResponse.getData());
        ResponseObject submitProblemResponse = submitService.submitProblemFromController(studentId, problemId, language, file);
        if (!submitProblemResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        Submission submission = (Submission) submitProblemResponse.getData();
        while(true) {
            Thread.sleep(200);
            ResponseObject getStatusResponse = submitService.getStatusAdapter(submission.getId());
            GetStatusResponse status = (GetStatusResponse) getStatusResponse.getData();
            if (!(status.getStatus().equals("queued") || status.getStatus().equals("judging"))) {
                break;
            }
        }
        return "redirect:/student/contest/{contestId}/submission/" + submission.getId();
    }
}
