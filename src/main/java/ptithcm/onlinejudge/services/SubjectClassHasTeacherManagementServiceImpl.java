package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.model.entity.SubjectClassHasTeacher;
import ptithcm.onlinejudge.model.entity.SubjectClassHasTeacherId;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.SubjectClassHasTeacherRepository;
import ptithcm.onlinejudge.repository.SubjectClassRepository;
import ptithcm.onlinejudge.repository.TeacherRepository;

import java.util.Optional;

@Service
public class SubjectClassHasTeacherManagementServiceImpl implements SubjectClassHasTeacherManagementService {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Autowired
    private SubjectClassHasTeacherRepository subjectClassHasTeacherRepository;

    @Override
    public ResponseObject addTeacherToClass(String teacherId, String classId) {
        Optional<Teacher> foundTeacher = teacherRepository.findById(teacherId);
        Optional<SubjectClass> foundClass = subjectClassRepository.findById(classId);
        if (foundTeacher.isEmpty() || foundClass.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Không tìm thấy giáo viên và lớp", null);
        Teacher teacher = foundTeacher.get();
        SubjectClass subjectClass = foundClass.get();
        SubjectClassHasTeacherId id = new SubjectClassHasTeacherId(classId, teacherId);
        SubjectClassHasTeacher subjectClassHasTeacher = new SubjectClassHasTeacher();
        subjectClassHasTeacher.setSubjectClass(subjectClass);
        subjectClassHasTeacher.setTeacher(teacher);
        subjectClassHasTeacher.setId(id);
        subjectClassHasTeacher = subjectClassHasTeacherRepository.save(subjectClassHasTeacher);
        return new ResponseObject(HttpStatus.OK, "Success", subjectClassHasTeacher);
    }

    @Override
    public ResponseObject removeTeacherFromClass(String teacherId, String classId) {
        SubjectClassHasTeacherId id = new SubjectClassHasTeacherId(classId, teacherId);
        subjectClassHasTeacherRepository.deleteById(id);
        return new ResponseObject(HttpStatus.OK, "Success", null);
    }
}
