package ptithcm.onlinejudge.adapter;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.request.SubmitRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface Adapter {
    ResponseObject getProblemList();
    ResponseObject getProblemInfo(String problemId);
    ResponseObject getStatus(String jobId);
    ResponseObject getSubmissionSource(String jobId);
    ResponseObject getSubmissions(int pageNumber, String key);
    ResponseObject submitProblem(SubmitRequest submitRequest);
}
