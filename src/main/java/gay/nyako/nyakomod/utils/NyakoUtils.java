package gay.nyako.nyakomod.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class NyakoUtils {
    public static String hash(String input) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            digest.update(input.getBytes(StandardCharsets.UTF_8));
            var hash = toHexString(digest.digest());
            return hash.substring(0, 10);
        } catch (NoSuchAlgorithmException ex) {
            return "default";
        }
    }

    public static String hashString(String input) {
        return "custom/" + hash(input);
    }

    private static String toHexString(byte[] bytes) {
        Formatter result = new Formatter();
        try (result) {
            for (var b : bytes) {
                result.format("%02x", b & 0xff);
            }
            return result.toString();
        }
    }
}
