package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.mapper.SubjectClassGroupMapper;
import ptithcm.onlinejudge.model.entity.Student;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.model.request.SubjectClassGroupRequest;
import ptithcm.onlinejudge.repository.StudentRepository;
import ptithcm.onlinejudge.repository.SubjectClassGroupRepository;
import ptithcm.onlinejudge.repository.SubjectClassRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SubjectClassGroupManagementServiceImpl implements SubjectClassGroupManagementService {
    @Autowired
    private SubjectClassGroupMapper subjectClassGroupMapper;
    @Autowired
    private SubjectClassGroupRepository subjectClassGroupRepository;
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Override
    public ResponseObject addGroupToClass(String classId, SubjectClassGroupDTO group) {
        String id = group.getGroupId().trim().toUpperCase();
        String name = group.getGroupName().trim();
        SubjectClassGroup newGroup = subjectClassGroupMapper.dtoToEntity(group);
        newGroup.setId(id);
        newGroup.setSubjectClassGroupName(name);
        newGroup.setCreateAt(Instant.now());
        newGroup.setUpdateAt(Instant.now());
        newGroup.setHide((byte) 0);
        newGroup.setSubjectClass(subjectClassRepository.findById(classId).get());
        newGroup = subjectClassGroupRepository.save(newGroup);
        return new ResponseObject(HttpStatus.OK, "Success", newGroup);
    }

    @Override
    public ResponseObject editGroup(SubjectClassGroupDTO group) {
        String name = group.getGroupName().trim();
        SubjectClassGroup editedGroup = subjectClassGroupRepository.findById(group.getGroupId()).get();
        editedGroup.setSubjectClassGroupName(name);
        editedGroup.setUpdateAt(Instant.now());
        editedGroup = subjectClassGroupRepository.save(editedGroup);
        return new ResponseObject(HttpStatus.OK, "Success", editedGroup);
    }

    @Override
    public ResponseObject getGroupsOfClassActive(String classId, int page) {
        if (page <= 0)
            page = 1;
        Page<SubjectClassGroup> subjectClassGroups = subjectClassGroupRepository.getGroupsOfClassActive(classId, PageRequest.of(page - 1, 10));
        int totalPage = subjectClassGroups.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", subjectClassGroups.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchGroupOfClassActive(String classId, String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<SubjectClassGroup> subjectClassGroups = subjectClassGroupRepository.searchGroupsOfClassByKeywordActive(classId, keyword, PageRequest.of(page - 1, 10));
        int totalPage = subjectClassGroups.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", subjectClassGroups.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject getAllGroupsOfClass(String subjectClassId, int page) {
        if (page <= 0)
            page = 1;
        Page<SubjectClassGroup> subjectClassGroups = subjectClassGroupRepository.getGroupsOfClass(subjectClassId, PageRequest.of(page - 1, 10));
        int totalPage = subjectClassGroups.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", subjectClassGroups.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchGroupOfClassByKeyword(String classId, String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<SubjectClassGroup> subjectClassGroups = subjectClassGroupRepository.searchGroupsOfClassByKeyword(classId, "%" + keyword + "%", PageRequest.of(page - 1, 10));
        int totalPage = subjectClassGroups.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", subjectClassGroups.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject lockGroup(String groupId) {
        SubjectClassGroup lockedGroup = subjectClassGroupRepository.findById(groupId).get();
        lockedGroup.setUpdateAt(Instant.now());
        lockedGroup.setHide((byte) 1);
        lockedGroup = subjectClassGroupRepository.save(lockedGroup);
        return new ResponseObject(HttpStatus.OK, "Success", lockedGroup);
    }

    @Override
    public ResponseObject unlockGroup(String groupId) {
        SubjectClassGroup unlockedGroup = subjectClassGroupRepository.findById(groupId).get();
        unlockedGroup.setUpdateAt(Instant.now());
        unlockedGroup.setHide((byte) 0);
        unlockedGroup = subjectClassGroupRepository.save(unlockedGroup);
        return new ResponseObject(HttpStatus.OK, "Success", unlockedGroup);
    }


    @Override
    public ResponseObject getGroupById(String subjectClassGroupId) {
        Optional<SubjectClassGroup> foundSubjectClassGroup = subjectClassGroupRepository.findById(subjectClassGroupId);
        if (foundSubjectClassGroup.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Nhóm thực hành không tồn tại", null);
        return new ResponseObject(HttpStatus.OK, "Success", foundSubjectClassGroup.get());
    }

    @Override
    public ResponseObject getGroupsHaveStudent(String studentId, int page) {
        if (page <= 0)
            page = 1;
        Page<SubjectClassGroup> subjectClassGroups = subjectClassGroupRepository.getGroupsThatHasStudent(studentId, PageRequest.of(page - 1, 10));
        int totalPage = subjectClassGroups.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", subjectClassGroups.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchGroupsHaveStudent(String studentId, String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<SubjectClassGroup> subjectClassGroups = subjectClassGroupRepository.searchGroupsThatHasStudent(studentId, "%" + keyword + "%", PageRequest.of(page - 1, 10));
        int totalPage = subjectClassGroups.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", subjectClassGroups.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    private boolean subjectClassGroupRequestIsValid(SubjectClassGroupRequest subjectClassGroupRequest) {
        boolean subjectClassGroupNameIsValid = subjectClassGroupRequest.getSubjectClassGroupName().length() > 0;
        boolean subjectIdIsValid = subjectClassRepository.existsById(subjectClassGroupRequest.getSubjectClassId());
        return subjectIdIsValid && !subjectClassGroupNameIsValid;
    }
}
