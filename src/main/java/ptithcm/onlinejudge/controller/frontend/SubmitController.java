package ptithcm.onlinejudge.controller.frontend;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.services.StorageService;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Controller
public class SubmitController{
    @Autowired
    private StorageService storageService;
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

    @PostMapping("/submit-code/{problemId}")
    public String submitCode(@PathVariable("problemId") String problemId, @RequestParam("file") MultipartFile multipartFile, @ModelAttribute("submit") SubmitDTO submitDTO, HttpSession session) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            // lấy source code
            String sourceCode = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
            // lưu file có đường dẫn là /uploads/a.cpp
            String filePath = storageService.storeFile(multipartFile);
            // gọi api
            Unirest.setTimeouts(0, 0);
            UserLogin userLogin = (UserLogin) session.getAttribute("user");
            File file = new File(filePath);
            HttpResponse<String> response = Unirest.post("http://localhost:80/api/submit")
                    .field("problem_id", problemId)
                    .field("username ", userLogin.getUsername())
                    .field("type", submitDTO.getLanguage())
                    .field("file", new File(filePath))
                    .field("secret_key", "default_change_this")
                    .asString();
            // hoàn thành gọi api xong thì xóa luôn file đó
            if (!file.delete())
                throw new RuntimeException("Không thể xóa file tạm được");
            JSONParser jsonParser = new JSONParser();
            try {
                // parse từ string sang đối tượng json
                JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
                // lấy status, nếu có thì lấy giá trị của nó, không thì giá trị mặc định là error
                String status = jsonObject.optString("status", "error");
                // nếu là error thì throw lỗi
                if (!status.equals("success")) {
                    throw new RuntimeException("Có vấn đề khi chấm bài");
                }
                // lấy mã code được sinh ra
                String jobId = jsonObject.getString("job_id");
                // set các dữ liệu cho submission và submissioncode
                SubmissionDTO submissionDTO = new SubmissionDTO();
                submissionDTO.setCodeId(jobId);
                submissionDTO.setProblemId(problemId);
                submissionDTO.setStatus("Accepted");
                submissionDTO.setTimeSubmit(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")));
                submissionDTO.setTimeExecute(15); // cái này là giả
                submissionDTO.setLanguage(submitDTO.getLanguage());
                submissionDTO.setUsername(userLogin.getUsername());
                SubmissionCodeDTO submissionCodeDTO = new SubmissionCodeDTO();
                submissionCodeDTO.setCodeId(submissionDTO.getCodeId());
                submissionCodeDTO.setStatus(submissionDTO.getStatus());
                submissionCodeDTO.setLanguage(submissionDTO.getLanguage());
                submissionCodeDTO.setSourceCode(sourceCode);
                submissionCodeDTO.setTimeExec(submissionDTO.getTimeExecute());
                submissionCodeDTO.setUsername(submissionDTO.getUsername());
                submissionCodeDTO.setMemoryUsed(10); // cái này cũng là giả
                Data.submissionList.add(0, submissionDTO);
                Data.submissionCodeList.add(0, submissionCodeDTO);
                return "redirect:/submissions";
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException | UnirestException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/submissions";
    }
}
