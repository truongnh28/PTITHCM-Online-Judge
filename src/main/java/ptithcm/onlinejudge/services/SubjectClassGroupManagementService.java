package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface SubjectClassGroupManagementService {
    ResponseObject addGroupToClass(String classId, SubjectClassGroupDTO group);

    ResponseObject editGroup(SubjectClassGroupDTO group);

    ResponseObject getGroupsOfClassActive(String classId);

    ResponseObject searchGroupOfClassActive(String classId, String keyword);

    ResponseObject getAllGroupsOfClass(String classId, int page);

    ResponseObject searchGroupOfClassByKeyword(String classId, String keyword, int page);

    ResponseObject lockGroup(String groupId);

    ResponseObject unlockGroup(String groupId);

    ResponseObject getGroupById(String groupId);

    ResponseObject getGroupsHaveStudent(String studentId);

    ResponseObject searchGroupsHaveStudent(String studentId, String keyword);
}
