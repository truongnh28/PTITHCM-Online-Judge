package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.ProblemRequest;
import ptithcm.onlinejudge.model.ResponseObject;
import ptithcm.onlinejudge.model.entity.Problem;

@Service
public interface ProblemManagementService {
    ResponseObject addProblem(ProblemRequest problem, String filePath);
    ResponseObject editProblem(ProblemRequest problemRequest, String filePath);
    ResponseObject deleteProblem(String problemId);
}
