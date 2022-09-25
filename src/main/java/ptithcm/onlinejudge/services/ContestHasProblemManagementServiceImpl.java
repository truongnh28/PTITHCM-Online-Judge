package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.ContestHasProblem;
import ptithcm.onlinejudge.model.entity.ContestHasProblemId;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.request.ContestHasProblemRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.ContestHasProblemRepository;

@Service
public class ContestHasProblemManagementServiceImpl implements ContestHasProblemManagementService {
    @Autowired
    private ProblemManagementService problemManagementService;
    @Autowired
    private ContestManagementService contestManagementService;
    @Autowired
    private ContestHasProblemRepository contestHasProblemRepository;
    @Override
    public ResponseObject addContestProblem(ContestHasProblemRequest request) {
        String problemId = request.getProblemId();
        String contestId = request.getContestId();
        ResponseObject findProblemResponse = problemManagementService.getProblemById(problemId);
        ResponseObject findContestResponse = contestManagementService.getContestById(contestId);
        if (!findContestResponse.getStatus().equals(HttpStatus.OK) || !findProblemResponse.getStatus().equals(HttpStatus.OK))
            return new ResponseObject(HttpStatus.FOUND, "Contest or problem not exist", "");
        Problem problem = (Problem) findProblemResponse.getData();
        Contest contest = (Contest) findContestResponse.getData();
        ContestHasProblem contestHasProblem = new ContestHasProblem();
        ContestHasProblemId contestHasProblemId = new ContestHasProblemId(contestId, problemId);
        contestHasProblem.setId(contestHasProblemId);
        contestHasProblem.setProblem(problem);
        contestHasProblem.setContest(contest);
        contestHasProblem = contestHasProblemRepository.save(contestHasProblem);
        return new ResponseObject(HttpStatus.OK, "Success", contestHasProblem);
    }

    @Override
    public ResponseObject deleteContestProblem(ContestHasProblemRequest request) {
        String problemId = request.getProblemId();
        String contestId = request.getContestId();
        ResponseObject findProblemResponse = problemManagementService.getProblemById(problemId);
        ResponseObject findContestResponse = contestManagementService.getContestById(contestId);
        if (!findContestResponse.getStatus().equals(HttpStatus.OK) || !findProblemResponse.getStatus().equals(HttpStatus.OK))
            return new ResponseObject(HttpStatus.FOUND, "Contest or problem not exist", "");
        ContestHasProblemId contestHasProblemId = new ContestHasProblemId(contestId, problemId);
        contestHasProblemRepository.deleteById(contestHasProblemId);
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }
}
