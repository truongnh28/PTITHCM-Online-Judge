package ptithcm.onlinejudge.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.helper.FileHelper;
import ptithcm.onlinejudge.model.entity.*;
import ptithcm.onlinejudge.model.request.ProblemHasTypeRequest;
import ptithcm.onlinejudge.model.request.ProblemRequest;
import ptithcm.onlinejudge.model.request.TestCaseRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProblemManagementServiceImpl implements ProblemManagementService {
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private TestCaseRepository testCaseRepository;
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private LevelManagementService levelManagementService;
    @Autowired
    private ContestHasProblemRepository contestHasProblemRepository;

    @Override
    public ResponseObject addProblem(ProblemRequest problemRequest, String descriptionPath) {
        if (checkFile(problemRequest, descriptionPath)) {
            return new ResponseObject(HttpStatus.FOUND, "Null data error", "");
        }
        if (problemRequestIsValid(problemRequest)) {
            return new ResponseObject(HttpStatus.FOUND, "Problem Found", "");
        }
        if (!Files.exists(Path.of(descriptionPath))) {
            return new ResponseObject(HttpStatus.FOUND, "File is not exists", "");
        }
        ResponseObject responseUpload = uploadFileService.uploadFile(descriptionPath);
        if (responseUpload.getStatus() == HttpStatus.FOUND) {
            return new ResponseObject(HttpStatus.FOUND, "Found upload", "");
        }
        if (problemRepository.existsById(problemRequest.getProblemId())) {
            return new ResponseObject(HttpStatus.FOUND, "Problem id is exist", "");
        }
        String problemId = problemRequest.getProblemId();
        String problemName = problemRequest.getProblemName();
        int score = problemRequest.getScore();
        int timeLimit = problemRequest.getTimeLimit();
        int memoryLimit = problemRequest.getMemoryLimit();
        // level
        ResponseObject responseGetLevelById = levelManagementService.getLevelById((byte) problemRequest.getLevelId());
        if (!responseGetLevelById.getStatus().equals(HttpStatus.OK))
            return new ResponseObject(HttpStatus.FOUND, "Level id not exist", "");
        Level level = (Level) responseGetLevelById.getData();
        // file description
        ObjectMapper objectMapper = new ObjectMapper();
        Map uploadInfo = objectMapper.convertValue(responseUpload.getData(), Map.class);
        String problemCloudinaryId = uploadInfo.get("public_id").toString();
        String url = uploadInfo.get("url").toString();

        Optional<Teacher> teacher = teacherRepository.findById(problemRequest.getTeacherId());
        Problem problem = new Problem(problemId, problemName, problemCloudinaryId, url, score, timeLimit, memoryLimit, (byte) 0, teacher.get(), level);
        problem = problemRepository.save(problem);
        return new ResponseObject(HttpStatus.OK, "Success", problem);
    }

    @Override
    public ResponseObject editProblem(ProblemRequest problemRequest, String descriptionPath) {
        if (checkFile(problemRequest, descriptionPath)) {
            return new ResponseObject(HttpStatus.FOUND, "Null data error", "");
        }
        if (problemRequestIsValid(problemRequest)) {
            return new ResponseObject(HttpStatus.FOUND, "Problem Found", "");
        }
        if (!Files.exists(Path.of(descriptionPath))) {
            return new ResponseObject(HttpStatus.FOUND, "File is not exists", "");
        }
        ResponseObject responseUpload = uploadFileService.uploadFile(descriptionPath);
        if (responseUpload.getStatus() == HttpStatus.FOUND) {
            return new ResponseObject(HttpStatus.FOUND, "Found upload", "");
        }
        if (!problemRepository.existsById(problemRequest.getProblemId())) {
            return new ResponseObject(HttpStatus.FOUND, "Problem id is not exist", "");
        }
        String problemId = problemRequest.getProblemId();
        String problemName = problemRequest.getProblemName();
        int score = problemRequest.getScore();
        int timeLimit = problemRequest.getTimeLimit();
        int memoryLimit = problemRequest.getMemoryLimit();
        ObjectMapper objectMapper = new ObjectMapper();
        Map uploadInfo = objectMapper.convertValue(responseUpload.getData(), Map.class);
        String problemCloudinaryId = uploadInfo.get("public_id").toString();
        String url = uploadInfo.get("url").toString();
        Optional<Problem> problem = problemRepository.findById(problemId);
        problem.get().setProblemName(problemName);
        problem.get().setProblemScore(score);
        problem.get().setProblemCloudinaryId(problemCloudinaryId);
        problem.get().setProblemUrl(url);
        problem.get().setProblemTimeLimit(timeLimit);
        problem.get().setProblemMemoryLimit(memoryLimit);
        ResponseObject responseDelete = uploadFileService.deleteFile(problemCloudinaryId);
        if (responseDelete.getStatus() == HttpStatus.FOUND) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
        problemRepository.save(problem.get());
        return new ResponseObject(HttpStatus.OK, "Success", problem);
    }

    @Override
    public ResponseObject deleteProblem(String problemId) {
        if (!problemRepository.existsById(problemId)) {
            return new ResponseObject(HttpStatus.FOUND, "Problem is not exists", "");
        }
        Optional<Problem> problem = problemRepository.findById(problemId);
        ResponseObject responseDelete = uploadFileService.deleteFile(problem.get().getProblemCloudinaryId());
        if (responseDelete.getStatus() == HttpStatus.FOUND) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
        problemRepository.deleteById(problemId);
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }

    @Override
    public ResponseObject addProblemToContest(String problemId, String contestId) {
        if (!problemRepository.existsById(problemId)) {
            return new ResponseObject(HttpStatus.FOUND, "Problem is not exist", "");
        }
        if (!contestRepository.existsById(contestId)) {
            return new ResponseObject(HttpStatus.FOUND, "Contest is not exist", "");
        }
        Optional<Problem> problem = problemRepository.findById(problemId);
        Optional<Contest> contest = contestRepository.findById(contestId);
        ContestHasProblem contestHasProblem = new ContestHasProblem();
        contestHasProblem.setContest(contest.get());
        contestHasProblem.setProblem(problem.get());
        contestHasProblemRepository.save(contestHasProblem);
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }

    @Override
    public ResponseObject getAllProblemCreateByTeacher(String teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            return new ResponseObject(HttpStatus.FOUND, "Teacher is not exist", "");
        }
        List<Problem> problems = problemRepository.getProblemsByTeacher(teacherId);
        return new ResponseObject(HttpStatus.OK, "Success", problems);
    }

    @Override
    public ResponseObject getProblemById(String problemId) {
        Optional<Problem> foundProblem = problemRepository.findById(problemId);
        if (foundProblem.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Problem is not exist", "");
        return new ResponseObject(HttpStatus.OK, "Success", foundProblem.get());
    }

    private boolean problemRequestIsValid(ProblemRequest problem) {
        boolean problemName = problem.getProblemName().length() > 0;
        boolean score = problem.getScore() > 0 && problem.getScore() <= 10;
        return problemName && score;
    }

    private boolean checkFile(ProblemRequest problemRequest, String filePath) {
        if (filePath == null) return true;
        return problemRequest == null;
    }
}
