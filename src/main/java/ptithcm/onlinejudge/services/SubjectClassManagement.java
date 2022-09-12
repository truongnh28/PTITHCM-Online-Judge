package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.request.SubjectClassRequest;
@Service
public interface SubjectClassManagement {
    ResponseObject addSubjectClass(SubjectClassRequest subjectClassRequest);
    ResponseObject editSubjectClass(SubjectClassRequest subjectClassRequest);
    ResponseObject getAllSubjectClass();
}
