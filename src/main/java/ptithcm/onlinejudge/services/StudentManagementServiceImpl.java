package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.HashMap;
import java.util.Map;
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
        Student newStudent = studentMapper.dtoToEntity(student);
        newStudent.setId(student.getStudentId().toUpperCase());
        newStudent.setStudentFirstName(student.getStudentFirstName().trim());
        newStudent.setStudentLastName(student.getStudentLastName().trim());
        newStudent.setStudentClass(student.getStudentClass().trim());
        newStudent.setPassword(SHA256Helper.hash("123456"));
        newStudent.setActive((byte) 1);
        newStudent.setCreateAt(Instant.now());
        newStudent.setUpdateAt(Instant.now());
        newStudent.setLastLogin(Instant.now());
        newStudent = studentRepository.save(newStudent);
        return new ResponseObject(HttpStatus.OK, "Success", newStudent);
    }

    @Override
    public ResponseObject editStudent(StudentDTO student) {
        String id = student.getStudentId();
        String newFName = student.getStudentFirstName().trim();
        String newLName = student.getStudentLastName().trim();
        String newClass = student.getStudentClass().trim();
        Student editedStudent = studentRepository.findById(id).get();
        editedStudent.setStudentClass(newClass);
        editedStudent.setStudentFirstName(newFName);
        editedStudent.setStudentLastName(newLName);
        editedStudent.setUpdateAt(Instant.now());
        editedStudent = studentRepository.save(editedStudent);
        return new ResponseObject(HttpStatus.OK, "Success", editedStudent);
    }

    @Override
    public ResponseObject updateStudent(String id, StudentDTO student) {
        Student updatedStudent = studentRepository.findById(id).get();
        updatedStudent.setStudentLastName(student.getStudentLastName().trim());
        updatedStudent.setStudentClass(student.getStudentClass().trim().toUpperCase());
        updatedStudent.setStudentFirstName(student.getStudentFirstName().trim());
        updatedStudent.setUpdateAt(Instant.now());
        updatedStudent = studentRepository.save(updatedStudent);
        return new ResponseObject(HttpStatus.OK, "Success", updatedStudent);
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
        Student lockedStudent = foundStudent.get();
        lockedStudent.setUpdateAt(Instant.now());
        lockedStudent.setActive((byte) 0);
        lockedStudent = studentRepository.save(lockedStudent);
        return new ResponseObject(HttpStatus.OK, "Success", lockedStudent);
    }

    @Override
    public ResponseObject unlockStudent(String studentId) {
        Optional<Student> foundStudent = studentRepository.findById(studentId);
        Student unlockedStudent = foundStudent.get();
        unlockedStudent.setUpdateAt(Instant.now());
        unlockedStudent.setActive((byte) 1);
        unlockedStudent = studentRepository.save(unlockedStudent);
        return new ResponseObject(HttpStatus.OK, "Success", unlockedStudent);
    }

    @Override
    public ResponseObject getAllStudent(int page) {
        if (page <= 0)
            page = 1;
        Page<Student> student = studentRepository.findAll(PageRequest.of(page - 1, 10));
        int totalPage = student.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", student.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchStudentById(String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<Student> student = studentRepository.searchStudentsById("%" + keyword + "%", PageRequest.of(page - 1, 10));
        int totalPage = student.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", student.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject getStudentsOfGroup(String groupId, int page) {
        if (page <= 0)
            page = 1;
        Page<Student> student = studentRepository.getStudentsOfGroup(groupId, PageRequest.of(page - 1, 10));
        int totalPage = student.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", student.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchStudentsOfGroupByKeyword(String groupId, String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<Student> student = studentRepository.searchStudentsOfGroupByKeyword(groupId, "%" + keyword + "%", PageRequest.of(page - 1, 10));
        int totalPage = student.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", student.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject getStudentsNotInClass(String classId, int page) {
        if (page <= 0)
            page = 1;
        Page<Student> student = studentRepository.getStudentsNotInClass(classId, PageRequest.of(page - 1, 10));
        int totalPage = student.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", student.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchStudentsNotInClassByKeyword(String classId, String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<Student> student = studentRepository.searchStudentsNotInClassByKeyword(classId, "%" + keyword + "%", PageRequest.of(page - 1, 10));
        int totalPage = student.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", student.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
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
        Student student = studentRepository.findById(studentId).get();
        student.setPassword(SHA256Helper.hash(password));
        student.setUpdateAt(Instant.now());
        student = studentRepository.save(student);
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
    public ResponseObject addStudentToClassGroup(AddStudentToGroupRequest addStudentToGroupRequest) {
        if (!addStudentToClassGroupIsValid(addStudentToGroupRequest)) {
            return new ResponseObject(HttpStatus.FOUND, "Request data is not valid", "");
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
}
