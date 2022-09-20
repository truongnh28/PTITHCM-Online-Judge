package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.request.SubmitRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public class SubmitServiceImpl implements SubmitService {

    @Override
    public ResponseObject submitProblem(SubmitRequest submitRequest) {
        return null;
    }

    @Override
    public ResponseObject getSubmissions(int numberPage, String key) {
        return null;
    }

    @Override
    public ResponseObject getSubmissionSource(String jobId) {
        return null;
    }

    @Override
    public ResponseObject getStatusSubmission(String jobId) {
        return null;
    }

    @Override
    public ResponseObject getProblemInfo(String problemId) {
        return null;
    }

    @Override
    public ResponseObject getProblemList() {
        return null;
    }
}
