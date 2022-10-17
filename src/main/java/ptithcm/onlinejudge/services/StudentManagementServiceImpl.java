package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.helper.SHA256Helper;
import ptithcm.onlinejudge.mapper.StudentMapper;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.entity.*;
import ptithcm.onlinejudge.model.request.AddStudentToGroupRequest;
import ptithcm.onlinejudge.model.request.StudentRequest;
import ptithcm.onlinejudge.repository.StudentOfGroupRepository;
import ptithcm.onlinejudge.repository.StudentRepository;
import ptithcm.onlinejudge.repository.SubjectClassGroupRepository;
import ptithcm.onlinejudge.repository.SubjectClassRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
@Service
public class StudentManagementServiceImpl implements StudentManagementService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectClassGroupRepository subjectClassGroupRepository;
    @Autowired
    private StudentOfGroupRepository studentOfGroupRepository;
    @Autowired
    private SubjectClassRepository subjectClassRepository;

    @Override
    public ResponseObject addStudent(StudentDTO student) {
        String id = student.getStudentId();
        String fName = student.getStudentFirstName();
        String lName = student.getStudentLastName();
        if(studentRepository.existsById(id))
            return new ResponseObject(HttpStatus.FOUND, "Sinh viên tồn tại", null);
        if (fName == null || fName.isEmpty() || lName == null || lName.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Họ và tên sinh viên không được trống", null);
        Student newStudent = studentMapper.dtoToEntity(student);
        newStudent.setPassword(SHA256Helper.hash("123456"));
        newStudent.setActive((byte) 1);
        newStudent.setCreateAt(Instant.now());
        newStudent.setUpdateAt(Instant.now());
        newStudent.setLastLogin(Instant.now());
        newStudent = studentRepository.save(newStudent);
        return new ResponseObject(HttpStatus.OK, "Success", newStudent);
    }

    @Override
    public ResponseObject editStudent(String studentId, StudentDTO student) {
        String newFName = student.getStudentFirstName();
        String newLName = student.getStudentLastName();
        String newClass = student.getStudentClass();
        if (newFName == null || newFName.isEmpty() || newLName == null || newLName.isEmpty() || newClass == null || newClass.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Họ, tên và lớp sinh viên không được để trống", null);
        Optional<Student> foundStudent = studentRepository.findById(studentId);
        if (foundStudent.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Sinh viên cũ không tồn tại", null);
        Student editedStudent = foundStudent.get();
        editedStudent.setStudentClass(newClass);
        editedStudent.setStudentFirstName(newFName);
        editedStudent.setStudentLastName(newLName);
        editedStudent.setUpdateAt(Instant.now());
        editedStudent = studentRepository.save(editedStudent);
        return new ResponseObject(HttpStatus.OK, "Success", editedStudent);
    }

    @Override
    public ResponseObject resetPassword(String studentId) {
        Optional<Student> foundStudent = studentRepository.findById(studentId);
        if (foundStudent.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Sinh viên không tồn tại", null);
        Student updatedStudent = foundStudent.get();
        updatedStudent.setUpdateAt(Instant.now());
        updatedStudent.setPassword(SHA256Helper.hash("123456"));
        updatedStudent = studentRepository.save(updatedStudent);
        return new ResponseObject(HttpStatus.OK, "Success", updatedStudent);
    }

    @Override
    public ResponseObject lockStudent(String studentId) {
        Optional<Student> foundStudent = studentRepository.findById(studentId);
        if (foundStudent.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Sinh viên không tồn tại", null);
        Student lockedStudent = foundStudent.get();
        lockedStudent.setUpdateAt(Instant.now());
        lockedStudent.setActive((byte) 0);
        lockedStudent = studentRepository.save(lockedStudent);
        return new ResponseObject(HttpStatus.OK, "Success", lockedStudent);
    }

    @Override
    public ResponseObject unlockStudent(String studentId) {
        Optional<Student> foundStudent = studentRepository.findById(studentId);
        if (foundStudent.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Sinh viên không tồn tại", null);
        Student unlockedStudent = foundStudent.get();
        unlockedStudent.setUpdateAt(Instant.now());
        unlockedStudent.setActive((byte) 1);
        unlockedStudent = studentRepository.save(unlockedStudent);
        return new ResponseObject(HttpStatus.OK, "Success", unlockedStudent);
    }

    @Override
    public ResponseObject getStudentsOfGroup(String groupId) {
        List<Student> students = studentRepository.getStudentsOfGroup(groupId);
        return new ResponseObject(HttpStatus.OK, "Success", students);
    }

    @Override
    public ResponseObject searchStudentsOfGroupById(String groupId, String studentId) {
        List<Student> students = studentRepository.searchStudentsInGroupById(groupId, studentId);
        return new ResponseObject(HttpStatus.OK, "Success", students);
    }

    @Override
    public ResponseObject getStudentsNotInClass(String classId) {
        List<Student> students = studentRepository.getStudentsNotInClass(classId);
        return new ResponseObject(HttpStatus.OK, "Success", students);
    }

    @Override
    public ResponseObject searchStudentsNotInClassById(String classId, String keyword) {
        List<Student> students = studentRepository.searchStudentsNotInClassById(classId, keyword);
        return new ResponseObject(HttpStatus.OK, "Success", students);
    }

    @Override
    public ResponseObject searchStudentByIdLike(String studentId) {
        List<Student> students = studentRepository.searchStudentByIdLikeIgnoreCase(studentId);
        return new ResponseObject(HttpStatus.OK, "Success", students);
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
    public ResponseObject getStudentById(String studentId) {
        Optional<Student> foundStudent = studentRepository.findById(studentId);
        if (foundStudent.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy sinh viên", null);
        return new ResponseObject(HttpStatus.OK, "Success", foundStudent.get());
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
