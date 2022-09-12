package ptithcm.onlinejudge.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.response.ResponseObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UploadFileServiceImpl implements UploadFileService {
    private final Cloudinary cloudinary = Singleton.getCloudinary();
    @Override
    public ResponseObject getFileByID(String id) {
        return null;
    }

    @Override
    public ResponseObject uploadFile(String filePath) {
        Map cloudinaryResponse = uploadFileToCloudinary(filePath);
        return new ResponseObject(HttpStatus.OK, "Success", cloudinaryResponse);
    }

    @Override
    public ResponseObject deleteFile(String problemCloudinaryId) {
        try {
            Map deleteResult = cloudinary.uploader().destroy(problemCloudinaryId, ObjectUtils.asMap("resource_type", "raw"));
            return new ResponseObject(HttpStatus.OK, "Success", deleteResult);
        } catch (IOException e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }

    private Map uploadFileToCloudinary(String filePath) {
        Map cloudinaryResponse;
        try {
            cloudinaryResponse = cloudinary.uploader().upload(filePath, ObjectUtils.asMap("resource_type", "raw"));
            return cloudinaryResponse;
        } catch (IOException e) {
            return new HashMap();
        }
    }
}
