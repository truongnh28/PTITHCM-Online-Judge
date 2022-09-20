package ptithcm.onlinejudge.services;

import org.springframework.web.multipart.MultipartFile;
// lưu file vào local
public interface StorageFileService {
    String storeFile(MultipartFile multipartFile);
    boolean deleteFile(String filePath);
}
