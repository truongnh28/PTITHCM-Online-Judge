package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;

public interface TeacherManagementService {

    ResponseObject getAllTeachersExceptAdmin();

    ResponseObject searchTeachersByKeyWordExceptAdmin(String keyword);

    ResponseObject addTeacher(TeacherDTO teacherDTO);

    ResponseObject editTeacher(String teacherId, TeacherDTO teacherDTO);

    ResponseObject lockTeacher(String teacherId);

    ResponseObject unlockTeacher(String teacherId);

    ResponseObject resetPasswordTeacher(String teacherId);

    ResponseObject getTeacherById(String teacherId);
}
