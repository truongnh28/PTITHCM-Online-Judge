package ptithcm.onlinejudge.helper;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Paths;

public class FileHelper {
    public static String getFileNameFromPath(String path) {
        return Paths.get(path).getFileName().toString();
    }
    public static String getFileExtensionFromPath(String path) {
        return FilenameUtils.getExtension(path);
    }
}
