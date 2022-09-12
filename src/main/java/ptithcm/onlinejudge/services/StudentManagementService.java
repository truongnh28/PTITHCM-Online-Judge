package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.request.AddStudentToGroupRequest;
import ptithcm.onlinejudge.model.request.StudentRequest;
@Service
public interface StudentManagementService {
    ResponseObject addStudent(StudentRequest studentRequest);
    ResponseObject editStudent(StudentRequest studentRequest);
    ResponseObject deleteStudent(String studentId);
    ResponseObject changePassword(String studentId, String password);
    ResponseObject changeActive(String studentId);
    ResponseObject getAllStudent();
    ResponseObject addStudentToClassGroup(AddStudentToGroupRequest addStudentToGroupRequest);
}
