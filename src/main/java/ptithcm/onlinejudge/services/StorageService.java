package ptithcm.onlinejudge.services;

import org.springframework.web.multipart.MultipartFile;
// lưu file vào local
public interface StorageService {
    String storeFile(MultipartFile file);
}
