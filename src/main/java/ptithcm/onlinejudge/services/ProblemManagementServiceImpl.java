package ptithcm.onlinejudge.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.dto.ProblemDTO;
import ptithcm.onlinejudge.dto.ProblemShowDTO;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.model.entity.*;
import ptithcm.onlinejudge.model.request.MultipleProblemTypeRequest;
import ptithcm.onlinejudge.model.request.ProblemRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProblemManagementServiceImpl implements ProblemManagementService {
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StorageFileService storageFileService;
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private LevelManagementService levelManagementService;
    @Autowired
    private ProblemHasTypeManagementService problemHasTypeManagementService;
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
    public ResponseObject addProblemWithTestCasesAndTypes(ProblemDTO problemDTO, String teacherId, int levelId, MultipartFile description, MultipartFile[] inputs, MultipartFile[] outputs, String[] typeIds) {
        Arrays.sort(inputs);
        Arrays.sort(outputs);
        if (!checkInputsAndOutputs(inputs, outputs))
            return new ResponseObject(HttpStatus.FOUND, "Input test cases and Output test cases not valid", "");

        ProblemRequest problemRequest = new ProblemRequest();
        problemRequest.setProblemId(problemDTO.getProblemId());
        problemRequest.setScore(problemDTO.getProblemScore());
        problemRequest.setProblemName(problemDTO.getProblemName());
        problemRequest.setTimeLimit(problemDTO.getProblemTimeLimit());
        problemRequest.setMemoryLimit(problemDTO.getProblemMemoryLimit());
        problemRequest.setLevelId(levelId);
        problemRequest.setTeacherId(teacherId);

        String pathDescription = storageFileService.storeFile(description);
        ResponseObject addProblemWithLevelAndTeacherResponse = addProblem(problemRequest, pathDescription);
        if (!addProblemWithLevelAndTeacherResponse.getStatus().equals(HttpStatus.OK))
            return new ResponseObject(HttpStatus.FOUND, "Add problem failed", "");

        Problem problem = (Problem) addProblemWithLevelAndTeacherResponse.getData();
        ResponseObject addProblemTypesResponse = problemHasTypeManagementService.addMultipleProblemType(new MultipleProblemTypeRequest(typeIds, problem));
        if (!addProblemTypesResponse.getStatus().equals(HttpStatus.OK))
            return new ResponseObject(HttpStatus.FOUND, "Add problem types failed", "");


        return new ResponseObject(HttpStatus.OK, "Success", "");
    }

    @Override
    public ResponseObject getAllProblems() {
        List<Problem> problems = problemRepository.findAll();
        return new ResponseObject(HttpStatus.OK, "Success", problems);
    }

    @Override
    public ResponseObject getAllProblemsCreateByTeacher(String teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            return new ResponseObject(HttpStatus.FOUND, "Teacher is not exist", "");
        }
        List<Problem> problems = problemRepository.getProblemsByTeacher(teacherId);
        return new ResponseObject(HttpStatus.OK, "Success", problems);
    }

    @Override
    public ResponseObject getAllProblemsForAddingOrRemovingContest(String contestId) {
        if (!contestRepository.existsById(contestId))
            return new ResponseObject(HttpStatus.FOUND, "Contest not exist", "");
        List<Problem> problems = problemRepository.findAll();
        List<ProblemShowDTO> problemShows = problems.stream().map(problem -> {
            ProblemShowDTO problemShowDTO = problemMapper.entityToProblemShowDTO(problem);
            problemShowDTO.setDisabledButtonAdding(contestHasProblemRepository.existsById(new ContestHasProblemId(contestId, problem.getId())));
            return problemShowDTO;
        }).toList();
        return new ResponseObject(HttpStatus.OK, "Success", problemShows);
    }

    @Override
    public ResponseObject getAllProblemsOfContest(String contestId) {
        if (!contestRepository.existsById(contestId))
            return new ResponseObject(HttpStatus.FOUND, "Contest is not exist", "");
        List<Problem> problems = problemRepository.getProblemsByContestId(contestId);
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

    private boolean isNotUploaded(MultipartFile file) {
        return (file == null || file.isEmpty());
    }

    private boolean checkInputsAndOutputs(MultipartFile[] inputs, MultipartFile[] outputs) {
        if (inputs.length != outputs.length)
            return false;
        if (inputs.length == 1) {
            if (isNotUploaded(inputs[0]) && isNotUploaded(outputs[0]))
                return true;
            if (isNotUploaded(inputs[0]) || isNotUploaded(outputs[0]))
                return false;
        }
        for (int i = 0; i < inputs.length; ++i) {
            String fullInputName = inputs[i].getOriginalFilename();
            String fullOutputName = outputs[i].getOriginalFilename();
            String inputName = StringUtils.cleanPath(fullInputName);
            String outputName = StringUtils.cleanPath(fullOutputName);
            if (!inputName.equals(outputName))
                return false;
            String inputExtension = FilenameUtils.getExtension(fullInputName);
            String outputExtension = FilenameUtils.getExtension(fullOutputName);
            if (!inputExtension.equals("in") || !outputExtension.equals("out"))
                return false;
        }
        return true;
    }
}
