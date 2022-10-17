package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.request.AddStudentToGroupRequest;

@Service
public interface StudentManagementService {
    ResponseObject addStudent(StudentDTO student);

    ResponseObject editStudent(String studentId, StudentDTO student);

    ResponseObject resetPassword(String studentId);

    ResponseObject lockStudent(String studentId);

    ResponseObject unlockStudent(String studentId);

    ResponseObject getStudentsOfGroup(String groupId);

    ResponseObject searchStudentsOfGroupById(String groupId, String studentId);

    ResponseObject getStudentsNotInClass(String classId);

    ResponseObject searchStudentsNotInClassById(String classId, String keyword);

    ResponseObject searchStudentByIdLike(String studentId);

    ResponseObject deleteStudent(String studentId);

    ResponseObject changePassword(String studentId, String password);

    ResponseObject changeActive(String studentId);

    ResponseObject getStudentById(String studentId);

    ResponseObject getAllStudent();

    ResponseObject addStudentToClassGroup(AddStudentToGroupRequest addStudentToGroupRequest);
}
