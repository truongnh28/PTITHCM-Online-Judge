package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.SubjectClassDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface SubjectClassManagementService {
    ResponseObject addSubjectClass(String subjectId, SubjectClassDTO subjectClass);

    ResponseObject editSubjectClass(String classId, SubjectClassDTO subjectClass);

    ResponseObject getAllClass();

    ResponseObject getAllClassesBySubjectId(String subjectId);

    ResponseObject searchClassesByIdOrName(String subjectId, String keyword);

    ResponseObject lockClass(String classId);

    ResponseObject unlockClass(String classId);

    ResponseObject getClassById(String classId);
}
