package ptithcm.onlinejudge.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.helper.FileHelper;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.TestCase;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.yaml.Info;
import ptithcm.onlinejudge.repository.ProblemRepository;
import ptithcm.onlinejudge.repository.TestCaseRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class TestCaseManagementServiceImpl implements TestCaseManagementService {
    static final private String problemInfoPath = System.getProperty("user.dir") + "/problem_info";
    @Autowired
    private AddProblemToYaml addProblemToYaml;
    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Override
    public ResponseObject getTestCasesByProblem(String problemId) {
        if (!problemRepository.existsById(problemId))
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy bài tập", null);
        return new ResponseObject(HttpStatus.OK, "Success", testCaseRepository.getTestCasesByProblem(problemId));
    }

    @Override
    public ResponseObject deleteTestCase(String problemId, String testCaseId) {
        if (!problemRepository.existsById(problemId))
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy bài tập", null);
        Optional<TestCase> foundTestCase = testCaseRepository.findById(testCaseId);
        if (foundTestCase.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy test case", null);
        // lấy test case đã có
        TestCase testCase = foundTestCase.get();
        // lấy đường dẫn test input và output
        String testInPath = testCase.getTestCaseIn();
        String testOutPath = testCase.getTestCaseOut();
        // xóa trong database
        testCaseRepository.deleteById(testCaseId);
        // đọc info
        Info info = addProblemToYaml.getProblemInfo(problemInfoPath + "/" + problemId + "/info.yml");
        // set numsamples thành mới
        info.getSubtasks().get(0).setNumSamples(testCaseRepository.countTestCaseByProblem(problemId));
        // cập nhật
        ResponseObject addProblemInfoResponse = addProblemToYaml.addProblemInfo(info, problemInfoPath + "/" + problemId + "/info.yml");
        if (!addProblemInfoResponse.getStatus().equals(HttpStatus.OK))
            return new ResponseObject(HttpStatus.OK, "Không thể update test case thành công", null);
        // Xử lý trong folder subtask
        // xóa test case cũ
        File testInFile = new File(problemInfoPath + testInPath);
        File testOutFile = new File(problemInfoPath + testOutPath);
        if (!testInFile.delete() || !testOutFile.delete())
            return new ResponseObject(HttpStatus.FOUND, "Không thể xóa test được", null);
        // update các file trong folder subtask để đảm bảo sự liên tục
        String subtask = problemInfoPath + "/" + problemId + "/subtasks/main/";
        File subtaskFolder = new File(subtask);
        // lọc các file input và output
        File[] inputs = subtaskFolder.listFiles((dir, name) -> name.endsWith(".in"));
        File[] outputs = subtaskFolder.listFiles(((dir, name) -> name.endsWith(".out")));
        int testCount = 0;
        if (inputs != null && outputs != null) {
            if (inputs.length != outputs.length)
                return new ResponseObject(HttpStatus.FOUND, "Có vấn đề trong file test case", null);
            testCount = inputs.length;
        }
        // Sắp xếp cho đúng thứ tự 1 2 ... 9 10 11...
        Arrays.sort(inputs, Comparator.comparingInt(o -> o.getName().length()));
        Arrays.sort(outputs, Comparator.comparingInt(o -> o.getName().length()));
        List<TestCase> testCases = testCaseRepository.getTestCasesByProblem(problemId);
        for (int i = 0; i < testCases.size(); ++i) {
            TestCase test = testCases.get(i);
            // tên file input, output cũ
            String oldInput = inputs[i].getName();
            String oldOutput = outputs[i].getName();
            // tên file input, output mới
            String newInput = (i + 1) + ".in";
            String newOutput = (i + 1) + ".out";
            if (oldInput.equals(newInput) && oldOutput.equals(newOutput))
                continue;
            // không giống file cũ thì tạo mới nó
            Path newInputPath = Path.of(subtask + newInput);
            Path newOutputPath = Path.of(subtask + newOutput);
            try {
                // đọc nội dung file input và output cũ
                String inputString = Files.readString(Path.of(subtask + oldInput));
                String outputString = Files.readString(Path.of(subtask + oldOutput));
                // sau đó ghi vào file input và output mới
                Files.writeString(newInputPath, inputString);
                Files.writeString(newOutputPath, outputString);
                File oldInputFile = inputs[i];
                File oldOutputFile = outputs[i];
                if (!oldInputFile.delete() || !oldOutputFile.delete())
                    return new ResponseObject(HttpStatus.FOUND, "Không thể xóa file cũ", null);
            } catch (IOException e) {
                return new ResponseObject(HttpStatus.FOUND, "Không thể đọc file", null);
            }
            test.setTestCaseIn("/" + problemId + "/subtasks/main/" + newInput);
            test.setTestCaseOut("/" + problemId + "/subtasks/main/" + newOutput);
            testCaseRepository.save(test);
        }
        return new ResponseObject(HttpStatus.OK, "Success", null);
    }

    @Override
    public ResponseObject addTestCase(String problemId, MultipartFile[] inputs, MultipartFile[] outputs) {
        Optional<Problem> foundProblem = problemRepository.findById(problemId);
        if (foundProblem.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy bài tập", null);
        Problem problem = foundProblem.get();
        // kiểm tra tính hợp lệ của inputs và outputs
        if (!inputsOutputsValid(inputs, outputs))
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Upload file không hợp lệ", null);
        // đọc info
        Info info = addProblemToYaml.getProblemInfo(problemInfoPath + "/" + problemId + "/info.yml");
        if (info == null)
            return new ResponseObject(HttpStatus.FOUND, "Không thể tìm được bài tập", null);
        int newTestCount = inputs.length;
        int oldTestCount = info.getSubtasks().get(0).getNumSamples();
        // cập nhật số lượng test
        info.getSubtasks().get(0).setNumSamples(oldTestCount + newTestCount);
        // cập nhật info
        ResponseObject addProblemToYamlResponse = addProblemToYaml.addProblemInfo(info, problemInfoPath + "/" + problemId + "/info.yml");
        if (!addProblemToYamlResponse.getStatus().equals(HttpStatus.OK))
            return new ResponseObject(HttpStatus.FOUND, "Không thể thêm bài tập", null);
        // xử lý trong folder subtask
        String subtask = "/" + problemId + "/subtasks/main/";
        for (int i = newTestCount + 1; i <= newTestCount + oldTestCount; ++i) {
            int j = i - newTestCount - 1;
            String originalInput = inputs[j].getOriginalFilename();
            String originalOutput = outputs[j].getOriginalFilename();
            if (originalInput == null || originalOutput == null)
                return new ResponseObject(HttpStatus.BAD_REQUEST, "Không thể upload test case", null);
            String inputName = i + ".in";
            String outputName = i + ".out";
            Path inputPath = Paths.get(problemInfoPath + subtask + inputName);
            Path outputPath = Paths.get(problemInfoPath + subtask + outputName);
            try {
                Files.copy(inputs[j].getInputStream(), inputPath, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(outputs[j].getInputStream(), outputPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                return new ResponseObject(HttpStatus.FOUND, "Không thể copy file từ client sang server", null);
            }
            TestCase testCase = new TestCase();
            testCase.setId(UUID.randomUUID().toString().replace("-", ""));
            testCase.setProblem(problem);
            testCase.setTestCaseIn(subtask + inputName);
            testCase.setTestCaseOut(subtask + outputName);
            testCaseRepository.save(testCase);
        }
        return new ResponseObject(HttpStatus.OK, "Success", null);
    }

    @Override
    public ResponseObject getContentTestCase(String problemId, String testCaseId) {
        if (!problemRepository.existsById(problemId))
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy bài tập", null);
        Optional<TestCase> foundTest = testCaseRepository.findById(testCaseId);
        if (foundTest.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy test thử", null);
        TestCase testCase = foundTest.get();
        Map<String, String> result = new HashMap<>();
        try {
            String inputString = Files.readString(Path.of(problemInfoPath + testCase.getTestCaseIn()));
            String outputString = Files.readString(Path.of(problemInfoPath + testCase.getTestCaseOut()));
            result.put("input", inputString);
            result.put("output", outputString);
        } catch (IOException e) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Không đọc được nội dung test", null);
        }
        return new ResponseObject(HttpStatus.OK, "Success", result);
    }

    @Override
    public ResponseObject editTestCase(String input, String output, String problemId, String testCaseId) {
        if (!problemRepository.existsById(problemId))
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy bài tập", null);
        Optional<TestCase> foundTest = testCaseRepository.findById(testCaseId);
        if (foundTest.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy test thử", null);
        TestCase testCase = foundTest.get();
        try {
            Files.writeString(Path.of(problemInfoPath + testCase.getTestCaseIn()), input);
            Files.writeString(Path.of(problemInfoPath + testCase.getTestCaseOut()), output);
        } catch (IOException e) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Không thể ghi nội dung ra file", null);
        }
        return new ResponseObject(HttpStatus.OK, "Success", null);
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
            // 1 trong 2 không được upload hoặc cả 2 đều không được upload => sai
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
