package ptithcm.onlinejudge.services;


import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.response.ResponseObject;
@Service
public interface UploadFileService {
    ResponseObject getFileByID(String id);
    ResponseObject uploadFile(String filePath);

    ResponseObject deleteFile(String assignmentId);
}
