package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface SubjectClassGroupManagement {
    ResponseObject addGroupToClass(String classId, SubjectClassGroupDTO group);

    ResponseObject editGroup(String groupId, SubjectClassGroupDTO group);

    ResponseObject getAllSubjectClassGroup();

    ResponseObject getAllGroupsOfClass(String classId);

    ResponseObject lockGroup(String groupId);

    ResponseObject unlockGroup(String groupId);

    ResponseObject searchGroupByIdOrName(String classId, String keyword);

    ResponseObject getGroupById(String groupId);
}
