package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.model.request.SubmitRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface SubmitService {
    ResponseObject getProblemListAdapter();
    ResponseObject getProblemInfoAdapter(String problemId);
    ResponseObject getStatusAdapter(String jobId);
    ResponseObject getSubmissionSourceAdapter(String jobId);
    ResponseObject getSubmissionsAdapter(int pageNumber, String key);
    ResponseObject submitProblemAdapter(SubmitRequest submitRequest);
    ResponseObject submitProblemFromController(String studentId, String problemId, String contestId, String language, MultipartFile file);
}
