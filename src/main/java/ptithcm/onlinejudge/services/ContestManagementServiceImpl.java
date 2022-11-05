package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.ContestDTO;
import ptithcm.onlinejudge.helper.TimeHelper;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.model.entity.*;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.request.ContestRequest;
import ptithcm.onlinejudge.repository.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ContestManagementServiceImpl implements ContestManagementService {
    @Autowired
    private SubjectClassGroupRepository subjectClassGroupRepository;

    @Autowired
    private GroupHasContestRepository groupHasContestRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ContestHasProblemRepository contestHasProblemRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ContestMapper contestMapper;

    @Override
    public ResponseObject addContest(ContestDTO contest, String teacherId, String groupId) {
        String id = contest.getContestId().trim().toUpperCase();
        String name = contest.getContestName().trim();
        String contestStart = TimeHelper.convertStringFormToDateFormatter(contest.getContestStart());
        String contestEnd = TimeHelper.convertStringFormToDateFormatter(contest.getContestEnd());
        Instant start = TimeHelper.convertStringToInstance(contestStart).minus(7, ChronoUnit.HOURS);
        Instant end = TimeHelper.convertStringToInstance(contestEnd).minus(7, ChronoUnit.HOURS);

        Contest newContest = new Contest();
        newContest.setId(id);
        newContest.setContestName(name);
        newContest.setContestStart(start);
        newContest.setContestEnd(end);
        newContest.setHide((byte) 0);
        newContest.setCreateAt(Instant.now());
        newContest.setUpdateAt(Instant.now());
        newContest.setTeacher(teacherRepository.findById(teacherId).get());
        newContest = contestRepository.save(newContest);

        GroupHasContest groupHasContest = new GroupHasContest();
        GroupHasContestId groupHasContestId = new GroupHasContestId(id, groupId);
        groupHasContest.setId(groupHasContestId);
        groupHasContest.setContest(newContest);
        groupHasContest.setSubjectClassGroup(subjectClassGroupRepository.findById(groupId).get());
        groupHasContestRepository.save(groupHasContest);

        return new ResponseObject(HttpStatus.OK, "Success", newContest);
    }

    @Override
    public ResponseObject cloneContest(ContestDTO contest, String teacherId, String contestId, String groupId) {
        ResponseObject addContestResponse = addContest(contest, teacherId, groupId);
        if (!addContestResponse.getStatus().equals(HttpStatus.OK))
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Thêm bài thực hành thất bại", null);
        Contest savedContest = (Contest) addContestResponse.getData();
        String savedContestId = savedContest.getId();
        List<Problem> problems = problemRepository.getProblemsActiveOfContest(contestId);
        for (Problem problem: problems) {
            ContestHasProblem contestHasProblem = new ContestHasProblem();
            ContestHasProblemId id = new ContestHasProblemId(savedContestId, problem.getId());
            contestHasProblem.setId(id);
            contestHasProblem.setProblem(problem);
            contestHasProblem.setContest(savedContest);
            contestHasProblemRepository.save(contestHasProblem);
        }
        return new ResponseObject(HttpStatus.OK, "Success", savedContest);
    }

    @Override
    public ResponseObject editContest(ContestDTO contest, String contestId) {
        String newName = contest.getContestName();
        if (newName == null || newName.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Không được để trống tên", null);
        String newStart = contest.getContestStart();
        String newEnd = contest.getContestEnd();
        if (newStart == null || newStart.isEmpty() || newEnd == null || newEnd.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Thời gian bắt đầu và kết thúc không được để trống", null);
        newStart = TimeHelper.convertStringFormToDateFormatter(contest.getContestStart());
        newEnd = TimeHelper.convertStringFormToDateFormatter(contest.getContestEnd());
        Optional<Contest> foundContest = contestRepository.findById(contestId);
        if (foundContest.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy bài thực hành", null);
        Instant start = TimeHelper.convertStringToInstance(newStart).minus(7, ChronoUnit.HOURS);
        Instant end = TimeHelper.convertStringToInstance(newEnd).minus(7, ChronoUnit.HOURS);
        if (!end.isAfter(start))
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Thời gian bắt đầu và kết thúc không hợp lệ", null);
        Contest editedContest = foundContest.get();
        editedContest.setContestName(newName);
        editedContest.setContestStart(start);
        editedContest.setContestEnd(end);
        editedContest.setUpdateAt(Instant.now());
        editedContest = contestRepository.save(editedContest);
        return new ResponseObject(HttpStatus.OK, "Success", editedContest);
    }

    @Override
    public ResponseObject deleteContest(String contestId) {
        if (!contestRepository.existsById(contestId))
            return new ResponseObject(HttpStatus.FOUND, "Contest not exist", "");
        Contest foundContest = contestRepository.findById(contestId).get();
        foundContest.setHide((byte) 1);
        contestRepository.save(foundContest);
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }

    @Override
    public ResponseObject getContestActiveSortByDate(int page) {
        if (page <= 0)
            page = 1;
        Page<Contest> contests = contestRepository.getContestsActiveDescByDate(PageRequest.of(page - 1, 10));
        int totalPage = contests.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", contests.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchContestsActiveSortByDate(String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<Contest> contests = contestRepository.searchContestsActiveDescByDate("%" + keyword.trim() + "%", PageRequest.of(page - 1, 10));
        int totalPage = contests.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", contests.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject getAllContestsActive(int page) {
        if (page <= 0)
            page = 1;
        Page<Contest> contests = contestRepository.getContestsActive(PageRequest.of(page - 1, 10));
        int totalPage = contests.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", contests.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchAllContestsActive(String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<Contest> contests = contestRepository.searchContestsActive("%" + keyword.trim() + "%", PageRequest.of(page - 1, 10));
        int totalPage = contests.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", contests.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject getContestById(String contestId) {
        Optional<Contest> foundContest = contestRepository.findById(contestId);
        if (foundContest.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Bài thực hành không tồn tại", null);
        return new ResponseObject(HttpStatus.OK, "Success", foundContest.get());
    }

    @Override
    public ResponseObject searchContestsActiveCreateByTeacher(String teacherId, String keyword) {
        if (!teacherRepository.existsById(teacherId))
            return new ResponseObject(HttpStatus.FOUND, "Giáo viên không tồn tại", null);
        List<Contest> contests = contestRepository.searchContestsActiveByTeacherIdDescByDate(teacherId, keyword);
        return new ResponseObject(HttpStatus.OK, "Success", contests);
    }

    @Override
    public ResponseObject lockContest(String contestId) {
        Contest contest = contestRepository.findById(contestId).get();
        contest.setHide((byte) 1);
        contest.setUpdateAt(Instant.now());
        contest = contestRepository.save(contest);
        return new ResponseObject(HttpStatus.OK, "Success", contest);
    }

    @Override
    public ResponseObject unlockContest(String contestId) {
        Contest contest = contestRepository.findById(contestId).get();
        contest.setHide((byte) 0);
        contest.setUpdateAt(Instant.now());
        contest = contestRepository.save(contest);
        return new ResponseObject(HttpStatus.OK, "Success", contest);
    }

    @Override
    public ResponseObject getAllContestsCreateByTeacher(String teacherId, int page) {
        if (page <= 0)
            page = 1;
        Page<Contest> contests = contestRepository.getAllContestsCreateByTeacher(teacherId, PageRequest.of(page - 1, 10));
        int totalPage = contests.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", contests.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchAllContestsCreateByTeacher(String teacherId, String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<Contest> contests = contestRepository.searchContestsCreateByTeacher(teacherId, "%" + keyword.trim() + "%", PageRequest.of(page - 1, 10));
        int totalPage = contests.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", contests.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject getAllContestsActiveCreatedByTeacher(String teacherId) {
        List<Contest> contests = contestRepository.getContestsActiveByTeacherIdDescByDate(teacherId);
        return new ResponseObject(HttpStatus.OK, "Success", contests);
    }

    @Override
    public ResponseObject getAllContestActiveCreatedByTeacherOfGroupId(String teacherId, String groupId) {
        return null;
    }
}
