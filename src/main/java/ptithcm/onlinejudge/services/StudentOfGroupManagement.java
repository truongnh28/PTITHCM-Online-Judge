package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.model.request.StudentOfGroupRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

public interface StudentOfGroupManagement {
    ResponseObject addStudentOfGroup(StudentOfGroupRequest studentOfGroupRequest);

    ResponseObject deleteStudentOfGroup(StudentOfGroupRequest studentOfGroupRequest);
    ResponseObject getStudentOfGroupByGroupId(String groupId);

    ResponseObject getStudentOfGroupByStudentId(String studentId);

    ResponseObject findStudentOfGroupByStudentIdAndSubjectClassId(String studentId, String subjectClassId);
}
