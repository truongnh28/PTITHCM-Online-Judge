package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.model.response.ResponseObject;

public interface ContestHasProblemManagementService {
    ResponseObject addProblemToContest(String contestId, String problemId);

    ResponseObject deleteProblemFromContest(String contestId, String problemId);
}
