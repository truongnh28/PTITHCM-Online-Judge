package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.model.request.StudentOfGroupRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

public interface StudentOfGroupManagement {
    ResponseObject addStudentToGroup(String studentId, String groupId);

    ResponseObject deleteStudentFromGroup(String studentId, String groupId);
    ResponseObject getStudentOfGroupByGroupId(String groupId);

    ResponseObject getStudentOfGroupByStudentId(String studentId);

    ResponseObject findStudentOfGroupByStudentIdAndSubjectClassId(String studentId, String subjectClassId);

    ResponseObject checkStudentInSubjectClass(String studentId, String subjectClassId);

    ResponseObject checkStudentInGroup(String studentId, String subjectClassGroupId);
}
