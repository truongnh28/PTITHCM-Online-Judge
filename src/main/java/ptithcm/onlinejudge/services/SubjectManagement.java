package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.request.SubjectRequest;
@Service
public interface SubjectManagement {
    ResponseObject addSubject(SubjectRequest subjectRequest);
    ResponseObject editSubject(SubjectRequest subjectRequest);
    ResponseObject getAllSubject();
}
