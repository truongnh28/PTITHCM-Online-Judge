package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.model.response.ResponseObject;

public interface SubmissionManagementService {

    ResponseObject getSubmissionById(String submissionId);

    ResponseObject getSubmissionsOfContest(String contestId, int page);

    ResponseObject searchSubmissionsOfContestByKeyword(String contestId, String keyword, int page);
}
