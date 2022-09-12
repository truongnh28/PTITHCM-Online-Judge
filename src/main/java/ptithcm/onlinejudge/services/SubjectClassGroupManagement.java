package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.request.SubjectClassGroupRequest;
@Service
public interface SubjectClassGroupManagement {
    ResponseObject addSubjectClassGroup(SubjectClassGroupRequest subjectClassGroupRequest);
    ResponseObject editSubjectClassGroup(SubjectClassGroupRequest subjectClassGroupRequest);
    ResponseObject getAllSubjectClassGroup();
    ResponseObject getAllSubjectClassGroupOfSubjectClass(String subjectClassId);
}
