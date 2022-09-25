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
    public String[] storeTestCase(MultipartFile input, MultipartFile output) {
        if (input == null || input.isEmpty() || output == null || output.isEmpty())
            throw new RuntimeException("Các file không được để trống");
        String inputName = StringUtils.cleanPath(input.getOriginalFilename());
        String outputName = StringUtils.cleanPath(output.getOriginalFilename());
        String fileNameServer = UUID.randomUUID().toString().replace("-", "");
        String uploadDir = "./uploads/";
        Path uploadPath = Paths.get(uploadDir);
        String[] ret = new String[2];
        try {
            if (!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);
            String extensionInput = FilenameUtils.getExtension(inputName);
            String extensionOutput = FilenameUtils.getExtension(outputName);
            String fileInputServer = fileNameServer + "." + extensionInput + "p";
            String fileOutputServer = fileNameServer + "." + extensionOutput + "p";
            Path inputServerPath = uploadPath.resolve(fileInputServer).toAbsolutePath();
            Path outputServerPath = uploadPath.resolve(fileOutputServer).toAbsolutePath();
            InputStream inputStreamInput = input.getInputStream();
            InputStream inputStreamOutput = output.getInputStream();
            Files.copy(inputStreamInput, inputServerPath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(inputStreamOutput, outputServerPath, StandardCopyOption.REPLACE_EXISTING);
            ret[0] = uploadDir + fileInputServer;
            ret[1] = uploadDir + fileOutputServer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret;
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
