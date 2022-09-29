package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.ContestDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.request.ContestRequest;

@Service
public interface ContestManagementService {
    ResponseObject addContest(ContestRequest contestRequest);

    ResponseObject cloneContest(ContestDTO contest, String teacherId, String contestId, String groupId);

    ResponseObject editContest(ContestRequest contestRequest);

    ResponseObject deleteContest(String contestId);

    ResponseObject addContestController(ContestDTO contest, String teacherId, String groupId);

    ResponseObject editContestDTO(ContestDTO contest);

    ResponseObject getAllContestActiveSortByDate();

    ResponseObject getAllContestActive();

    ResponseObject getContestById(String contestId);
    ResponseObject getAllContestCreateByTeacher(String teacherId);

    ResponseObject getAllContestActiveCreatedByTeacher(String teacherId);

    ResponseObject getAllContestActiveCreatedByTeacherOfGroupId(String teacherId, String groupId);

}
