package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.SubjectClassGroupRepository;
import ptithcm.onlinejudge.repository.SubjectClassRepository;
import ptithcm.onlinejudge.repository.SubjectRepository;

@RestController
@RequestMapping("/admin/subject/{subjectId}/class/{classId}/group")
public class AdminSubjectClassGroupBackendController {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Autowired
    private SubjectClassGroupRepository subjectClassGroupRepository;

    @PostMapping("/{id}")
    public ResponseObject checkUniqueGroup(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("id") String groupId) {
        if (!checkValid(subjectId, classId))
            return new ResponseObject(HttpStatus.FOUND, "Mã môn học hoặc mã lớp không tồn tại! Vui lòng kiểm tra lại server", null);
        if (subjectClassGroupRepository.existsById(groupId))
            return new ResponseObject(HttpStatus.FOUND, "Mã nhóm thực hành đã tồn tại! Vui lòng chọn mã khác", null);
        return new ResponseObject(HttpStatus.OK, "Success", null);
    }

    @GetMapping("/{id}")
    public ResponseObject getOne(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId, @PathVariable("id") String groupId) {
        if (!checkValid(subjectId, classId))
            return new ResponseObject(HttpStatus.FOUND, "Mã môn học hoặc mã lớp không tồn tại! Vui lòng kiểm tra lại server", null);
        if (!subjectClassGroupRepository.existsById(groupId))
            return new ResponseObject(HttpStatus.FOUND, "Khồng tồn tại mã nhóm thực hành! Vui lòng kiểm tra lại server", null);
        return new ResponseObject(HttpStatus.OK, "Success", subjectClassGroupRepository.findById(groupId));
    }

    private boolean checkValid(String subjectId, String classId) {
        return subjectRepository.existsById(subjectId) && subjectClassRepository.existsById(classId);
    }
}
