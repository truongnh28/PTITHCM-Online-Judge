package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.request.SubmitRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface SubmitService {
    ResponseObject submitProblem(SubmitRequest submitRequest);
    ResponseObject getSubmissions(int numberPage, String key);
    ResponseObject getSubmissionSource(String jobId);
    ResponseObject getStatusSubmission(String jobId);
    ResponseObject getProblemInfo(String problemId);
    ResponseObject getProblemList();
}
