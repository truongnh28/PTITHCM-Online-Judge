package ptithcm.onlinejudge.adapter;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.request.SubmitRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface Adapter {
    ResponseObject getProblemListAdapter();
    ResponseObject getProblemInfoAdapter(String problemId);
    ResponseObject getStatusAdapter(String jobId);
    ResponseObject getSubmissionSourceAdapter(String jobId);
    ResponseObject getSubmissionsAdapter(int pageNumber, String key);
    ResponseObject submitProblemAdapter(SubmitRequest submitRequest);
}
