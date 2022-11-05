package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.dto.PasswordChangeDTO;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;

public interface TeacherManagementService {

    ResponseObject getAllTeachersExceptAdmin(int page);

    ResponseObject searchTeachersByKeyWordExceptAdmin(String keyword, int page);

    ResponseObject addTeacher(TeacherDTO teacherDTO);

    ResponseObject editTeacher(TeacherDTO teacherDTO);

    ResponseObject updateTeacher(String id, TeacherDTO teacherDTO);

    ResponseObject changePassword(String id, PasswordChangeDTO passwordChange);

    ResponseObject lockTeacher(String teacherId);

    ResponseObject unlockTeacher(String teacherId);

    ResponseObject resetPasswordTeacher(String teacherId);

    ResponseObject getTeacherById(String teacherId);

    ResponseObject getTeachersOwnClass(String classId);

    ResponseObject searchTeachersOwnClassById(String classId, String keyword);

    ResponseObject getTeachersNotOwnClass(String classId, int page);

    ResponseObject searchTeachersNotOwnClassByKeyword(String classId, String keyword, int page);
}
