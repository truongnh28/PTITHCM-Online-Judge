package ptithcm.onlinejudge.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.ContestHasProblem;
import ptithcm.onlinejudge.model.request.ProblemRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.repository.ContestHasProblemRepository;
import ptithcm.onlinejudge.repository.ContestRepository;
import ptithcm.onlinejudge.repository.ProblemRepository;
import ptithcm.onlinejudge.repository.TeacherRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProblemManagementServiceImpl implements ProblemManagementService {
    @Autowired
    UploadFileService uploadFileService;
    @Autowired
    ProblemRepository problemRepository;
    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    ContestRepository contestRepository;
    @Autowired
    ContestHasProblemRepository contestHasProblemRepository;

    @Override
    public ResponseObject addProblem(ProblemRequest problemRequest, String filePath) {
        if (checkFile(problemRequest, filePath)) {
            return new ResponseObject(HttpStatus.FOUND, "Null data error", "");
        }
        if (problemRequestIsValid(problemRequest)) {
            return new ResponseObject(HttpStatus.FOUND, "Problem Found", "");
        }
        if (!Files.exists(Path.of(filePath))) {
            return new ResponseObject(HttpStatus.FOUND, "File is not exists", "");
        }
        ResponseObject responseUpload = uploadFileService.uploadFile(filePath);
        if (responseUpload.getStatus() == HttpStatus.FOUND) {
            return new ResponseObject(HttpStatus.FOUND, "Found upload", "");
        }
        if(problemRepository.existsById(problemRequest.getProblemId())) {
            return new ResponseObject(HttpStatus.FOUND, "Problem id is exist", "");
        }
        String problemId = problemRequest.getProblemId();
        String problemName = problemRequest.getProblemName();
        int score = problemRequest.getScore();
        ObjectMapper objectMapper = new ObjectMapper();
        Map uploadInfo = objectMapper.convertValue(responseUpload.getData(), Map.class);
        String problemCloudinaryId = uploadInfo.get("public_id").toString();
        String url = uploadInfo.get("url").toString();
        Optional<Teacher> teacher = teacherRepository.findById(problemRequest.getTeacherId());
        Problem problem = new Problem(problemId, problemName, problemCloudinaryId, url, score, (byte) 0, teacher.get());
        problemRepository.save(problem);
        return new ResponseObject(HttpStatus.OK, "Success", problem);
    }

    @Override
    public ResponseObject editProblem(ProblemRequest problemRequest, String filePath) {
        if (checkFile(problemRequest, filePath)) {
            return new ResponseObject(HttpStatus.FOUND, "Null data error", "");
        }
        if (problemRequestIsValid(problemRequest)) {
            return new ResponseObject(HttpStatus.FOUND, "Problem Found", "");
        }
        if (!Files.exists(Path.of(filePath))) {
            return new ResponseObject(HttpStatus.FOUND, "File is not exists", "");
        }
        ResponseObject responseUpload = uploadFileService.uploadFile(filePath);
        if (responseUpload.getStatus() == HttpStatus.FOUND) {
            return new ResponseObject(HttpStatus.FOUND, "Found upload", "");
        }
        if(!problemRepository.existsById(problemRequest.getProblemId())) {
            return new ResponseObject(HttpStatus.FOUND, "Problem id is not exist", "");
        }
        String problemId = problemRequest.getProblemId();
        String problemName = problemRequest.getProblemName();
        int score = problemRequest.getScore();
        ObjectMapper objectMapper = new ObjectMapper();
        Map uploadInfo = objectMapper.convertValue(responseUpload.getData(), Map.class);
        String problemCloudinaryId = uploadInfo.get("public_id").toString();
        String url = uploadInfo.get("url").toString();
        Optional<Problem> problem = problemRepository.findById(problemId);
        problem.get().setProblemName(problemName);
        problem.get().setProblemScore(score);
        problem.get().setProblemCloudinaryId(problemCloudinaryId);
        problem.get().setProblemUrl(url);
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
        if(!contestRepository.existsById(contestId)) {
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
        if(!teacherRepository.existsById(teacherId)) {
            return new ResponseObject(HttpStatus.FOUND, "Teacher is not exist", "");
        }
        List<Problem> problems = problemRepository.getProblemsByTeacher(teacherId);
        return new ResponseObject(HttpStatus.OK, "Success", problems);
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
