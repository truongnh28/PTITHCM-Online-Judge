package ptithcm.onlinejudge.helper;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Paths;

public class FileHelper {
    public static String getBaseNameFromPath(String path) {
        return FilenameUtils.getBaseName(path);
    }
    public static String getFileExtensionFromPath(String path) {
        return FilenameUtils.getExtension(path);
    }
}
