package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.ResponseObject;
import ptithcm.onlinejudge.model.entity.*;
import ptithcm.onlinejudge.model.request.AddStudentToGroupRequest;
import ptithcm.onlinejudge.model.request.StudentRequest;
import ptithcm.onlinejudge.repository.StudentOfGroupRepository;
import ptithcm.onlinejudge.repository.StudentRepository;
import ptithcm.onlinejudge.repository.SubjectClassGroupRepository;
import ptithcm.onlinejudge.repository.SubjectClassRepository;

import java.util.List;
import java.util.Optional;
@Service
public class StudentManagementServiceImpl implements StudentManagementService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SubjectClassGroupRepository subjectClassGroupRepository;
    @Autowired
    StudentOfGroupRepository studentOfGroupRepository;
    @Autowired
    SubjectClassRepository subjectClassRepository;

    @Override
    public ResponseObject addStudent(StudentRequest studentRequest) {
        if(studentRepository.existsById(studentRequest.getStudentId())) {
            return new ResponseObject(HttpStatus.FOUND, "Student is exist", "");
        }
        if(!studentRequestIsValid(studentRequest)) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "request data is not valid", "");
        }
        String studentId = studentRequest.getStudentId();
        String fName = studentRequest.getFirstName();
        String lName = studentRequest.getLastName();
        String password = studentRequest.getPassword();
        Student student = new Student(studentId, password, fName, lName, (byte)1);
        studentRepository.save(student);
        return new ResponseObject(HttpStatus.OK, "Success", student);
    }

    @Override
    public ResponseObject editStudent(StudentRequest studentRequest) {
        if(!studentRepository.existsById(studentRequest.getStudentId())) {
            return new ResponseObject(HttpStatus.FOUND, "Student is not exist", "");
        }
        if(!studentRequestIsValid(studentRequest)) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "request data is not valid", "");
        }
        Optional<Student> student = studentRepository.findById(studentRequest.getStudentId());
        student.get().setStudentFirstName(studentRequest.getFirstName());
        student.get().setStudentLastName(studentRequest.getLastName());
        studentRepository.save(student.get());
        return new ResponseObject(HttpStatus.OK, "Success", student);
    }

    @Override
    public ResponseObject deleteStudent(String studentId) {
        if(!studentRepository.existsById(studentId)) {
            return new ResponseObject(HttpStatus.FOUND, "Student is not exist", "");
        }
        Optional<Student> problem = studentRepository.findById(studentId);
        studentRepository.deleteById(studentId);
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }

    @Override
    public ResponseObject changePassword(String studentId, String password) {
        if(!studentRepository.existsById(studentId)) {
            return new ResponseObject(HttpStatus.FOUND, "Student is not exist", "");
        }
        Optional<Student> student = studentRepository.findById(studentId);
        student.get().setPassword(password);
        studentRepository.save(student.get());
        return new ResponseObject(HttpStatus.OK, "Success", student);
    }

    @Override
    public ResponseObject changeActive(String studentId) {
        if(!studentRepository.existsById(studentId)) {
            return new ResponseObject(HttpStatus.FOUND, "Student is not exist", "");
        }
        Optional<Student> student = studentRepository.findById(studentId);
        student.get().setActive((byte) (student.get().getActive() == 0 ? 1 : 0));
        studentRepository.save(student.get());
        return new ResponseObject(HttpStatus.OK, "Success", student);
    }

    @Override
    public ResponseObject getAllStudent() {
        List<Student> students = studentRepository.findAll();
        return new ResponseObject(HttpStatus.OK, "Success", students);
    }

    @Override
    public ResponseObject addStudentToClassGroup(AddStudentToGroupRequest addStudentToGroupRequest) {
        if (!addStudentToClassGroupIsValid(addStudentToGroupRequest)) {
            return new ResponseObject(HttpStatus.FOUND, "Request data is not valid", "");
        }
        if(!checkStudentOfGroup(addStudentToGroupRequest)) {
            return new ResponseObject(HttpStatus.FOUND, "Student have class group", "");
        }
        Optional<Student> student = studentRepository.findById(addStudentToGroupRequest.getStudentId());
        Optional<SubjectClassGroup> subjectClassGroup = subjectClassGroupRepository.findById(addStudentToGroupRequest.getClassGroupId());
        StudentOfGroup studentOfGroup = new StudentOfGroup();
        studentOfGroup.setStudent(student.get());
        studentOfGroup.setSubjectClassGroup(subjectClassGroup.get());
        studentOfGroupRepository.save(studentOfGroup);
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }

    private boolean studentRequestIsValid(StudentRequest studentRequest) {
        boolean passwordIsValid = studentRequest.getPassword().length() > 0;
        boolean firstNameIsValid = studentRequest.getFirstName().length() > 0;
        boolean lastNameIsValid = studentRequest.getLastName().length() > 0;
        return passwordIsValid && firstNameIsValid && lastNameIsValid;
    }
    private boolean addStudentToClassGroupIsValid(AddStudentToGroupRequest addStudentToGroupRequest) {
        boolean studentIdIsValid = studentRepository.existsById(addStudentToGroupRequest.getStudentId());
        boolean classIdIsValid = subjectClassRepository.existsById(addStudentToGroupRequest.getClassId());
        boolean classGroupIdIsValid = subjectClassGroupRepository.existsById(addStudentToGroupRequest.getClassGroupId());
        return studentIdIsValid && classIdIsValid && classGroupIdIsValid;
    }
    private boolean checkStudentOfGroup (AddStudentToGroupRequest addStudentToGroupRequest) {
        List<StudentOfGroup> studentOfGroups = studentOfGroupRepository.getStudentOfGroupsByStudentId(addStudentToGroupRequest.getStudentId());
        List<SubjectClassGroup> subjectClassGroups = subjectClassGroupRepository.getSubjectClassGroupsBySubjectClass(addStudentToGroupRequest.getClassId());
        for(StudentOfGroup studentOfGroup:studentOfGroups) {
            for(SubjectClassGroup subjectClassGroup:subjectClassGroups) {
                if(studentOfGroup.getSubjectClassGroup() == subjectClassGroup) {
                    return false;
                }
            }
        }
        return true;
    }
}
