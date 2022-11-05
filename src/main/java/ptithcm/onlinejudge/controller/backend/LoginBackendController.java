package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.onlinejudge.dto.LoginDTO;
import ptithcm.onlinejudge.helper.SHA256Helper;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.StudentRepository;
import ptithcm.onlinejudge.repository.TeacherRepository;

@RestController
public class LoginBackendController {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping("/check/student")
    public ResponseObject checkStudent(LoginDTO loginDTO) {
        String studentId = loginDTO.getUsername();
        String studentPassword = loginDTO.getPassword();
        if (!studentRepository.existsById(studentId))
            return new ResponseObject(HttpStatus.FOUND, "Không tồn tại sinh viên", -1);
        Student student = studentRepository.findById(studentId).get();
        if (student.getActive() == (byte) 1) {
            if (student.getPassword().equals(SHA256Helper.hash(studentPassword)))
                return new ResponseObject(HttpStatus.OK, "Success", 1);
            return new ResponseObject(HttpStatus.FOUND, "Sai thông tin đăng nhập", 0);
        }
        return new ResponseObject(HttpStatus.FOUND, "Tài khoản bị khóa", 2);
    }

    @PostMapping("/check/teacher")
    public ResponseObject checkTeacher(LoginDTO loginDTO) {
        String teacherId = loginDTO.getUsername();
        String teacherPassword = loginDTO.getPassword();
        if (!teacherRepository.existsById(teacherId))
            return new ResponseObject(HttpStatus.FOUND, "Không tồn tại giảng viên", -1);
        Teacher teacher = teacherRepository.findById(teacherId).get();
        if (teacher.getActive() == (byte) 1) {
            if (teacher.getPassword().equals(SHA256Helper.hash(teacherPassword)))
                return new ResponseObject(HttpStatus.OK, "Success", 1);
            return new ResponseObject(HttpStatus.FOUND, "Sai thông tin đăng nhập", 0);
        }
        return new ResponseObject(HttpStatus.FOUND, "Tài khoản bị khóa", 2);
    }
}
