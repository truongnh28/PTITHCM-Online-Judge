package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.ResponseObject;
import ptithcm.onlinejudge.model.entity.Subject;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.model.request.SubjectClassRequest;
import ptithcm.onlinejudge.repository.SubjectClassRepository;
import ptithcm.onlinejudge.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;
@Service
public class SubjectClassManagementImpl implements SubjectClassManagement{
    @Autowired
    SubjectClassRepository SubjectClassRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Override
    public ResponseObject addSubjectClass(SubjectClassRequest subjectClassRequest) {
        if(SubjectClassRepository.existsById(subjectClassRequest.getSubjectClassId())) {
            return new ResponseObject(HttpStatus.FOUND, "Student is exist", "");
        }
        if(subjectClassRequestIsValid(subjectClassRequest)) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Request data is not valid", "");
        }
        String subjectClassId = subjectClassRequest.getSubjectClassId();
        String subjectClassName = subjectClassRequest.getSubjectClassName();
        Optional<Subject> subject = subjectRepository.findById(subjectClassRequest.getSubjectId());
        SubjectClass subjectClass = new SubjectClass(subjectClassId, subjectClassName, subject.get());
        SubjectClassRepository.save(subjectClass);
        return new ResponseObject(HttpStatus.OK, "Success", subjectClass);
    }

    @Override
    public ResponseObject editSubjectClass(SubjectClassRequest subjectClassRequest) {
        if(!SubjectClassRepository.existsById(subjectClassRequest.getSubjectClassId())) {
            return new ResponseObject(HttpStatus.FOUND, "Student is not exist", "");
        }
        if(!subjectClassRequestIsValid(subjectClassRequest)) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Request data is not valid", "");
        }
        Optional<SubjectClass> SubjectClass = SubjectClassRepository.findById(subjectClassRequest.getSubjectClassId());
        String SubjectClassName = subjectClassRequest.getSubjectClassName();
        SubjectClass.get().setSubjectClassName(SubjectClassName);
        SubjectClassRepository.save(SubjectClass.get());
        return new ResponseObject(HttpStatus.OK, "Success", SubjectClass);
    }

    @Override
    public ResponseObject getAllSubjectClass() {
        List<SubjectClass> SubjectClasses = SubjectClassRepository.findAll();
        return new ResponseObject(HttpStatus.OK, "Success", SubjectClasses);
    }
    private boolean subjectClassRequestIsValid (SubjectClassRequest subjectClassRequest) {
        boolean subjectClassNameIsValid = subjectClassRequest.getSubjectClassName().length() > 0;
        boolean subjectIdIsValid = subjectRepository.existsById(subjectClassRequest.getSubjectId());
        return subjectIdIsValid && subjectClassNameIsValid;
    }
}