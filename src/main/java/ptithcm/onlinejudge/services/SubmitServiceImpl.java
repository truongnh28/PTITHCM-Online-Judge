package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.helper.FileHelper;
import ptithcm.onlinejudge.helper.TimeHelper;
import ptithcm.onlinejudge.model.adapter.*;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.entity.Submission;
import ptithcm.onlinejudge.model.request.SubmitRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.ContestRepository;
import ptithcm.onlinejudge.repository.ProblemRepository;
import ptithcm.onlinejudge.repository.StudentRepository;
import ptithcm.onlinejudge.repository.SubmissionRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class SubmitServiceImpl implements SubmitService {
    public static final String folder = "./uploads/source/";
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private ContestRepository contestRepository;
    @Override
    public ResponseObject getProblemListAdapter() {
        final String url = "http://localhost:80/api/get_problem_list";
        try {
            RestTemplate restTemplate = new RestTemplate();
            GetProblemListResponse result = restTemplate.getForObject(url, GetProblemListResponse.class);
            return new ResponseObject(HttpStatus.OK, "Success", result);
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }

    @Override
    public ResponseObject getProblemInfoAdapter(String problemId) {
        final String url = "http://localhost:80/api/get_problem_info/" + problemId;
        try {
            RestTemplate restTemplate = new RestTemplate();
            GetProblemInfoResponse result = restTemplate.getForObject(url, GetProblemInfoResponse.class);
            return new ResponseObject(HttpStatus.OK, "Success", result);
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }

    @Override
    public ResponseObject getStatusAdapter(String jobId) {
        final String url = "http://localhost:80/api/get_status/" + jobId;
        try {
            RestTemplate restTemplate = new RestTemplate();
            GetStatusResponse result = restTemplate.getForObject(url, GetStatusResponse.class);
            return new ResponseObject(HttpStatus.OK, "Success", result);
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }

    @Override
    public ResponseObject getSubmissionSourceAdapter(String jobId) {
        final String url = "http://localhost:80/api/get_submission_source/" + jobId;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            return new ResponseObject(HttpStatus.OK, "Success", result);
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }

    @Override
    public ResponseObject getSubmissionsAdapter(int pageNumber, String key) {
        final String url = "http://localhost:80/api/get_submissions/" + pageNumber + "?secret_key=" + key;
        try {
            RestTemplate restTemplate = new RestTemplate();
            GetSubmissionsResponse result = restTemplate.getForObject(url, GetSubmissionsResponse.class);
            return new ResponseObject(HttpStatus.OK, "Success", result);
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }

    @Override
    public ResponseObject submitProblemAdapter(SubmitRequest submitRequest) {
        final String url = "http://localhost:80/api/submit";
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
            multipartBodyBuilder.part("problem_id", submitRequest.getProblemId());
            multipartBodyBuilder.part("username", submitRequest.getUserId());
            multipartBodyBuilder.part("type", submitRequest.getType());
            multipartBodyBuilder.part("secret_key", submitRequest.getKey());
            Resource code = new FileSystemResource(submitRequest.getPath());
            multipartBodyBuilder.part("code", code, MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();
            HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody, headers);
            SubmitResponse result = restTemplate.postForEntity(url, httpEntity, SubmitResponse.class).getBody();
            return new ResponseObject(HttpStatus.OK, "Success", result);
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }

    @Override
    public ResponseObject submitProblemFromController(String studentId, String problemId, String contestId, String language, MultipartFile file) {
        if (file == null)
            return new ResponseObject(HttpStatus.FOUND, "File not found", null);
        Contest contestFound = contestRepository.findById(contestId).get();
        if (Instant.now().isAfter(contestFound.getContestEnd())) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Không submit được", null);
        }
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null)
            return new ResponseObject(HttpStatus.FOUND, "File upload not valid", null);

        String uploadFileName = StringUtils.cleanPath(originalFileName);
        String extensionFile = FileHelper.getFileExtensionFromPath(uploadFileName);
        // upload to server
        String fileBaseName = UUID.randomUUID().toString().replace("-", "");
        String fullPath = folder + fileBaseName + "." + extensionFile;
        File sourceCodeFolder = new File(folder);
        if (!sourceCodeFolder.exists()) {
            if (!sourceCodeFolder.mkdirs())
                return new ResponseObject(HttpStatus.FOUND, "Make folder source code failed", null);
        }
        try {
            Path pathSourceCode = Paths.get(fullPath);
            Files.copy(file.getInputStream(), pathSourceCode, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Optional<Problem> foundProblem = problemRepository.findById(problemId);
        if (foundProblem.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy bài tập", null);

        Problem problem = foundProblem.get();

        Optional<Student> foundStudent = studentRepository.findById(studentId);
        if (foundStudent.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy sinh viên", null);

        Student student = foundStudent.get();

        Optional<Contest> foundContest = contestRepository.findById(contestId);
        if (foundContest.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy bài thực hành", null);

        Contest contest = foundContest.get();

        SubmitRequest submitRequest = new SubmitRequest();
        submitRequest.setProblemId(problemId);
        submitRequest.setType(language);
        submitRequest.setKey("default_change_this");
        submitRequest.setUserId(studentId);
        submitRequest.setPath(fullPath);
        ResponseObject responseObject = submitProblemAdapter(submitRequest);
        if (!responseObject.getStatus().equals(HttpStatus.OK))
            return new ResponseObject(HttpStatus.FOUND, "Failed to submit code", null);

        SubmitResponse submitResponse = (SubmitResponse) responseObject.getData();
        Submission submission = new Submission();
        submission.setId(submitResponse.getJobId());
        submission.setProblem(problem);
        submission.setStudent(student);
        submission.setVerdict((byte) 0); // PENDING
        submission.setSubmissionTime(Instant.now());
        submission.setSubmissionScore(0);
        submission.setContest(contest);
        submission.setSubmissionSourcePath(fullPath);
        submission = submissionRepository.save(submission);

        return new ResponseObject(HttpStatus.OK, "Success", submission);
    }
}
