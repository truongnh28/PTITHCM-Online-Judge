package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.model.request.MultipleTestCaseRequest;
import ptithcm.onlinejudge.model.request.TestCaseRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

public interface TestCaseManagementService {
    ResponseObject addTestCaseProblem(TestCaseRequest testCaseRequest);

    ResponseObject addMultipleTestCaseProblem(MultipleTestCaseRequest multipleTestCaseRequest);
}
