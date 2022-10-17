package ptithcm.onlinejudge.services;

import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.model.request.MultipleTestCaseRequest;
import ptithcm.onlinejudge.model.request.TestCaseRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

public interface TestCaseManagementService {
    ResponseObject getTestCasesByProblem(String problemId);

    ResponseObject deleteTestCase(String problemId, String testCaseId);

    ResponseObject addTestCase(String problemId, MultipartFile[] inputs, MultipartFile[] outputs);

    ResponseObject getContentTestCase(String problemId, String testCaseId);

    ResponseObject editTestCase(String input, String output, String problemId, String testCaseId);
}
