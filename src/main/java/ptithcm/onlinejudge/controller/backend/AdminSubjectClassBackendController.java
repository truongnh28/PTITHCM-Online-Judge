package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.SubjectClassRepository;
import ptithcm.onlinejudge.repository.SubjectRepository;

@RestController
@RequestMapping("/admin/subject/{subjectId}/class")
public class AdminSubjectClassBackendController {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectClassRepository subjectClassRepository;

    @PostMapping("/{classId}")
    public ResponseObject checkUniqueClass(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId) {
        if (!checkValid(subjectId))
            return new ResponseObject(HttpStatus.FOUND, "Không tồn tại mã môn học! Vui lòng kiểm tra lại server", null);
        if (subjectClassRepository.existsById(classId))
            return new ResponseObject(HttpStatus.FOUND, "Mã lớp học đã tồn tại! Vui lòng chọn mã lớp khác", null);
        return new ResponseObject(HttpStatus.OK, "Success", null);
    }

    @GetMapping("/{classId}")
    public ResponseObject getOne(@PathVariable("subjectId") String subjectId, @PathVariable("classId") String classId) {
        if (!checkValid(subjectId))
            return new ResponseObject(HttpStatus.FOUND, "Không tồn tại mã môn học! Vui lòng kiểm tra lại server", null);
        if (!subjectClassRepository.existsById(classId))
            return new ResponseObject(HttpStatus.FOUND, "Không tồn tại mã lớp! Vui lòng kiểm tra lại server", null);
        return new ResponseObject(HttpStatus.OK, "Success", subjectClassRepository.findById(classId));
    }

    private boolean checkValid(String subjectId) {
        return subjectRepository.existsById(subjectId);
    }
}
