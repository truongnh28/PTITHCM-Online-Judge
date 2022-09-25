package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.ContestDTO;
import ptithcm.onlinejudge.helper.TimeHelper;
import ptithcm.onlinejudge.mapper.ContestMapper;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.model.request.ContestRequest;
import ptithcm.onlinejudge.repository.ContestRepository;
import ptithcm.onlinejudge.repository.TeacherRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ContestManagementServiceImpl implements ContestManagementService{
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ContestMapper contestMapper;

    @Override
    public ResponseObject addContest(ContestRequest contestRequest) {
        if(contestRepository.existsById(contestRequest.getContestId())) {
            return new ResponseObject(HttpStatus.FOUND, "Problem id is exist", "");
        }
        if(!contestRequestIsValid(contestRequest)) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "request data is not valid", "");
        }
        String contestId = contestRequest.getContestId();
        String contestName = contestRequest.getContestName();
        Instant startTime = TimeHelper.convertStringToInstance(contestRequest.getContestStartTime());
        Instant endTime = TimeHelper.convertStringToInstance(contestRequest.getContestEndTime());
        Optional<Teacher> teacher = teacherRepository.findById(contestRequest.getTeacherId());
        Contest contest = new Contest(contestId, contestName, startTime, endTime, (byte)0, teacher.get());
        contestRepository.save(contest);
        return new ResponseObject(HttpStatus.OK, "Success", contest);
    }

    @Override
    public ResponseObject editContest(ContestRequest contestRequest) {
        if(!contestRepository.existsById(contestRequest.getContestId())) {
            return new ResponseObject(HttpStatus.FOUND, "Problem id is not exist", "");
        }
        if(!contestRequestIsValid(contestRequest)) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "request data is not valid", "");
        }
        String contestId = contestRequest.getContestId();
        String contestName = contestRequest.getContestName();
        Instant startTime = TimeHelper.convertStringToInstance(contestRequest.getContestStartTime());
        Instant endTime = TimeHelper.convertStringToInstance(contestRequest.getContestEndTime());
        Optional<Teacher> teacher = teacherRepository.findById(contestRequest.getTeacherId());
        Optional<Contest> contest = contestRepository.findById(contestRequest.getContestId());
        contest.get().setContestName(contestName);
        contest.get().setContestStart(startTime);
        contest.get().setContestEnd(endTime);
        contestRepository.save(contest.get());
        return new ResponseObject(HttpStatus.OK, "Success", contest);
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
    public ResponseObject addContestDTOAndTeacherId(ContestDTO contest, String teacherId) {
        ContestRequest contestRequest = contestMapper.dtoToRequest(contest);
        contestRequest.setTeacherId(teacherId);
        return addContest(contestRequest);
    }

    @Override
    public ResponseObject editContestDTO(ContestDTO contest) {
        ContestRequest contestRequest = contestMapper.dtoToRequest(contest);
        return editContest(contestRequest);
    }

    @Override
    public ResponseObject getAllContestActive() {
        List<Contest> contests = contestRepository.getContestsActive();
        return new ResponseObject(HttpStatus.OK, "Success", contests);
    }

    @Override
    public ResponseObject getContestById(String contestId) {
        Optional<Contest> foundContest = contestRepository.findById(contestId);
        if (foundContest.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Contest not exist", "");
        return new ResponseObject(HttpStatus.OK, "Success", foundContest.get());
    }

    @Override
    public ResponseObject getAllContestCreateByTeacher(String teacherId) {
        if(!teacherRepository.existsById(teacherId)) {
            return new ResponseObject(HttpStatus.FOUND, "Teacher is not exist", "");
        }
        List<Contest> contests = contestRepository.getContestsByTeacher(teacherId);
        return new ResponseObject(HttpStatus.OK, "Success", contests);
    }

    @Override
    public ResponseObject getAllContestActiveCreatedByTeacher(String teacherId) {
        List<Contest> contests = contestRepository.getContestsActiveByTeacherId(teacherId);
        return new ResponseObject(HttpStatus.OK, "Success", contests);
    }

    @Override
    public ResponseObject getAllContestActiveCreatedByTeacherOfGroupId(String teacherId, String groupId) {
        return null;
    }

    private boolean contestRequestIsValid (ContestRequest contestRequest) {
        boolean teacherIdIsValid = teacherRepository.existsById(contestRequest.getTeacherId());
        boolean problemNameIsValid = contestRequest.getContestName().length() > 0;
        Instant sTime = TimeHelper.convertStringToInstance(contestRequest.getContestStartTime());
        Instant eTime = TimeHelper.convertStringToInstance(contestRequest.getContestEndTime());
        boolean timeStartIsValid = sTime.isAfter(Instant.now());
        boolean timeEndIsValid = eTime.isAfter(sTime);
        return problemNameIsValid && timeStartIsValid && timeEndIsValid && teacherIdIsValid;
    }
}
