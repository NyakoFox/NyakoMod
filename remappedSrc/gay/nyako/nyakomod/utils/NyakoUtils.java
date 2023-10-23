package gay.nyako.nyakomod.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import B;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class NyakoUtils {

    public static boolean blockedByShield(LivingEntity entity, Vec3d pos) {
        if (entity.isBlocking() && pos != null) {
            Vec3d vec3d2 = entity.getRotationVec(1.0f);
            Vec3d vec3d3 = pos.relativize(entity.getPos()).normalize();
            vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z);
            return vec3d3.dotProduct(vec3d2) < 0.0;
        }
        return false;
    }


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
