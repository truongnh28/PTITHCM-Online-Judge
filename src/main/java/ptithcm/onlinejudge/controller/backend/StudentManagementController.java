package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.onlinejudge.helper.SHA256Helper;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.request.StudentRequest;
import ptithcm.onlinejudge.repository.StudentRepository;

@RestController
@RequestMapping("/api/student")
public class StudentManagementController {
    @Autowired
    private StudentRepository studentRepository;
    @PostMapping("")
    public ResponseEntity<?> addStudent(@RequestBody StudentRequest studentRequest) {
        String studentId = studentRequest.getStudentId();
        String studentFirstName = studentRequest.getFirstName();
        String studentLastName = studentRequest.getLastName();
        String studentPassword = studentRequest.getPassword();
        Student student = new Student();
        student.setId(studentId);
        student.setStudentLastName(studentLastName);
        student.setStudentFirstName(studentFirstName);
        student.setPassword(SHA256Helper.hash(studentPassword));
        student.setActive((byte) 1);
        studentRepository.save(student);
        return ResponseEntity.ok("Student added");
    }
}
