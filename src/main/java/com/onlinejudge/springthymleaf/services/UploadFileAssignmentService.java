package com.onlinejudge.springthymleaf.services;

import com.onlinejudge.springthymleaf.model.ResponseObject;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileAssignmentService {
    ResponseObject getFileByID(String id);
    ResponseObject uploadFile(String filePath, String assignmentId);

    ResponseObject deleteFile(String assignmentId);
}
