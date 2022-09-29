package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.adapter.GetStatusResponse;
import ptithcm.onlinejudge.model.entity.Submission;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.SubmissionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionManagementServiceImpl implements SubmissionManagementService {
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private SubmitService submitService;
    @Override
    public ResponseObject getAllSubmission() {
        List<Submission> submissions = submissionRepository.findAll(Sort.by("submissionTime").descending());
        return new ResponseObject(HttpStatus.OK, "Success", submissions);
    }

    @Override
    public ResponseObject getSubmissionById(String submissionId) {
        Optional<Submission> foundSubmission = submissionRepository.findById(submissionId);
        if (foundSubmission.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Submission not exist", "");
        Submission submission = foundSubmission.get();
        if (submission.getVerdict() != (byte) 0)
            return new ResponseObject(HttpStatus.OK, "Success", submission);
        ResponseObject getStatusResponse = submitService.getStatusAdapter(submissionId);
        if (!getStatusResponse.getStatus().equals(HttpStatus.OK))
            return new ResponseObject(HttpStatus.FOUND, "Not found", null);
        GetStatusResponse statusResponse = (GetStatusResponse) getStatusResponse.getData();
        if (statusResponse.getStatus().equals("queued") || statusResponse.getStatus().equals("judging")) {
            submission.setVerdict((byte) 0);
            submission = submissionRepository.save(submission);
            return new ResponseObject(HttpStatus.OK, "Success", submission);
        }
        if (statusResponse.getStatus().equals("compile_error")) {
            submission.setSubmissionScore(statusResponse.getFinalScore());
            submission.setVerdict((byte) 4);
            submission = submissionRepository.save(submission);
            return new ResponseObject(HttpStatus.OK, "Success", submission);
        }
        if (statusResponse.getStatus().equals("internal_error")) {
            submission.setSubmissionScore(0);
            submission.setVerdict((byte) 4);
            submission = submissionRepository.save(submission);
            return new ResponseObject(HttpStatus.OK, "Success", submission);
        }
        submission.setSubmissionScore(statusResponse.getFinalScore());
        List<List<String>> results = statusResponse.getSubtasks().get(0);
        boolean wrongAnswer = false, timeLimit = false, runtimeError = false;
        for (List<String> result: results) {
            String subVerdict = result.get(0);
            if (subVerdict.equals("WA"))
                wrongAnswer = true;
            if (subVerdict.equals("TLE"))
                timeLimit = true;
            if (subVerdict.equals("MLE"))
                runtimeError = true;
            if (subVerdict.equals("RE"))
                runtimeError = true;
            if (subVerdict.equals("SK"))
                runtimeError = true;
        }
        if (wrongAnswer)
            submission.setVerdict((byte) 2);
        if (!wrongAnswer && timeLimit && !runtimeError)
            submission.setVerdict((byte) 3);
        if (!wrongAnswer && !timeLimit && runtimeError)
            submission.setVerdict((byte) 4);
        if (!wrongAnswer && !timeLimit && !runtimeError)
            submission.setVerdict((byte) 1);
        submission = submissionRepository.save(submission);
        return new ResponseObject(HttpStatus.OK, "Success", submission);
    }
}
