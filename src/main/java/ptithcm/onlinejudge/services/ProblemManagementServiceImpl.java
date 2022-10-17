package ptithcm.onlinejudge.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.dto.ProblemDTO;
import ptithcm.onlinejudge.dto.ProblemShowDTO;
import ptithcm.onlinejudge.helper.FileHelper;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.model.entity.*;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.yaml.Info;
import ptithcm.onlinejudge.model.yaml.ProblemYaml;
import ptithcm.onlinejudge.model.yaml.Subtask;
import ptithcm.onlinejudge.repository.*;

import java.io.File;
import java.time.Instant;
import java.util.*;

@Service
public class ProblemManagementServiceImpl implements ProblemManagementService {
    static final private String problemInfoPath = System.getProperty("user.dir") + "/problem_info";
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
    private AddProblemToYaml addProblemToYaml;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private ProblemTypeRepository problemTypeRepository;
    @Autowired
    private ProblemHasTypeRepository problemHasTypeRepository;
    @Autowired
    private ContestHasProblemRepository contestHasProblemRepository;

    @Override
    public ResponseObject addProblem(ProblemDTO problemDTO, String teacherId, int levelId, MultipartFile description, MultipartFile[] inputs, MultipartFile[] outputs, String[] types) {
        Map<String, String> errors = addErrors(problemDTO, teacherId, levelId, description, inputs, outputs, types);
        if (!errors.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Có lỗi", errors);
        // store to spring boot server
        String descriptionPath = storageFileService.storeFile(description);
        // upload to cloudinary
        ResponseObject responseUpload = uploadFileService.uploadFile(descriptionPath);
        if (!responseUpload.getStatus().equals(HttpStatus.OK)) {
            errors.put("server", "Không thể upload file được");
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Có lỗi", errors);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map uploadInfo = objectMapper.convertValue(responseUpload.getData(), Map.class);
        String problemCloudinaryId = uploadInfo.get("public_id").toString();
        String url = uploadInfo.get("url").toString();

        File file = new File(descriptionPath);
        if (!file.delete()) {
            errors.put("server", "Lỗi server");
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Có lỗi", errors);
        }

        Optional<Level> foundLevel = levelRepository.findById((byte) levelId);
        Level level = foundLevel.get();

        Optional<Teacher> foundTeacher = teacherRepository.findById(teacherId);
        Teacher teacher = foundTeacher.get();

        Problem problem = problemMapper.dtoToEntity(problemDTO);
        problem.setProblemCloudinaryId(problemCloudinaryId);
        problem.setProblemUrl(url);
        problem.setCreateAt(Instant.now());
        problem.setUpdateAt(Instant.now());
        problem.setHide((byte) 0);
        problem.setLevel(level);
        problem.setTeacher(teacher);
        problem = problemRepository.save(problem);

        String problemId = problem.getId();
        String problemName = problem.getProblemName();

        for (String typeId : types) {
            Optional<ProblemType> foundType = problemTypeRepository.findById(typeId);
            if (foundType.isEmpty()) {
                errors.put("server", "Không thể tìm thấy loại bài tập");
                return new ResponseObject(HttpStatus.BAD_REQUEST, "Có lỗi", errors);
            }
            ProblemType problemType = foundType.get();
            ProblemHasType problemHasType = new ProblemHasType();
            ProblemHasTypeId id = new ProblemHasTypeId(problemId, problemType.getId());
            problemHasType.setId(id);
            problemHasType.setProblem(problem);
            problemHasType.setProblemType(problemType);
            problemHasTypeRepository.save(problemHasType);
        }

        ProblemYaml problemYaml = new ProblemYaml();
        problemYaml.setId(problemId);
        problemYaml.setName(problemName);
        problemYaml.setStatus("up");

        ResponseObject responseAddProblemToYaml = addProblemToYaml.addProblemToYaml(problemYaml);
        if (!responseAddProblemToYaml.getStatus().equals(HttpStatus.OK)) {
            errors.put("server", "Thêm bài tập thất bại");
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Thêm bài tập thất bại", errors);
        }

        Info info = new Info();
        info.setProblemId(problem.getId());
        info.setProblemName(problem.getProblemName());
        info.setTimeLimit(problem.getProblemTimeLimit());
        info.setMemoryLimit(problem.getProblemMemoryLimit());
        info.setMaxScore(problem.getProblemScore());
        info.setScoringMethod("minimum");
        info.setChecker("diff");

        List<Subtask> subtasks = new ArrayList<>();
        Subtask subtask = new Subtask();
        subtask.setName("main");
        subtask.setScore(problem.getProblemScore());
        subtask.setNumSamples(inputs.length);
        if (inputs.length == 1 && !isUploaded(inputs[0]) && !isUploaded(outputs[0]))
            subtask.setNumSamples(0);
        subtasks.add(subtask);

        info.setSubtasks(subtasks);
        ResponseObject addProblemToDirResponse = addProblemToYaml.addProblemToDir(info, inputs, outputs);
        if (!addProblemToDirResponse.getStatus().equals(HttpStatus.OK)) {
            errors.put("server", "Thêm bài tập thất bại");
            return new ResponseObject(HttpStatus.FOUND, "Thêm bài tập thất bại", errors);
        }
        return new ResponseObject(HttpStatus.OK, "Success", problem);
    }

    @Override
    public ResponseObject editProblem(ProblemDTO problemDTO, String teacherId, int levelId, MultipartFile description, String[] types) {
        String problemId = problemDTO.getProblemId();
        Map<String, String> errors = editErrors(problemDTO, teacherId, levelId, types);
        if (!errors.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Có lỗi", errors);
        Optional<Problem> foundProblem = problemRepository.findById(problemId);
        if (foundProblem.isEmpty()) {
            errors.put("server", "Không tìm thấy bài tập");
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Có lỗi", errors);
        }
        Problem editedProblem = foundProblem.get();
        editedProblem.setProblemName(problemDTO.getProblemName());
        editedProblem.setProblemTimeLimit(problemDTO.getProblemTimeLimit());
        editedProblem.setProblemMemoryLimit(problemDTO.getProblemMemoryLimit());
        editedProblem.setProblemScore(problemDTO.getProblemScore());
        if (description != null && !description.isEmpty()) {
            // upload to server spring boot
            String descriptionPath = storageFileService.storeFile(description);
            // upload to cloudinary
            ResponseObject responseUpload = uploadFileService.uploadFile(descriptionPath);
            if (!responseUpload.getStatus().equals(HttpStatus.OK)) {
                errors.put("server", "Không thể upload file được");
                return new ResponseObject(HttpStatus.BAD_REQUEST, "Có lỗi", errors);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Map uploadInfo = objectMapper.convertValue(responseUpload.getData(), Map.class);
            String problemCloudinaryId = uploadInfo.get("public_id").toString();
            String url = uploadInfo.get("url").toString();
            editedProblem.setProblemUrl(url);
            editedProblem.setProblemCloudinaryId(problemCloudinaryId);
        }

        Optional<Level> foundLevel = levelRepository.findById((byte) levelId);
        if (foundLevel.isEmpty()) {
            errors.put("server", "Không thể tìm thấy mức độ");
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Có lỗi", errors);
        }
        Level level = foundLevel.get();

        Optional<Teacher> foundTeacher = teacherRepository.findById(teacherId);
        if (foundTeacher.isEmpty()) {
            errors.put("server", "Không thể tìm thấy giáo viên");
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Có lỗi", errors);
        }
        Teacher teacher = foundTeacher.get();

        editedProblem.setLevel(level);
        editedProblem.setUpdateAt(Instant.now());
        editedProblem.setTeacher(teacher);
        editedProblem = problemRepository.save(editedProblem);

        problemHasTypeRepository.deleteByProblem(problemId);
        for (String typeId: types) {
            Optional<ProblemType> foundType = problemTypeRepository.findById(typeId);
            if (foundType.isEmpty()) {
                errors.put("server", "Không tìm thấy loại bài tập");
                return new ResponseObject(HttpStatus.FOUND, "Có lỗi", errors);
            }
            ProblemType problemType = foundType.get();
            ProblemHasType problemHasType = new ProblemHasType();
            ProblemHasTypeId id = new ProblemHasTypeId(problemId, typeId);
            problemHasType.setId(id);
            problemHasType.setProblem(editedProblem);
            problemHasType.setProblemType(problemType);
            problemHasTypeRepository.save(problemHasType);
        }

        ProblemYaml problemYaml = new ProblemYaml();
        problemYaml.setId(problemId);
        problemYaml.setName(editedProblem.getProblemName());
        problemYaml.setStatus("up");

        ResponseObject addProblemToYamlResponse = addProblemToYaml.addProblemToYaml(problemYaml);
        if (!addProblemToYamlResponse.getStatus().equals(HttpStatus.OK)) {
            errors.put("server", "Thêm bài tập thất bại");
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Có lỗi", errors);
        }

        Info info = addProblemToYaml.getProblemInfo(problemInfoPath + "/" + problemId + "/info.yml");
        if (info == null) {
            errors.put("server", "Không tìm thấy bài tập trong hệ thống");
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Có lỗi", errors);
        }
        info.setProblemName(editedProblem.getProblemName());
        info.setTimeLimit(editedProblem.getProblemTimeLimit());
        info.setMemoryLimit(editedProblem.getProblemMemoryLimit());
        info.setMaxScore(editedProblem.getProblemScore());
        ResponseObject addProblemInfoResponse = addProblemToYaml.addProblemInfo(info, problemInfoPath + "/" + problemId + "/info.yml");
        if (!addProblemInfoResponse.getStatus().equals(HttpStatus.OK)) {
            errors.put("server", "Không cập nhật vào hệ thống được");
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Có lỗi", errors);
        }

        return new ResponseObject(HttpStatus.OK, "Success", editedProblem);
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
    public ResponseObject getAllProblemsActive() {
        List<Problem> problems = problemRepository.getProblemsActive();
        return new ResponseObject(HttpStatus.OK, "Success", problems);
    }

    @Override
    public ResponseObject getAllProblemsActiveNotInContest(String contestId) {
        if (!contestRepository.existsById(contestId))
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy bài thực hành", null);
        return new ResponseObject(HttpStatus.OK, "Success", problemRepository.getProblemsActiveNotInContest(contestId));
    }

    @Override
    public ResponseObject searchAllProblemsActiveNotInContest(String contestId, String keyword) {
        if (!contestRepository.existsById(contestId))
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy bài thực hành", null);
        return new ResponseObject(HttpStatus.OK, "Success", problemRepository.searchProblemsActiveNotInContest(contestId, keyword));
    }

    @Override
    public ResponseObject getAllProblemsCreateByTeacher(String teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            return new ResponseObject(HttpStatus.FOUND, "Teacher is not exist", "");
        }
        List<Problem> problems = problemRepository.getAllProblemsByTeacher(teacherId);
        return new ResponseObject(HttpStatus.OK, "Success", problems);
    }

    @Override
    public ResponseObject searchAllProblemsCreateByTeacher(String teacherId, String keyword) {
        if (!teacherRepository.existsById(teacherId))
            return new ResponseObject(HttpStatus.FOUND, "Giáo viên không tồn tại", null);
        List<Problem> problems = problemRepository.searchAllProblemsByTeacher(teacherId, keyword);
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
    public ResponseObject getAllProblemsActiveOfContest(String contestId) {
        if (!contestRepository.existsById(contestId))
            return new ResponseObject(HttpStatus.FOUND, "Bài thực hành không tồn tại", null);
        List<Problem> problems = problemRepository.getProblemsActiveOfContest(contestId);
        return new ResponseObject(HttpStatus.OK, "Success", problems);
    }

    @Override
    public ResponseObject getProblemById(String problemId) {
        Optional<Problem> foundProblem = problemRepository.findById(problemId);
        if (foundProblem.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Problem is not exist", "");
        return new ResponseObject(HttpStatus.OK, "Success", foundProblem.get());
    }

    @Override
    public ResponseObject lockProblem(String problemId) {
        Optional<Problem> foundProblem = problemRepository.findById(problemId);
        if (foundProblem.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Bài tập không tồn tại", null);
        Problem problem = foundProblem.get();
        problem.setHide((byte) 1);
        problem.setUpdateAt(Instant.now());
        problem = problemRepository.save(problem);
        return new ResponseObject(HttpStatus.OK, "Success", problem);
    }

    @Override
    public ResponseObject unlockProblem(String problemId) {
        Optional<Problem> foundProblem = problemRepository.findById(problemId);
        if (foundProblem.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Bài tập không tồn tại", null);
        Problem problem = foundProblem.get();
        problem.setHide((byte) 0);
        problem.setUpdateAt(Instant.now());
        problem = problemRepository.save(problem);
        return new ResponseObject(HttpStatus.OK, "Success", problem);
    }

    private Map<String, String> addErrors(ProblemDTO problemDTO, String teacherId, int levelId, MultipartFile description, MultipartFile[] inputs, MultipartFile[] outputs, String[] types) {
        Map<String, String> ret = new HashMap<>();
        String id = problemDTO.getProblemId();
        String name = problemDTO.getProblemName();
        int timeLimit = problemDTO.getProblemTimeLimit();
        int memoryLimit = problemDTO.getProblemMemoryLimit();
        int score = problemDTO.getProblemScore();
        if (id == null || id.isEmpty() || id.length() > 100)
            ret.put("problemId", "Mã bài tập không được trống và không quá 100 ký tự");
        if (name == null || name.isEmpty() || name.length() > 100)
            ret.put("problemName", "Tên bài tập không được trống và không quá 100 ký tự");
        if (timeLimit == 0 || timeLimit > 1000)
            ret.put("problemTimeLimit", "Giới hạn thời gian không được 0s và không quá 1000s");
        if (memoryLimit == 0 || memoryLimit > 1000)
            ret.put("problemMemoryLimit", "Giới hạn bộ nhớ không được 0MB và không quá 1000MB");
        if (score < 10 || score > 1000)
            ret.put("problemScore", "Điểm bài tập nằm trong đoạn 10 đến 1000");
        if (description == null || description.isEmpty())
            ret.put("problemDescription", "File mô tả không được upload");
        if (types.length == 0)
            ret.put("problemTypes", "Vui lòng chọn ít nhất một loại bài tập");
        if (id != null && !id.isEmpty() && id.length() <= 100 && problemRepository.existsById(id))
            ret.put("problemId", "Mã bài tập đã tồn tại, vui lòng chọn mã bài tập khác");
        if (!inputsOutputsValid(inputs, outputs))
            ret.put("filesUpload", "Các file upload không hợp lệ");
        if (!levelRepository.existsById((byte) levelId))
            ret.put("level", "Không tìm thấy mức độ");
        if (!teacherRepository.existsById(teacherId))
            ret.put("server", "Không tìm thấy giáo viên");
        return ret;
    }

    private Map<String, String> editErrors(ProblemDTO problemDTO, String teacherId, int levelId, String[] types) {
        Map<String, String> ret = new HashMap<>();
        String name = problemDTO.getProblemName();
        int timeLimit = problemDTO.getProblemTimeLimit();
        int memoryLimit = problemDTO.getProblemMemoryLimit();
        int score = problemDTO.getProblemScore();
        if (name == null || name.isEmpty() || name.length() > 100)
            ret.put("problemName", "Tên bài tập không được để trống và quá 100 ký tự");
        if (timeLimit == 0 || timeLimit > 1000)
            ret.put("problemTimeLimit", "Giới hạn thời gian không được 0s và không quá 1000s");
        if (memoryLimit == 0 || memoryLimit > 1000)
            ret.put("problemMemoryLimit", "Giới hạn bộ nhớ không được 0MB và không quá 1000MB");
        if (score < 10 || score > 1000)
            ret.put("problemScore", "Điểm bài tập nằm trong đoạn từ 10 đến 1000");
        if (types.length == 0)
            ret.put("problemTypes", "Vui lòng chọn ít nhất một loại bài tập");
        if (!levelRepository.existsById((byte) levelId))
            ret.put("level", "Không tìm thấy mức độ");
        if (!teacherRepository.existsById(teacherId))
            ret.put("server", "Không tìm thấy giáo viên");
        return ret;
    }

    private boolean isUploaded(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    private boolean inputsOutputsValid(MultipartFile[] inputs, MultipartFile[] outputs) {
        // nếu không cùng kích thước thì sai
        if (inputs.length != outputs.length)
            return false;
        // cùng kích thước nhưng mỗi bên có 1 testcase
        if (inputs.length == 1) {
            // đều được upload => đúng
            if (isUploaded(inputs[0]) && isUploaded(outputs[0]))
                return true;
            // đều không được upload => đúng
            if (!isUploaded(inputs[0]) && !isUploaded(outputs[0]))
                return true;
            // 1 trong 2 không được upload => sai
            if (isUploaded(inputs[0]) || isUploaded(outputs[0]))
                return false;
        }
        for (int i = 0; i < inputs.length; ++i) {
            String fullInputName = inputs[i].getOriginalFilename();
            String fullOutputName = outputs[i].getOriginalFilename();
            if (fullInputName == null || fullOutputName == null)
                return false;
            String inputName = FileHelper.getBaseNameFromPath(fullInputName);
            String outputName = FileHelper.getBaseNameFromPath(fullOutputName);
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
