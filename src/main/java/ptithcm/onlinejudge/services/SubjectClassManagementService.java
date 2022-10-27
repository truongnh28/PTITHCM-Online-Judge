package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.SubjectClassDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface SubjectClassManagementService {
    ResponseObject add(String subjectId, SubjectClassDTO subjectClass);

    ResponseObject edit(SubjectClassDTO subjectClass);

    ResponseObject getAllClassesOfSubject(String subjectId, int page);

    ResponseObject searchClassesOfSubjectByKeyword(String subjectId, String keyword, int page);

    ResponseObject lockClass(String classId);

    ResponseObject unlockClass(String classId);

    ResponseObject getClassById(String classId);

    ResponseObject getClassesTeacherOwnActive(String teacherId, int page);

    ResponseObject searchClassesTeacherOwnActive(String teacherId, String keyword, int page);
}
