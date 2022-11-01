package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.LoginDTO;
import ptithcm.onlinejudge.helper.SHA256Helper;
import ptithcm.onlinejudge.helper.TimeHelper;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.StudentRepository;
import ptithcm.onlinejudge.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Override
    public ResponseObject login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        Optional<Student> foundStudent = studentRepository.findById(username);
        Optional<Teacher> foundTeacher = teacherRepository.findById(username);

        if (foundStudent.isPresent()) {
            Student student = foundStudent.get();
            String hashPassword = SHA256Helper.hash(password);
            if (hashPassword.equals(student.getPassword()) && student.getActive() == (byte) 1) {
                student.setLastLogin(TimeHelper.convertStringToInstance(TimeHelper.convertLocalDateTimeToString(LocalDateTime.now())));
                studentRepository.save(student);
                return new ResponseObject(HttpStatus.OK, "Success", student);
            }
            return new ResponseObject(HttpStatus.FOUND, "Không đúng password", null);
        }

        if (foundTeacher.isPresent()) {
            Teacher teacher = foundTeacher.get();
            String hashPassword = SHA256Helper.hash(password);
            if (hashPassword.equals(teacher.getPassword()) && teacher.getActive() == (byte) 1) {
                teacher.setLastLogin(TimeHelper.convertStringToInstance(TimeHelper.convertLocalDateTimeToString(LocalDateTime.now())));
                teacherRepository.save(teacher);
                return new ResponseObject(HttpStatus.OK, "Success", teacher);
            }
            return new ResponseObject(HttpStatus.FOUND, "Không đúng passowrd", null);
        }
        return new ResponseObject(HttpStatus.FOUND, "Tên đăng nhập hoặc mật khẩu không đúng", null);
    }
}
