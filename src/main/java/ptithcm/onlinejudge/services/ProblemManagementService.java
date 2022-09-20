package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.request.ProblemRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface ProblemManagementService {
    ResponseObject addProblem(ProblemRequest problemRequest, String descriptionPath);

    ResponseObject editProblem(ProblemRequest problemRequest, String descriptionPath);

    ResponseObject deleteProblem(String problemId);

    ResponseObject addProblemToContest(String problemId, String contestId);

    ResponseObject getAllProblemCreateByTeacher(String teacherId);

    ResponseObject getProblemById(String problemId);
}
