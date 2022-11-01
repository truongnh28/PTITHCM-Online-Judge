package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.StatisticDTO;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.ContestRepository;
import ptithcm.onlinejudge.repository.ProblemRepository;
import ptithcm.onlinejudge.repository.SubmissionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Override
    public ResponseObject statisticContest(String contestId) {
        if (!contestRepository.existsById(contestId))
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy bài thực hành", null);
        List<Problem> problems = problemRepository.getProblemsActiveOfContest(contestId);
        Map<String, Object> data = new HashMap<>();
        data.put("SumACs", submissionRepository.countAcceptedSubmissionsByContestId(contestId));
        data.put("SumSubs", submissionRepository.countAllSubmissionsByContestId(contestId));
        data.put("SumStudentACs", submissionRepository.countAllStudentsAcceptedByContestId(contestId));
        data.put("SumStudentSubs", submissionRepository.countAllStudentsSubmittedByContestId(contestId));
        List<StatisticDTO> statistics = problems.stream().map(problem -> {
            String problemId = problem.getId();
            StatisticDTO statistic = new StatisticDTO();
            statistic.setProblemId(problemId);
            statistic.setCountACs(submissionRepository.countAcceptedSubmissionByContestIdAndProblemId(contestId, problemId));
            statistic.setCountSubs(submissionRepository.countSubmissionsByContestIdAndProblemId(contestId, problemId));
            statistic.setCountStudentACs(submissionRepository.countStudentAcceptedByContestIdAndProblemId(contestId, problemId));
            statistic.setCountStudentSubs(submissionRepository.countStudentsSubmittedByContestIdAndProblemId(contestId, problemId));
            return statistic;
        }).toList();
        data.put("Detail", statistics);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }
}
