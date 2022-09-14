package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.response.ResponseObject;
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
    SubjectClassRepository subjectClassRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Override
    public ResponseObject addSubjectClass(SubjectClassRequest subjectClassRequest) {
        if(subjectClassRepository.existsById(subjectClassRequest.getSubjectClassId())) {
            return new ResponseObject(HttpStatus.FOUND, "Subject class is exist", "");
        }
        if(subjectClassRequestIsValidAddNew(subjectClassRequest)) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Request data is not valid", "");
        }
        String subjectClassId = subjectClassRequest.getSubjectClassId();
        String subjectClassName = subjectClassRequest.getSubjectClassName();
        Optional<Subject> subject = subjectRepository.findById(subjectClassRequest.getSubjectId());
        SubjectClass subjectClass = new SubjectClass(subjectClassId, subjectClassName, subject.get());
        subjectClassRepository.save(subjectClass);
        return new ResponseObject(HttpStatus.OK, "Success", subjectClass);
    }

    @Override
    public ResponseObject editSubjectClass(SubjectClassRequest subjectClassRequest) {
        if(!subjectClassRepository.existsById(subjectClassRequest.getSubjectClassId())) {
            return new ResponseObject(HttpStatus.FOUND, "Subject class is not exist", "");
        }
        if(subjectClassRequest.getSubjectClassName().isEmpty()) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Request data is not valid", "");
        }
        Optional<SubjectClass> subjectClass = subjectClassRepository.findById(subjectClassRequest.getSubjectClassId());
        String SubjectClassName = subjectClassRequest.getSubjectClassName();
        subjectClass.get().setSubjectClassName(SubjectClassName);
        subjectClassRepository.save(subjectClass.get());
        return new ResponseObject(HttpStatus.OK, "Success", subjectClass);
    }

    @Override
    public ResponseObject getAllSubjectClass() {
        List<SubjectClass> SubjectClasses = subjectClassRepository.findAll();
        return new ResponseObject(HttpStatus.OK, "Success", SubjectClasses);
    }

    @Override
    public ResponseObject getAllSubjectClassBySubjectId(String subjectId) {
        List<SubjectClass> subjectClasses = subjectClassRepository.getSubjectClassBySubjectId(subjectId);
        return new ResponseObject(HttpStatus.OK, "Success", subjectClasses);
    }

    @Override
    public ResponseObject findSubjectClassBySubjectClassId(String subjectClassId) {
        Optional<SubjectClass> foundSubjectClass = subjectClassRepository.findById(subjectClassId);
        if (foundSubjectClass.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Subject class is not exist", "");
        return new ResponseObject(HttpStatus.OK, "Success", foundSubjectClass.get());
    }

    private boolean subjectClassRequestIsValidAddNew(SubjectClassRequest subjectClassRequest) {
        boolean subjectClassNameIsValid = subjectClassRequest.getSubjectClassName().length() > 0;
        boolean subjectIdIsValid = subjectRepository.existsById(subjectClassRequest.getSubjectId());
        return !subjectIdIsValid && subjectClassNameIsValid;
    }
}
