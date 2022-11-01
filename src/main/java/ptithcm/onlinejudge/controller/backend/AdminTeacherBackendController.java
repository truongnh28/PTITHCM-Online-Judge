package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.TeacherRepository;

@RestController
@RequestMapping("/admin/teacher")
public class AdminTeacherBackendController {
    @Autowired
    private TeacherRepository teacherRepository;
    @PostMapping("/{id}")
    public ResponseObject checkUniqueTeacher(@PathVariable("id") String id) {
        if (teacherRepository.existsById(id))
            return new ResponseObject(HttpStatus.FOUND, "Mã giáo viên đã tồn tại! Vui lòng chọn mã giáo viên khác", null);
        return new ResponseObject(HttpStatus.OK, "Success", null);
    }

    @GetMapping("/{id}")
    public ResponseObject getOne(@PathVariable("id") String id) {
        if (!teacherRepository.existsById(id))
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy giáo viên! Vui lòng kiểm tra lại server", null);
        return new ResponseObject(HttpStatus.OK, "Success", teacherRepository.findById(id));
    }
}
