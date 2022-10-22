package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.SubjectDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface SubjectManagementService {
    ResponseObject addSubject(SubjectDTO subject);

    ResponseObject editSubject(SubjectDTO subject);

    ResponseObject searchByIdOrName(String keyword, int page);

    ResponseObject getSubjectById(String subjectId);

    ResponseObject lockSubject(String subjectId);

    ResponseObject unlockSubject(String subjectId);

    ResponseObject getAllSubject(int page);
}
