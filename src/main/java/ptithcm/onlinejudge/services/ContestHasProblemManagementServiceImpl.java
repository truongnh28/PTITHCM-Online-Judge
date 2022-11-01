package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.ContestHasProblem;
import ptithcm.onlinejudge.model.entity.ContestHasProblemId;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.ContestHasProblemRepository;
import ptithcm.onlinejudge.repository.ContestRepository;
import ptithcm.onlinejudge.repository.ProblemRepository;

import java.util.Optional;

@Service
public class ContestHasProblemManagementServiceImpl implements ContestHasProblemManagementService {
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private ContestHasProblemRepository contestHasProblemRepository;

    @Override
    public ResponseObject addProblemToContest(String contestId, String problemId) {
        Optional<Problem> foundProblem = problemRepository.findById(problemId);
        Optional<Contest> foundContest = contestRepository.findById(contestId);
        if (foundProblem.isEmpty() || foundContest.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Bài tập hoặc bài thực hành không tồn tại", null);
        ContestHasProblem contestHasProblem = new ContestHasProblem();
        ContestHasProblemId contestHasProblemId = new ContestHasProblemId(contestId, problemId);
        contestHasProblem.setId(contestHasProblemId);
        contestHasProblem.setProblem(foundProblem.get());
        contestHasProblem.setContest(foundContest.get());
        contestHasProblem = contestHasProblemRepository.save(contestHasProblem);
        return new ResponseObject(HttpStatus.OK, "Success", contestHasProblem);
    }

    @Override
    public ResponseObject deleteProblemFromContest(String contestId, String problemId) {
        Optional<Problem> foundProblem = problemRepository.findById(problemId);
        Optional<Contest> foundContest = contestRepository.findById(contestId);
        if (foundProblem.isEmpty() || foundContest.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Bài tập hoặc bài thực hành không tồn tại", null);
        ContestHasProblemId id = new ContestHasProblemId(contestId, problemId);
        Optional<ContestHasProblem> foundContestHasProblem = contestHasProblemRepository.findById(id);
        if (foundContestHasProblem.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy bài tập, thực hành tương ứng", null);
        contestHasProblemRepository.deleteById(id);
        return new ResponseObject(HttpStatus.OK, "Success", null);
    }
}
