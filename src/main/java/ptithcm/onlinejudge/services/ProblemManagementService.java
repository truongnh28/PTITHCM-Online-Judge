package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.dto.ProblemDTO;
import ptithcm.onlinejudge.model.request.ProblemRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface ProblemManagementService {
    ResponseObject addProblem(ProblemRequest problemRequest, String descriptionPath);

    ResponseObject editProblem(ProblemRequest problemRequest, String descriptionPath);

    ResponseObject deleteProblem(String problemId);

    ResponseObject addProblemToContest(String problemId, String contestId);

    ResponseObject addProblemWithTestCasesAndTypes(ProblemDTO problemDTO, String teacherId, int levelId, MultipartFile description, MultipartFile[] inputs, MultipartFile[] outputs, String[] types);

    ResponseObject getAllProblems();
    ResponseObject getAllProblemsCreateByTeacher(String teacherId);

    ResponseObject getAllProblemsForAddingOrRemovingContest(String contestId);

    ResponseObject getAllProblemsOfContest(String contestId);

    ResponseObject getProblemById(String problemId);
}
