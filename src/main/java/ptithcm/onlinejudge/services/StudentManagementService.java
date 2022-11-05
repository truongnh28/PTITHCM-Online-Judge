package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.request.AddStudentToGroupRequest;

@Service
public interface StudentManagementService {
    ResponseObject addStudent(StudentDTO student);

    ResponseObject editStudent(StudentDTO student);

    ResponseObject updateStudent(String id, StudentDTO student);

    ResponseObject resetPassword(String studentId);

    ResponseObject lockStudent(String studentId);

    ResponseObject unlockStudent(String studentId);

    ResponseObject getAllStudent(int page);

    ResponseObject searchStudentById(String keyword, int page);

    ResponseObject getStudentsOfGroup(String groupId, int page);

    ResponseObject searchStudentsOfGroupByKeyword(String groupId, String keyword, int page);

    ResponseObject getStudentsNotInClass(String classId, int page);

    ResponseObject searchStudentsNotInClassByKeyword(String classId, String keyword, int page);

    ResponseObject deleteStudent(String studentId);

    ResponseObject changePassword(String studentId, String password);

    ResponseObject changeActive(String studentId);

    ResponseObject getStudentById(String studentId);

    ResponseObject addStudentToClassGroup(AddStudentToGroupRequest addStudentToGroupRequest);
}
