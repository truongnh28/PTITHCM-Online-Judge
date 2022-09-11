package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.ResponseObject;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.entity.Subject;
import ptithcm.onlinejudge.model.request.SubjectRequest;
import ptithcm.onlinejudge.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;
@Service
public class SubjectManagementImpl implements SubjectManagement{
    @Autowired
    SubjectRepository subjectRepository;
    @Override
    public ResponseObject addSubject(SubjectRequest subjectRequest) {
        if(subjectRepository.existsById(subjectRequest.getSubjectId())) {
            return new ResponseObject(HttpStatus.FOUND, "Student is exist", "");
        }
        if(!(subjectRequest.getSubjectName().length() > 0)) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "request data is not valid", "");
        }
        String subjectId = subjectRequest.getSubjectId();
        String subjectName = subjectRequest.getSubjectName();
        Subject subject = new Subject(subjectId, subjectName);
        subjectRepository.save(subject);
        return new ResponseObject(HttpStatus.OK, "Success", subject);
    }

    @Override
    public ResponseObject editSubject(SubjectRequest subjectRequest) {
        if(!subjectRepository.existsById(subjectRequest.getSubjectId())) {
            return new ResponseObject(HttpStatus.FOUND, "Student is not exist", "");
        }
        if(!(subjectRequest.getSubjectName().length() > 0)) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "request data is not valid", "");
        }
        Optional<Subject> subject = subjectRepository.findById(subjectRequest.getSubjectId());
        String subjectName = subjectRequest.getSubjectName();
        subject.get().setSubjectName(subjectName);
        subjectRepository.save(subject.get());
        return new ResponseObject(HttpStatus.OK, "Success", subject);
    }

    @Override
    public ResponseObject getAllSubject() {
        List<Subject> subjects = subjectRepository.findAll();
        return new ResponseObject(HttpStatus.OK, "Success", subjects);
    }
}
