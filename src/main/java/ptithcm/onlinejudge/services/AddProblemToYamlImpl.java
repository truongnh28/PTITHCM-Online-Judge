package ptithcm.onlinejudge.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.TestCase;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.yaml.Info;
import ptithcm.onlinejudge.model.yaml.ProblemYaml;
import ptithcm.onlinejudge.model.yaml.ProblemsYaml;
import ptithcm.onlinejudge.repository.ProblemRepository;
import ptithcm.onlinejudge.repository.TestCaseRepository;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

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
        boolean found = false;
        for(int i = 0; i < problemList.size(); i++) {
            if(Objects.equals(problemList.get(i).getId(), problemYaml.getId())) {
                problemList.set(i, problemYaml);
                found = true;
            }
        }
        if (!found)
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
        File fileProblem = new File(problemInfoPath + "/" + info.getProblemId());
        File fileProblemSubtask = new File(problemInfoPath + "/" + info.getProblemId() + "/subtasks");
        if (!fileProblem.exists()){
            if (!fileProblem.mkdirs())
                return new ResponseObject(HttpStatus.FOUND, "Make directory problem failed", null);
        }
        if (!fileProblemSubtask.exists()){
            if (!fileProblemSubtask.mkdirs())
                return new ResponseObject(HttpStatus.FOUND, "Make directory subtasks failed", null);
        }
        ResponseObject responseAddProblemInfo = addProblemInfo(info, problemInfoPath + "/" + info.getProblemId() + "/info.yml");
        //validate responseAddProblem
        if (responseAddProblemInfo.getStatus().equals(HttpStatus.FOUND))
            return new ResponseObject(HttpStatus.FOUND, "Add problem failed", null);
        Optional<Problem> foundProblem = problemRepository.findById(info.getProblemId());
        if (foundProblem.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Problem is not exist", null);
        Problem problemEntity = foundProblem.get();
        // get name of subtask to create subtask folder of problem
        String subtaskName = info.getSubtasks().get(0).getName();
        File problemSubtask = new File(problemInfoPath + "/" + info.getProblemId() + "/subtasks/" + subtaskName);
        if (!problemSubtask.exists()) {
            if (!problemSubtask.mkdirs())
                return new ResponseObject(HttpStatus.FOUND, "Make subtask directory failed", null);
        }
        // create and copy data from MultipartFile inputs, outputs to subtask folder
        if (info.getSubtasks().get(0).getNumSamples() == 0)
            return new ResponseObject(HttpStatus.OK, "Success", null);
        String testCasePath = "/" + info.getProblemId() + "/subtasks/" + subtaskName + "/";
        for (int i = 0; i < inputs.length; ++i) {
            String originalFileInput = inputs[i].getOriginalFilename();
            String originalFileOutput = outputs[i].getOriginalFilename();
            if (originalFileInput == null || originalFileOutput == null)
                return new ResponseObject(HttpStatus.BAD_REQUEST, "Không thể upload test case", null);
            String fileInputName = (i + 1) + ".in";
            String fileOutputName = (i + 1) + ".out";
            Path inputPath = Paths.get(problemInfoPath + testCasePath + fileInputName);
            Path outputPath = Paths.get(problemInfoPath + testCasePath + fileOutputName);
            try {
                Files.copy(inputs[i].getInputStream(), inputPath, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(outputs[i].getInputStream(), outputPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                return new ResponseObject(HttpStatus.FOUND, "Không thể copy từ client sang server", null);
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

    @Override
    //info == null -> err.
    public Info getProblemInfo(String problemPath) {
        Info info = new Info();
        ObjectMapper objectMapper = new YAMLMapper();
        try {
            info = objectMapper.readValue(new File(problemPath),
                    new TypeReference<>() {});
            return info;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    //List<ProblemYaml> == null -> err
    public List<ProblemYaml> getProblemYaml() {
        ObjectMapper objectMapper = new YAMLMapper();
        ProblemsYaml problemsYamlObjectMap = null;
        try {
            problemsYamlObjectMap = objectMapper.readValue(new File(problemsPath),
                    new TypeReference<>() {
                    });
        } catch (IOException e) {
            return null;
        }
        return problemsYamlObjectMap.getGroups().get(0).getProblems();
    }

    public static void main(String[] args) {
        File subtaskFolder= new File(problemInfoPath + "/aplusb1/subtasks/main");
        File[] input = subtaskFolder.listFiles((dir, name) -> name.endsWith(".in"));
        File[] output = subtaskFolder.listFiles((dir, name) -> name.endsWith(".out"));
        Arrays.sort(input, Comparator.comparingInt(o -> o.getName().length()));
        Arrays.sort(output, Comparator.comparingInt(o -> o.getName().length()));
        System.out.println("OK");
    }
}
