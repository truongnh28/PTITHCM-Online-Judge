package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.helper.SHA256Helper;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.repository.TeacherRepository;

@RequestMapping("api/teacher")
@RestController
public class TeacherController {
    @Autowired
    private TeacherRepository teacherRepository;
    @PostMapping("")
    public ResponseEntity<?> addTeacher(@RequestBody TeacherDTO teacher) {
        Teacher entity = new Teacher();
        entity.setId(teacher.getTeacherId());
        entity.setTeacherFirstName(teacher.getTeacherFirstName());
        entity.setTeacherLastName(teacher.getTeacherLastName());
        SHA256Helper sha256 = new SHA256Helper();
        entity.setPassword(sha256.hash(teacher.getTeacherPassword()));
        entity.setActive(Byte.valueOf("1"));
        teacherRepository.save(entity);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Add successful");
    }
}
