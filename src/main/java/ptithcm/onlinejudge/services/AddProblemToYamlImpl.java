package ptithcm.onlinejudge.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.helper.FileHelper;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.TestCase;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.yaml.Info;
import ptithcm.onlinejudge.model.yaml.ProblemYaml;
import ptithcm.onlinejudge.model.yaml.ProblemsYaml;
import ptithcm.onlinejudge.model.yaml.Subtask;
import ptithcm.onlinejudge.repository.ProblemRepository;
import ptithcm.onlinejudge.repository.TestCaseRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddProblemToYamlImpl implements AddProblemToYaml{
    static final private String problemsPath = System.getProperty("user.dir") + "/problem_info/problems.yml";
    static final private String problemInfoPath = System.getProperty("user.dir") + "/problem_info";
    @Autowired
    private TestCaseRepository testCaseRepository;
    @Autowired
    private ProblemRepository problemRepository;
    @Override
    public ResponseObject addProblemToYaml(ProblemYaml problemYaml) {
        ObjectMapper objectMapper = new YAMLMapper();
        ProblemsYaml problemsYamlObjectMap = null;
        try {
            problemsYamlObjectMap = objectMapper.readValue(new File(problemsPath),
                    new TypeReference<>() {});
        } catch (IOException e) {
            return new ResponseObject(HttpStatus.FOUND, "Open file problems.yml err", "");
        }

        List<ProblemYaml> problemList = problemsYamlObjectMap.getGroups().get(0).getProblems();
        problemList.add(problemYaml);
        try {
            objectMapper.writeValue(new File(problemsPath), problemsYamlObjectMap);
        } catch (IOException e) {
            return new ResponseObject(HttpStatus.FOUND, "Write file problems.yml err", "");
        }
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }

    @Override
    public ResponseObject addProblemToDir(Info info, MultipartFile[] inputs, MultipartFile[] outputs) {
        File problem = new File(problemInfoPath + "/" + info.getProblemId());
        File problemInfo = new File(problemInfoPath + "/" + info.getProblemId() + "/subtasks");
        if (!problem.exists()){
            if (!problem.mkdirs())
                return new ResponseObject(HttpStatus.FOUND, "Make directory problem failed", "");
        }
        if (!problemInfo.exists()){
            if (!problemInfo.mkdirs())
                return new ResponseObject(HttpStatus.FOUND, "Make directory subtasks failed", "");
        }
        ResponseObject responseAddProblemInfo = addProblemInfo(info, problemInfoPath + "/" + info.getProblemId() + "/info.yml");
        //validate responseAddProblem
        if (responseAddProblemInfo.getStatus().equals(HttpStatus.FOUND))
            return new ResponseObject(HttpStatus.FOUND, "Add problem failed", "");
        Optional<Problem> foundProblem = problemRepository.findById(info.getProblemId());
        if (foundProblem.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Problem is not exist", "");
        Problem problemEntity = foundProblem.get();
        // get name of subtask to create subtask folder of problem
        String subtaskName = info.getSubtasks().get(0).getName();
        File problemSubtask = new File(problemInfoPath + "/" + info.getProblemId() + "/subtasks/" + subtaskName);
        if (!problemSubtask.exists()) {
            if (!problemSubtask.mkdirs())
                return new ResponseObject(HttpStatus.FOUND, "Make subtask directory failed", "");
        }
        // create and copy data from MultipartFile inputs, outputs to subtask folder
        if (info.getSubtasks().get(0).getNumSamples() == 0)
            return new ResponseObject(HttpStatus.OK, "Success", "");
        String testCasePath = "/" + info.getProblemId() + "/subtasks/" + subtaskName + "/";
        for (int i = 0; i < inputs.length; ++i) {
            String fileInputName = StringUtils.cleanPath(inputs[i].getOriginalFilename());
            String fileOutputName = StringUtils.cleanPath(outputs[i].getOriginalFilename());
            Path inputPath = Paths.get(problemInfoPath + testCasePath + fileInputName);
            Path outputPath = Paths.get(problemInfoPath + testCasePath + fileOutputName);
            try {
                Files.copy(inputs[i].getInputStream(), inputPath, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(outputs[i].getInputStream(), outputPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            TestCase testCase = new TestCase();
            testCase.setId(UUID.randomUUID().toString().replace("-", ""));
            testCase.setProblem(problemEntity);
            testCase.setTestCaseIn(testCasePath + fileInputName);
            testCase.setTestCaseOut(testCasePath + fileOutputName);
            testCaseRepository.save(testCase);
        }
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }

    @Override
    public ResponseObject addProblemInfo(Info info, String problemPath) {
        ObjectMapper objectMapper = new YAMLMapper();
        try {
            objectMapper.writeValue(new File(problemPath), info);
        } catch (IOException e) {
            return new ResponseObject(HttpStatus.FOUND, "Write file info.yml err", "");
        }
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }
}
