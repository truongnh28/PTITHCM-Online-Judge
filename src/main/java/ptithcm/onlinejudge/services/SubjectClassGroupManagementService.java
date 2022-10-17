package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface SubjectClassGroupManagementService {
    ResponseObject addGroupToClass(String classId, SubjectClassGroupDTO group);

    ResponseObject editGroup(String groupId, SubjectClassGroupDTO group);

    ResponseObject getGroupsOfClassActive(String classId);

    ResponseObject searchGroupOfClassActive(String classId, String keyword);

    ResponseObject getAllGroupsOfClass(String classId);

    ResponseObject lockGroup(String groupId);

    ResponseObject unlockGroup(String groupId);

    ResponseObject searchGroupByIdOrName(String classId, String keyword);

    ResponseObject getGroupById(String groupId);

    ResponseObject getGroupsHaveStudent(String studentId);

    ResponseObject searchGroupsHaveStudent(String studentId, String keyword);
}
