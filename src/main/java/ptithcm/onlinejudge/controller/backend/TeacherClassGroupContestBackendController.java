package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.ContestRepository;
import ptithcm.onlinejudge.repository.SubjectClassGroupRepository;
import ptithcm.onlinejudge.repository.SubjectClassRepository;

@RestController
@RequestMapping("/teacher/class/{classId}/group/{groupId}/contest")
public class TeacherClassGroupContestBackendController {
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Autowired
    private SubjectClassGroupRepository subjectClassGroupRepository;
    @Autowired
    private ContestRepository contestRepository;
    @PostMapping("/{contestId}")
    public ResponseObject checkUnique(@PathVariable("classId") String classId,
                                      @PathVariable("groupId") String groupId,
                                      @PathVariable("contestId") String contestId) {
        if (!checkValid(classId, groupId))
            return new ResponseObject(HttpStatus.FOUND, "Mã lớp hoặc mã môn học không tồn tại! Vui lòng kiểm tra lại server", null);
        if (contestRepository.existsById(contestId))
            return new ResponseObject(HttpStatus.FOUND, "Mã bài thực hành đã tồn tại! Vui lòng chọn một mã khác", null);
        return new ResponseObject(HttpStatus.OK, "Success", null);
    }

    @GetMapping("/{contestId}")
    public ResponseObject getOne(@PathVariable("classId") String classId,
                                 @PathVariable("groupId") String groupId,
                                 @PathVariable("contestId") String contestId) {
        if (!checkValid(classId, groupId))
            return new ResponseObject(HttpStatus.FOUND, "Mã lớp hoặc mã môn học không tồn tại! Vui lòng kiểm tra lại server", null);
        if (!contestRepository.existsById(contestId))
            return new ResponseObject(HttpStatus.FOUND, "Mã bài thực hành không tồn tại! Vui lòng kiểm tra lại server", null);
        return new ResponseObject(HttpStatus.OK, "Success", contestRepository.findById(contestId));
    }

    private boolean checkValid(String classId, String groupId) {
        return subjectClassRepository.existsById(classId) && subjectClassGroupRepository.existsById(groupId);
    }
}
