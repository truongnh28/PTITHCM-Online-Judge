package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.dto.ProblemDTO;
import ptithcm.onlinejudge.model.request.ProblemRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface ProblemManagementService {
    ResponseObject addProblem(ProblemDTO problemDTO, String teacherId, int levelId, MultipartFile description, MultipartFile[] inputs, MultipartFile[] outputs, String[] types);

    ResponseObject editProblem(ProblemDTO problemDTO, String teacherId, int levelId, MultipartFile description, String[] types);

    ResponseObject deleteProblem(String problemId);

    ResponseObject addProblemToContest(String problemId, String contestId);

    ResponseObject getAllProblemsActive();

    ResponseObject getAllProblemsActiveNotInContest(String contestId, int page);

    ResponseObject searchAllProblemsActiveNotInContest(String contestId, String keyword, int page);

    ResponseObject getAllProblemsCreateByTeacher(String teacherId, int page);

    ResponseObject searchAllProblemsCreateByTeacher(String teacherId, String keyword, int page);

    ResponseObject getAllProblemsForAddingOrRemovingContest(String contestId);

    ResponseObject getAllProblemsActiveOfContest(String contestId);

    ResponseObject getProblemById(String problemId);

    ResponseObject lockProblem(String problemId);

    ResponseObject unlockProblem(String problemId);
}
