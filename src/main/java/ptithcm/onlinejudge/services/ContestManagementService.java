package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.ContestDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface ContestManagementService {
    ResponseObject addContest(ContestDTO contest, String teacherId, String groupId);

    ResponseObject cloneContest(ContestDTO contest, String teacherId, String contestId, String groupId);

    ResponseObject editContest(ContestDTO contest, String contestId);

    ResponseObject deleteContest(String contestId);

    ResponseObject getContestActiveSortByDate();

    ResponseObject searchContestsActiveSortByDate(String keyword);

    ResponseObject getAllContestsActive();

    ResponseObject searchAllContestsActive(String keyword);

    ResponseObject getContestById(String contestId);

    ResponseObject searchContestsActiveCreateByTeacher(String teacherId, String keyword);

    ResponseObject lockContest(String contestId);

    ResponseObject unlockContest(String contestId);

    ResponseObject getAllContestsCreateByTeacher(String teacherId);

    ResponseObject searchAllContestsCreateByTeacher(String teacherId, String keyword);

    ResponseObject getAllContestsActiveCreatedByTeacher(String teacherId);

    ResponseObject getAllContestActiveCreatedByTeacherOfGroupId(String teacherId, String groupId);

}
