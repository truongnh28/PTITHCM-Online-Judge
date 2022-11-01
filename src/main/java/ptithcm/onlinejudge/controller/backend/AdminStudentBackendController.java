package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.StudentRepository;

@RestController
@RequestMapping("/admin/student")
public class AdminStudentBackendController {
    @Autowired
    private StudentRepository studentRepository;
    @PostMapping("/{id}")
    public ResponseObject checkUniqueStudent(@PathVariable("id") String id) {
        if (studentRepository.existsById(id))
            return new ResponseObject(HttpStatus.FOUND, "Bị trùng mã sinh viên! Vui lòng chọn mã sinh viên mới", null);
        return new ResponseObject(HttpStatus.OK, "Thành công", null);
    }

    @GetMapping("/{id}")
    public ResponseObject getOne(@PathVariable("id") String id) {
        if (!studentRepository.existsById(id))
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy mã sinh viên! Vui lòng kiểm tra lại server", null);
        return new ResponseObject(HttpStatus.OK, "Thành công", studentRepository.findById(id));
    }
}
