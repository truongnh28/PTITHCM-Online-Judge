package ptithcm.onlinejudge.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.helper.FileHelper;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.TestCase;
import ptithcm.onlinejudge.model.request.MultipleTestCaseRequest;
import ptithcm.onlinejudge.model.request.TestCaseRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.TestCaseRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class TestCaseManagementServiceImpl implements TestCaseManagementService {
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private TestCaseRepository testCaseRepository;

    @Override
    public ResponseObject addTestCaseProblem(TestCaseRequest testCaseRequest) {
        TestCase testCase = new TestCase();
        testCase.setId(UUID.randomUUID().toString().replace("-", ""));
        testCase.setTestCaseIn(testCaseRequest.getTestCaseIn());
        testCase.setTestCaseOut(testCaseRequest.getTestCaseOut());
        testCase.setTestCaseScore(testCaseRequest.getTestCaseScore());
        testCase.setProblem(testCaseRequest.getProblem());
        testCase = testCaseRepository.save(testCase);
        return new ResponseObject(HttpStatus.OK, "Success", testCase);
    }

    @Override
    public ResponseObject addMultipleTestCaseProblem(MultipleTestCaseRequest multipleTestCaseRequest) {
        String[] testInPaths = multipleTestCaseRequest.getTestInPaths();
        String[] testOutPaths = multipleTestCaseRequest.getTestOutPaths();
        Problem problem = multipleTestCaseRequest.getProblem();
        // sort test cases
        Arrays.sort(testInPaths);
        Arrays.sort(testOutPaths);
        // check test cases is valid
        if (!checkTestCaseFilesIsValid(testInPaths, testOutPaths))
            return new ResponseObject(HttpStatus.FOUND, "Test case request invalid", "");

        ObjectMapper objectMapper = new ObjectMapper();
        String[] uploadTestCaseInPath = new String[testInPaths.length];
        String[] uploadTestCaseOutPath = new String[testOutPaths.length];
        // upload every pair of test. Ex: (1.in, 1.out), (2.in, 2.out),...
        for (int i = 0; i < testInPaths.length; ++i) {
            ResponseObject responseUploadTestCaseIn = uploadFileService.uploadFile(testInPaths[i]);
            ResponseObject responseUploadTestCaseOut = uploadFileService.uploadFile(testOutPaths[i]);
            if (!responseUploadTestCaseIn.getStatus().equals(HttpStatus.OK) || !responseUploadTestCaseOut.getStatus().equals(HttpStatus.OK))
                return new ResponseObject(HttpStatus.FOUND, "Test case upload has problem", "");
            Map uploadTestCaseInInfo = objectMapper.convertValue(responseUploadTestCaseIn.getData(), Map.class);
            Map uploadTestCaseOutInfo = objectMapper.convertValue(responseUploadTestCaseOut.getData(), Map.class);
            uploadTestCaseInPath[i] = uploadTestCaseInInfo.get("url").toString();
            uploadTestCaseOutPath[i] = uploadTestCaseOutInfo.get("url").toString();
        }

        int score = problem.getProblemScore();
        int testCaseScore = score / testInPaths.length;
        // store to database
        for (int i = 0; i < testInPaths.length; ++i) {
            TestCaseRequest testCaseRequest = new TestCaseRequest();
            testCaseRequest.setTestCaseIn(uploadTestCaseInPath[i]);
            testCaseRequest.setTestCaseOut(uploadTestCaseOutPath[i]);
            testCaseRequest.setTestCaseScore(testCaseScore);
            testCaseRequest.setProblem(problem);
            ResponseObject responseAddTestCase = addTestCaseProblem(testCaseRequest);
            if (!responseAddTestCase.getStatus().equals(HttpStatus.OK))
                return new ResponseObject(HttpStatus.FOUND, "Add test case to problem failed", "");
        }
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }

    private boolean checkTestCaseFilesIsValid(String[] testCaseInPath, String[] testCaseOutPath) {
        if (testCaseInPath.length != testCaseOutPath.length)
            return false;
        for (int i = 0; i < testCaseInPath.length; ++i) {
            if (!Files.exists(Path.of(testCaseInPath[i])) || !Files.exists(Path.of(testCaseOutPath[i])))
                return false;
            String fileNameIn = FileHelper.getFileNameFromPath(testCaseInPath[i]);
            String fileNameOut = FileHelper.getFileNameFromPath(testCaseOutPath[i]);
            if (!fileNameIn.equals(fileNameOut))
                return false;
            String fileExtensionIn = FileHelper.getFileExtensionFromPath(testCaseInPath[i]);
            String fileExtensionOut = FileHelper.getFileExtensionFromPath(testCaseOutPath[i]);
            if (!fileExtensionIn.equals("inp") || !fileExtensionOut.equals("outp"))
                return false;
        }
        return true;
    }
}
