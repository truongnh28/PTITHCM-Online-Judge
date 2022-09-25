package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.model.request.ContestHasProblemRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

public interface ContestHasProblemManagementService {
    ResponseObject addContestProblem(ContestHasProblemRequest request);

    ResponseObject deleteContestProblem(ContestHasProblemRequest request);
}
