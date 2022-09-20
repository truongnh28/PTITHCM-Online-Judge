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
public class StudentOfGroupManagementImpl implements StudentOfGroupManagement{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectClassGroupRepository subjectClassGroupRepository;
    @Autowired
    private StudentOfGroupRepository studentOfGroupRepository;

    @Override
    public ResponseObject addStudentOfGroup(StudentOfGroupRequest studentOfGroupRequest) {
        if (!checkRequestAddingIsValid(studentOfGroupRequest))
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Request data is not valid", "");
        String studentId = studentOfGroupRequest.getStudentId();
        String subjectClassGroupId = studentOfGroupRequest.getSubjectClassGroupId();

        Student student = studentRepository.findById(studentId).get();
        SubjectClassGroup subjectClassGroup = subjectClassGroupRepository.findById(subjectClassGroupId).get();

        StudentOfGroup studentOfGroup = new StudentOfGroup();
        studentOfGroup.setStudent(student);
        studentOfGroup.setSubjectClassGroup(subjectClassGroup);
        StudentOfGroupId studentOfGroupId = new StudentOfGroupId();
        studentOfGroupId.setStudentId(studentId);
        studentOfGroupId.setSubjectClassGroupId(subjectClassGroupId);
        studentOfGroup.setId(studentOfGroupId);

        studentOfGroupRepository.save(studentOfGroup);
        return new ResponseObject(HttpStatus.OK, "Success", studentOfGroup);
    }

    @Override
    public ResponseObject deleteStudentOfGroup(StudentOfGroupRequest studentOfGroupRequest) {
        if (!checkRequestAddingIsValid(studentOfGroupRequest))
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Request data is not valid", "");
        String studentId = studentOfGroupRequest.getStudentId();
        String subjectClassGroupId = studentOfGroupRequest.getSubjectClassGroupId();

        StudentOfGroupId studentOfGroupId = new StudentOfGroupId();
        studentOfGroupId.setStudentId(studentId);
        studentOfGroupId.setSubjectClassGroupId(subjectClassGroupId);

        studentOfGroupRepository.deleteById(studentOfGroupId);
        return new ResponseObject(HttpStatus.OK, "Success", "");
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

    private boolean checkRequestAddingIsValid(StudentOfGroupRequest studentOfGroupRequest) {
        String studentId = studentOfGroupRequest.getStudentId();
        String subjectClassGroupId = studentOfGroupRequest.getSubjectClassGroupId();
        Optional<Student> foundStudent = studentRepository.findById(studentId);
        Optional<SubjectClassGroup> foundSubjectClassGroup = subjectClassGroupRepository.findById(subjectClassGroupId);
        return foundStudent.isPresent() && foundSubjectClassGroup.isPresent();
    }
}
