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

    ResponseObject getTeachersOwnClass(String classId);

    ResponseObject searchTeachersOwnClassById(String classId, String keyword);

    ResponseObject getTeachersNotOwnClass(String classId);

    ResponseObject searchTeachersNotOwnClassById(String classId, String keyword);
}
