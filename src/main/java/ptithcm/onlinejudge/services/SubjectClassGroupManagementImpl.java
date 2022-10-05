package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.mapper.SubjectClassGroupMapper;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.model.request.SubjectClassGroupRequest;
import ptithcm.onlinejudge.repository.SubjectClassGroupRepository;
import ptithcm.onlinejudge.repository.SubjectClassRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectClassGroupManagementImpl implements SubjectClassGroupManagement {
    @Autowired
    private SubjectClassGroupMapper subjectClassGroupMapper;
    @Autowired
    private SubjectClassGroupRepository subjectClassGroupRepository;
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Override
    public ResponseObject addGroupToClass(String classId, SubjectClassGroupDTO group) {
        String id = group.getGroupId();
        String name = group.getGroupName();
        Optional<SubjectClassGroup> foundGroup = subjectClassGroupRepository.findById(id);
        if (foundGroup.isPresent())
            return new ResponseObject(HttpStatus.FOUND, "Nhóm thực hành tồn tại", null);
        if (id.isEmpty() || name == null || name.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Mã nhóm và tên nhóm không được để trống", null);
        Optional<SubjectClass> foundClass = subjectClassRepository.findById(classId);
        if (foundClass.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Lớp không tồn tại", null);
        SubjectClassGroup newGroup = subjectClassGroupMapper.dtoToEntity(group);
        newGroup.setCreateAt(Instant.now());
        newGroup.setUpdateAt(Instant.now());
        newGroup.setHide((byte) 0);
        newGroup.setSubjectClass(foundClass.get());
        newGroup = subjectClassGroupRepository.save(newGroup);
        return new ResponseObject(HttpStatus.OK, "Success", newGroup);
    }

    @Override
    public ResponseObject editGroup(String groupId, SubjectClassGroupDTO group) {
        String name = group.getGroupName();
        if (name == null || name.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Tên nhóm không được để trống", null);
        Optional<SubjectClassGroup> foundOldGroup = subjectClassGroupRepository.findById(groupId);
        if (foundOldGroup.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Nhóm thực hành không tồn tại", null);
        SubjectClassGroup editedGroup = foundOldGroup.get();
        editedGroup.setSubjectClassGroupName(name);
        editedGroup.setUpdateAt(Instant.now());
        editedGroup = subjectClassGroupRepository.save(editedGroup);
        return new ResponseObject(HttpStatus.OK, "Success", editedGroup);
    }

    @Override
    public ResponseObject getAllSubjectClassGroup() {
        List<SubjectClassGroup> groups = subjectClassGroupRepository.findAll();
        return new ResponseObject(HttpStatus.OK, "Success", groups);
    }

    @Override
    public ResponseObject getAllGroupsOfClass(String subjectClassId) {
        List<SubjectClassGroup> groups = subjectClassGroupRepository.getSubjectClassGroupsBySubjectClass(subjectClassId);
        return new ResponseObject(HttpStatus.OK, "Success", groups);
    }

    @Override
    public ResponseObject lockGroup(String groupId) {
        Optional<SubjectClassGroup> foundGroup = subjectClassGroupRepository.findById(groupId);
        if (foundGroup.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Nhóm thực hành không tồn tại", null);
        SubjectClassGroup lockedGroup = foundGroup.get();
        lockedGroup.setUpdateAt(Instant.now());
        lockedGroup.setHide((byte) 1);
        lockedGroup = subjectClassGroupRepository.save(lockedGroup);
        return new ResponseObject(HttpStatus.OK, "Success", lockedGroup);
    }

    @Override
    public ResponseObject unlockGroup(String groupId) {
        Optional<SubjectClassGroup> foundGroup = subjectClassGroupRepository.findById(groupId);
        if (foundGroup.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Nhóm thực hành không tồn tại", null);
        SubjectClassGroup unlockedGroup = foundGroup.get();
        unlockedGroup.setUpdateAt(Instant.now());
        unlockedGroup.setHide((byte) 0);
        unlockedGroup = subjectClassGroupRepository.save(unlockedGroup);
        return new ResponseObject(HttpStatus.OK, "Success", unlockedGroup);
    }

    @Override
    public ResponseObject searchGroupByIdOrName(String classId, String keyword) {
        List<SubjectClassGroup> groups = subjectClassGroupRepository.searchGroupByIdOrName(classId, keyword);
        return new ResponseObject(HttpStatus.OK, "Success", groups);
    }

    @Override
    public ResponseObject getGroupById(String subjectClassGroupId) {
        Optional<SubjectClassGroup> foundSubjectClassGroup = subjectClassGroupRepository.findById(subjectClassGroupId);
        if (foundSubjectClassGroup.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Nhóm thực hành không tồn tại", null);
        return new ResponseObject(HttpStatus.OK, "Success", foundSubjectClassGroup.get());
    }

    private boolean subjectClassGroupRequestIsValid(SubjectClassGroupRequest subjectClassGroupRequest) {
        boolean subjectClassGroupNameIsValid = subjectClassGroupRequest.getSubjectClassGroupName().length() > 0;
        boolean subjectIdIsValid = subjectClassRepository.existsById(subjectClassGroupRequest.getSubjectClassId());
        return subjectIdIsValid && !subjectClassGroupNameIsValid;
    }
}
