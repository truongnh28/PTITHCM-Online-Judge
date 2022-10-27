package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;

@Service
public interface SubjectClassGroupManagementService {
    ResponseObject addGroupToClass(String classId, SubjectClassGroupDTO group);

    ResponseObject editGroup(SubjectClassGroupDTO group);

    ResponseObject getGroupsOfClassActive(String classId, int page);

    ResponseObject searchGroupOfClassActive(String classId, String keyword, int page);

    ResponseObject getAllGroupsOfClass(String classId, int page);

    ResponseObject searchGroupOfClassByKeyword(String classId, String keyword, int page);

    ResponseObject lockGroup(String groupId);

    ResponseObject unlockGroup(String groupId);

    ResponseObject getGroupById(String groupId);

    ResponseObject getGroupsHaveStudent(String studentId, int page);

    ResponseObject searchGroupsHaveStudent(String studentId, String keyword, int page);
}
