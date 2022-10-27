package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.model.response.ResponseObject;

public interface SubmissionManagementService {

    ResponseObject getSubmissionById(String submissionId);

    ResponseObject getSubmissionsByContest(String contestId, int page);
}
