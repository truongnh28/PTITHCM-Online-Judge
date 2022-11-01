package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.entity.StudentOfGroup;
import ptithcm.onlinejudge.model.entity.StudentOfGroupId;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.model.request.StudentOfGroupRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.StudentOfGroupRepository;
import ptithcm.onlinejudge.repository.StudentRepository;
import ptithcm.onlinejudge.repository.SubjectClassGroupRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentOfGroupManagementImpl implements StudentOfGroupManagement {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectClassGroupRepository subjectClassGroupRepository;
    @Autowired
    private StudentOfGroupRepository studentOfGroupRepository;

    @Override
    public ResponseObject addStudentToGroup(String studentId, String groupId) {
        Optional<Student> foundStudent = studentRepository.findById(studentId);
        Optional<SubjectClassGroup> foundGroup = subjectClassGroupRepository.findById(groupId);
        if (foundStudent.isEmpty() || foundGroup.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tồn tại cặp mã sinh viên và nhóm thực hành", null);
        Student student = foundStudent.get();
        SubjectClassGroup group = foundGroup.get();
        StudentOfGroup studentOfGroup = new StudentOfGroup();
        studentOfGroup.setStudent(student);
        studentOfGroup.setSubjectClassGroup(group);
        studentOfGroup.setId(new StudentOfGroupId(studentId, groupId));
        studentOfGroup = studentOfGroupRepository.save(studentOfGroup);
        return new ResponseObject(HttpStatus.OK, "Success", studentOfGroup);
    }

    @Override
    public ResponseObject deleteStudentFromGroup(String studentId, String groupId) {
        StudentOfGroupId id = new StudentOfGroupId(studentId, groupId);
        studentOfGroupRepository.deleteById(id);
        return new ResponseObject(HttpStatus.OK, "Success", null);
    }

    @Override
    public ResponseObject getStudentOfGroupByGroupId(String groupId) {
        List<StudentOfGroup> studentOfGroups = studentOfGroupRepository.getStudentOfGroupsByGroupId(groupId);
        return new ResponseObject(HttpStatus.OK, "Success", studentOfGroups);
    }

    @Override
    public ResponseObject getStudentOfGroupByStudentId(String studentId) {
        List<StudentOfGroup> studentOfGroups = studentOfGroupRepository.getStudentOfGroupsByStudentId(studentId);
        return new ResponseObject(HttpStatus.OK, "Success", studentOfGroups);
    }

    @Override
    public ResponseObject findStudentOfGroupByStudentIdAndSubjectClassId(String studentId, String subjectClassId) {
        Optional<StudentOfGroup> foundStudentOfGroup = studentOfGroupRepository.findByStudentIdAndSubjectClassId(studentId, subjectClassId);
        if (foundStudentOfGroup.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Student of group not exist", "");
        return new ResponseObject(HttpStatus.OK, "Success", foundStudentOfGroup.get());
    }

    @Override
    public ResponseObject checkStudentInSubjectClass(String studentId, String subjectClassId) {
        Optional<StudentOfGroup> foundStudentOfGroup = studentOfGroupRepository.findByStudentIdAndSubjectClassId(studentId, subjectClassId);
        if (foundStudentOfGroup.isEmpty())
            return new ResponseObject(HttpStatus.OK, "Student not exist", false);
        return new ResponseObject(HttpStatus.OK, "Student exist in class", true);
    }

    @Override
    public ResponseObject checkStudentInGroup(String studentId, String subjectClassGroupId) {
        Optional<StudentOfGroup> foundStudentOfGroup = studentOfGroupRepository.findByStudentIdAndSubjectClassGroupId(studentId, subjectClassGroupId);
        if (foundStudentOfGroup.isEmpty())
            return new ResponseObject(HttpStatus.OK, "Student not exist", false);
        return new ResponseObject(HttpStatus.OK, "Student exist in class", true);
    }

    private boolean checkRequestAddingIsValid(StudentOfGroupRequest studentOfGroupRequest) {
        String studentId = studentOfGroupRequest.getStudentId();
        String subjectClassGroupId = studentOfGroupRequest.getSubjectClassGroupId();
        Optional<Student> foundStudent = studentRepository.findById(studentId);
        Optional<SubjectClassGroup> foundSubjectClassGroup = subjectClassGroupRepository.findById(subjectClassGroupId);
        return foundStudent.isPresent() && foundSubjectClassGroup.isPresent();
    }
}
