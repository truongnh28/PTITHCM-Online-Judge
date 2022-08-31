package ptithcm.onlinejudge.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.ProblemRequest;
import ptithcm.onlinejudge.model.ResponseObject;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.repository.ContestRepository;
import ptithcm.onlinejudge.repository.ProblemRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@Service
public class ProblemManagementServiceImpl implements ProblemManagementService{
    @Autowired
    UploadFileService uploadFileService;
    @Autowired
    ProblemRepository problemRepository;
    @Autowired
    ContestRepository contestRepository;
    @Override
    public ResponseObject addProblem(ProblemRequest problemRequest, String filePath) {
        if (filePath == null || problemRequest == null) {
            return new ResponseObject(HttpStatus.FOUND, "Null data error", "");
        }
        if (checkProblemIsValid(problemRequest)) {
            return new ResponseObject(HttpStatus.FOUND, "Problem Found", "");
        }
        if(!Files.exists(Path.of(filePath))) {
            return new ResponseObject(HttpStatus.FOUND, "File is not exists", "");
        }
        ResponseObject responseUpload = uploadFileService.uploadFile(filePath);
        if(responseUpload.getStatus() == HttpStatus.FOUND) {
            return new ResponseObject(HttpStatus.FOUND, "Found upload", "");
        }
        String problemId = problemRequest.getProblemId();
        String problemName = problemRequest.getProblemName();
        int score = problemRequest.getScore();
        ObjectMapper objectMapper = new ObjectMapper();
        Map uploadInfo = objectMapper.convertValue(responseUpload.getData(), Map.class);
        String problemCloudinaryId = uploadInfo.get("public_id").toString();
        String url = uploadInfo.get("url").toString();
        Problem problem = new Problem(problemId, problemName, score, null, problemCloudinaryId, url);
        problemRepository.save(problem);
        return new ResponseObject(HttpStatus.OK, "Success", problem);
    }

    @Override
    public ResponseObject editProblem(ProblemRequest problemRequest, String filePath) {
        if (filePath == null || problemRequest == null) {
            return new ResponseObject(HttpStatus.FOUND, "Null data error", "");
        }
        if (checkProblemIsValid(problemRequest)) {
            return new ResponseObject(HttpStatus.FOUND, "Problem Found", "");
        }
        if(!Files.exists(Path.of(filePath))) {
            return new ResponseObject(HttpStatus.FOUND, "File is not exists", "");
        }
        ResponseObject responseUpload = uploadFileService.uploadFile(filePath);
        if(responseUpload.getStatus() == HttpStatus.FOUND) {
            return new ResponseObject(HttpStatus.FOUND, "Found upload", "");
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
        problem.get().setScore(score);
        problem.get().setProblemCloudinaryId(problemCloudinaryId);
        problem.get().setProblemUrl(url);
        ResponseObject responseDelete = uploadFileService.deleteFile(problemCloudinaryId);
        if(responseDelete.getStatus() == HttpStatus.FOUND) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
        problemRepository.save(problem.get());
        return new ResponseObject(HttpStatus.OK, "Success", problem);
    }

    @Override
    public ResponseObject deleteProblem(String problemId) {
        if(!problemRepository.existsById(problemId)) {
            return new ResponseObject(HttpStatus.FOUND, "Problem is not exists", "");
        }
        Optional<Problem> problem = problemRepository.findById(problemId);
        ResponseObject responseDelete = uploadFileService.deleteFile(problem.get().getProblemCloudinaryId());
        if(responseDelete.getStatus() == HttpStatus.FOUND) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
        problemRepository.deleteById(problemId);
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }
    private boolean checkProblemIsValid(ProblemRequest problem) {
        boolean problemId = problemRepository.existsById(problem.getProblemId());
        boolean problemName = problem.getProblemName().length() > 0;
        boolean score = problem.getScore() > 0 && problem.getScore() <= 10;
        return problemId && problemName && score;
    }
}
