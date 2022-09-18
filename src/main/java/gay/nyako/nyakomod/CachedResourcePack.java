package gay.nyako.nyakomod;

import gay.nyako.nyakomod.utils.Zipper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HexFormat;
import java.util.logging.Level;

public class CachedResourcePack {
    Path gameDir;

    final File packOutputFile;
    final File packOutputFolder;
    final File packSourceFolder;

    static byte[] cachedPack;
    static byte[] packHash;

    public CachedResourcePack() {
        gameDir = FabricLoader.getInstance().getGameDir();
        packOutputFolder = new File(gameDir.toString(), "static");
        packOutputFile = new File(gameDir.toString(), "static/pack.zip");
        packSourceFolder = new File(gameDir.toString(), "ResourcePack");

        initializeResourcePack();
    }

    void initializeResourcePack() {
        try {
            if (!packSourceFolder.exists()) {
                 packSourceFolder.mkdir();
            }

            if (!packOutputFolder.exists()) {
                packOutputFolder.mkdir();
            }

            var packMeta = new File(packSourceFolder, "pack.mcmeta");
            if (!packMeta.exists()) {
                var writer = new FileWriter(packMeta);
                writer.write("""
                {
                  \"pack\": {
                    \"pack_format\": 7,
                    \"description\": \"ally ally ally\"
                  }
                }
                """);
                writer.close();
            }

            var customModels = new File(packSourceFolder, "assets/nyako_custom/models/item");
            if (!customModels.exists()) {
                customModels.mkdirs();
            }
            var customTextures = new File(packSourceFolder, "assets/nyako_custom/textures/item");
            if (!customTextures.exists()) {
                customTextures.mkdirs();
            }

            zipResourcePack();
            cacheResourcePack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void zipResourcePack() {
        try {
            var zipper = new Zipper(packSourceFolder, packOutputFile);
            zipper.zip();
        } catch (Exception e) {
            Log.warn(LogCategory.LOG, "Unable to zip resource pack", e);
        }
    }

    public void cacheResourcePack() {
        try {
            cachedPack = Files.readAllBytes(packOutputFile.toPath());
            packHash = getSHADigest(cachedPack);
        } catch (Exception e) {
            Log.warn(LogCategory.LOG, "Unable to cache resource pack", e);
        }
    }

    byte[] getSHADigest(byte[] input) {
        try {
            var crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(input);
            return crypt.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static byte[] getCachedPack() {
        return cachedPack;
    }

    public static byte[] getPackHash() {
        return packHash;
    }

    public static void setPlayerResourcePack(ServerPlayerEntity player) {
        // var config = AllyCatPlugin.INSTANCE.getConfig();

        Formatter formatter = new Formatter();
        for (byte b : packHash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();

        // Append the pack's hash as a nonce, to force refetch
        // var url = config.getString("resourcePackUrl") + "?t=" + result;
        var url = NyakoMod.CONFIG.packURL() + "?t=" + result;

        player.sendResourcePackUrl(url, result, false, Text.literal("Pwease enable da wesouwce pack ;w;").formatted(Formatting.AQUA));

        // PackUpdateNotifier.registerUpdate(player);
    }
}
