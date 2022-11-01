package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.SubjectRepository;

@RestController
@RequestMapping("/admin/subject")
public class AdminSubjectBackendController {
    @Autowired
    private SubjectRepository subjectRepository;

    @PostMapping("/{id}")
    public ResponseObject checkUniqueById(@PathVariable("id") String id) {
        if (subjectRepository.existsById(id))
            return new ResponseObject(HttpStatus.FOUND, "Bị trùng mã môn học! Vui lòng chọn mã mới", null);
        return new ResponseObject(HttpStatus.OK, "Thành công", null);
    }

    @GetMapping("/{id}")
    public ResponseObject getOne(@PathVariable("id") String id) {
        if (!subjectRepository.existsById(id))
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy môn học! Vui lòng kiểm tra lại server!", null);
        return new ResponseObject(HttpStatus.OK, "Thành công", subjectRepository.findById(id));
    }
}
