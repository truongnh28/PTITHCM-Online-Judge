package ptithcm.onlinejudge.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StorageFileServiceImpl implements StorageFileService {
    @Override
    public String storeFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new RuntimeException("File không được để trống");
        }
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String fileNameServer = UUID.randomUUID().toString().replace("-", "");
        String uploadDir = "./uploads/";
        Path uploadPath = Paths.get(uploadDir);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileExtension = FilenameUtils.getExtension(fileName);
            fileNameServer = fileNameServer + "." + fileExtension;
            Path fileServerPath = uploadPath.resolve(fileNameServer).toAbsolutePath();
            InputStream inputStream = multipartFile.getInputStream();
            Files.copy(inputStream, fileServerPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return uploadDir + fileNameServer;
    }

    @Override
    public boolean deleteFile(String filePath) {
        if (Files.exists(Path.of(filePath))) {
            File file = new File(filePath);
            return file.delete();
        }
        return false;
    }
}
