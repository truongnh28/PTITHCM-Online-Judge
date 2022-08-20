package ptithcm.onlinejudge.services;


import ptithcm.onlinejudge.model.ResponseObject;

public interface UploadFileAssignmentService {
    ResponseObject getFileByID(String id);
    ResponseObject uploadFile(String filePath, String assignmentId);

    ResponseObject deleteFile(String assignmentId);
}
