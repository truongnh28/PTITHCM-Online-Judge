package ptithcm.onlinejudge.helper;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class SHA256Helper {
    public static String hash(String password) {
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }
}
