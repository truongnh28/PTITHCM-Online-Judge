package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.adapter.GetStatusResponse;
import ptithcm.onlinejudge.model.entity.Submission;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.ContestRepository;
import ptithcm.onlinejudge.repository.SubmissionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SubmissionManagementServiceImpl implements SubmissionManagementService {
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private SubmitService submitService;

    @Override
    public ResponseObject getSubmissionById(String submissionId) {
        Optional<Submission> foundSubmission = submissionRepository.findById(submissionId);
        if (foundSubmission.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy submission", null);
        Submission submission = foundSubmission.get();
        if (submission.getVerdict() != (byte) 0)
            return new ResponseObject(HttpStatus.OK, "Success", submission);

        ResponseObject getStatusResponse = submitService.getStatusAdapter(submissionId);
        if (!getStatusResponse.getStatus().equals(HttpStatus.OK))
            return new ResponseObject(HttpStatus.FOUND, "Lỗi server", null);
        GetStatusResponse statusResponse = (GetStatusResponse) getStatusResponse.getData();
        if (statusResponse.getStatus().equals("queued") || statusResponse.getStatus().equals("judging")) {
            submission.setVerdict((byte) 0);
            submission = submissionRepository.save(submission);
            return new ResponseObject(HttpStatus.OK, "Success", submission);
        }
        if (statusResponse.getStatus().equals("compile_error")) {
            submission.setSubmissionScore(statusResponse.getFinalScore());
            submission.setVerdict((byte) 6);
            submission = submissionRepository.save(submission);
            return new ResponseObject(HttpStatus.OK, "Success", submission);
        }
        if (statusResponse.getStatus().equals("internal_error")) {
            submission.setSubmissionScore(0);
            submission.setVerdict((byte) 5);
            submission = submissionRepository.save(submission);
            return new ResponseObject(HttpStatus.OK, "Success", submission);
        }
        submission.setSubmissionScore(statusResponse.getFinalScore());
        List<List<String>> results = statusResponse.getSubtasks().get(0);
        boolean wrongAnswer = false, timeLimit = false, runtimeError = false, memoryLimit = false;
        for (List<String> result: results) {
            String subVerdict = result.get(0);
            if (subVerdict.equals("WA")) {
                wrongAnswer = true;
                break;
            }
            if (subVerdict.equals("TLE")) {
                timeLimit = true;
                break;
            }
            if (subVerdict.equals("MLE")) {
                memoryLimit = true;
                break;
            }
            if (subVerdict.equals("RE") || subVerdict.equals("SK")) {
                runtimeError = true;
                break;
            }
        }
        if (wrongAnswer)
            submission.setVerdict((byte) 2);
        if (timeLimit)
            submission.setVerdict((byte) 3);
        if (memoryLimit)
            submission.setVerdict((byte) 4);
        if (runtimeError)
            submission.setVerdict((byte) 5);
        if (!wrongAnswer && !timeLimit && !memoryLimit && !runtimeError)
            submission.setVerdict((byte) 1);
        submission = submissionRepository.save(submission);
        return new ResponseObject(HttpStatus.OK, "Success", submission);
    }

    @Override
    public ResponseObject getSubmissionsOfContest(String contestId, int page) {
        if (page <= 0)
            page = 1;
        Page<Submission> submissions = submissionRepository.getSubmissionsOfContest(contestId, PageRequest.of(page - 1, 10));
        int totalPage = submissions.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", submissions.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchSubmissionsOfContestByKeyword(String contestId, String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<Submission> submissions = submissionRepository.searchSubmissionOfContestByKeyword(contestId, "%" + keyword.trim() + "%", PageRequest.of(page - 1, 10));
        int totalPage = submissions.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", submissions.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }
}
