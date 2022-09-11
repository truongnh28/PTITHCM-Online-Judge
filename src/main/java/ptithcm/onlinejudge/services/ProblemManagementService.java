package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.request.ProblemRequest;
import ptithcm.onlinejudge.model.ResponseObject;

@Service
public interface ProblemManagementService {
    ResponseObject addProblem(ProblemRequest problemRequest, String filePath);
    ResponseObject editProblem(ProblemRequest problemRequest, String filePath);
    ResponseObject deleteProblem(String problemId);

    ResponseObject addProblemToContest(String problemId, String contestId);
}
