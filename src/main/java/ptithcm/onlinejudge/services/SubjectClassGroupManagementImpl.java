package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.model.request.SubjectClassGroupRequest;
import ptithcm.onlinejudge.repository.SubjectClassGroupRepository;
import ptithcm.onlinejudge.repository.SubjectClassRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectClassGroupManagementImpl implements SubjectClassGroupManagement {
    @Autowired
    SubjectClassGroupRepository subjectClassGroupRepository;
    @Autowired
    SubjectClassRepository subjectClassRepository;
    @Override
    public ResponseObject addSubjectClassGroup(SubjectClassGroupRequest subjectClassGroupRequest) {
        if(subjectClassGroupRepository.existsById(subjectClassGroupRequest.getSubjectClassGroupId())) {
            return new ResponseObject(HttpStatus.FOUND, "Student is exist", "");
        }
        if(subjectClassGroupRequestIsValid(subjectClassGroupRequest)) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Request data is not valid", "");
        }
        String subjectClassGroupId = subjectClassGroupRequest.getSubjectClassGroupId();
        String subjectClassGroupName = subjectClassGroupRequest.getSubjectClassGroupName();
        Optional<SubjectClass> subjectClass = subjectClassRepository.findById(subjectClassGroupRequest.getSubjectClassId());
        SubjectClassGroup SubjectClassGroup = new SubjectClassGroup(subjectClassGroupId, subjectClassGroupName, subjectClass.get());
        subjectClassGroupRepository.save(SubjectClassGroup);
        return new ResponseObject(HttpStatus.OK, "Success", SubjectClassGroup);
    }

    @Override
    public ResponseObject editSubjectClassGroup(SubjectClassGroupRequest subjectClassGroupRequest) {
        if(!subjectClassGroupRepository.existsById(subjectClassGroupRequest.getSubjectClassGroupId())) {
            return new ResponseObject(HttpStatus.FOUND, "Student is not exist", "");
        }
        if(subjectClassGroupRequest.getSubjectClassGroupName().isEmpty()) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Request data is not valid", "");
        }
        Optional<SubjectClassGroup> subjectClassGroup = subjectClassGroupRepository.findById(subjectClassGroupRequest.getSubjectClassGroupId());
        String subjectClassGroupName = subjectClassGroupRequest.getSubjectClassGroupName();
        subjectClassGroup.get().setSubjectClassGroupName(subjectClassGroupName);
        subjectClassGroupRepository.save(subjectClassGroup.get());
        return new ResponseObject(HttpStatus.OK, "Success", subjectClassGroup);
    }

    @Override
    public ResponseObject getAllSubjectClassGroup() {
        List<SubjectClassGroup> SubjectClassGroups = subjectClassGroupRepository.findAll();
        return new ResponseObject(HttpStatus.OK, "Success", SubjectClassGroups);
    }

    @Override
    public ResponseObject getAllSubjectClassGroupOfSubjectClass(String subjectClassId) {
        List<SubjectClassGroup> SubjectClassGroups = subjectClassGroupRepository.getSubjectClassGroupsBySubjectClass(subjectClassId);
        return new ResponseObject(HttpStatus.OK, "Success", SubjectClassGroups);
    }

    @Override
    public ResponseObject getSubjectClassGroupById(String subjectClassGroupId) {
        Optional<SubjectClassGroup> foundSubjectClassGroup = subjectClassGroupRepository.findById(subjectClassGroupId);
        if (foundSubjectClassGroup.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Subject class group not exist", "");
        return new ResponseObject(HttpStatus.OK, "Success", foundSubjectClassGroup.get());
    }

    private boolean subjectClassGroupRequestIsValid(SubjectClassGroupRequest subjectClassGroupRequest) {
        boolean subjectClassGroupNameIsValid = subjectClassGroupRequest.getSubjectClassGroupName().length() > 0;
        boolean subjectIdIsValid = subjectClassRepository.existsById(subjectClassGroupRequest.getSubjectClassId());
        return subjectIdIsValid && !subjectClassGroupNameIsValid;
    }
}
