package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.model.response.ResponseObject;

public interface SubjectClassHasTeacherManagementService {
    ResponseObject addTeacherToClass(String teacherId, String classId);

    ResponseObject removeTeacherFromClass(String teacherId, String classId);
}
