package ptithcm.onlinejudge.services;

import org.springframework.web.multipart.MultipartFile;
// lưu file vào local
public interface StorageFileService {
    String storeFile(MultipartFile multipartFile);
    String[] storeTestCase(MultipartFile input, MultipartFile output);
    boolean deleteFile(String filePath);
}
