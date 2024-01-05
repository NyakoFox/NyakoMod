package gay.nyako.nyakomod;

import gay.nyako.nyakomod.utils.NyakoUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModelManager {
    Path gameDir;

    final File packSourceFolder;
    final File manifestFile;

    JSONObject manifest;

    public ModelManager() {
        gameDir = FabricLoader.getInstance().getGameDir();
        packSourceFolder = new File(gameDir.toString(), "ResourcePack");
        manifestFile = new File(gameDir.toString(), "ResourcePack/assets/nyakomod/models/item/custom.json");

        try {
            var content = Files.readString(manifestFile.toPath());
            manifest = new JSONObject(content);
        } catch (IOException e) {
            manifest = new JSONObject("""
                {
                    "parent": "item/generated",
                    "textures": {
                        "layer0": "minecraft:item/diamond"
                    },
                    "overrides": []
                }
            """);

            saveManifest();
        }
    }

    private void saveManifest() {
        try {
            manifestFile.getParentFile().mkdirs();
            Files.writeString(manifestFile.toPath(), manifest.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addModelToManifest(String name, String displayName, String uuid) {
        var overrides = manifest.getJSONArray("overrides");

        var override = new JSONObject();
        var predicate = new JSONObject();
        predicate.put("custom_model_data", overrides.length() + 1);
        override.put("predicate", predicate);
        override.put("model", "nyako_custom:item/" + name);
        override.put("_name", displayName);
        override.put("_uuid", uuid);

        overrides.put(override);
        saveManifest();
    }

    private JSONObject createItemModel(String type, String name) {
        var obj = new JSONObject();

        if (type.equals("block")) {
            obj.put("parent", "nyako_custom:block/" + name);
        } else {
            var textures = new JSONObject();
            textures.put("layer0", "nyako_custom:item/" + name);
            obj.put("textures", textures);

            if (type.equals("handheld")) {
                obj.put("parent", "minecraft:item/handheld");
            } else {
                obj.put("parent", "minecraft:item/generated");
            }
        }

        return obj;
    }

    private JSONObject createBlockModel(String name) {
        var obj = new JSONObject();

        obj.put("parent", "minecraft:block/cube_all");
        var textures = new JSONObject();
        textures.put("all", "nyako_custom:block/" + name);

        obj.put("textures", textures);

        return obj;
    }

    public static BufferedImage downloadImage(String urlPath) {
        BufferedImage image = null;
        URL url = null;

        try {
            url = new URL(urlPath);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "NyakoMod");
            connection.connect();
            image = ImageIO.read(connection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }

    public void addModel(ServerPlayerEntity player, String name, String type, String url) {
        var hash = NyakoUtils.hash(url + player.getUuidAsString());
        var hashedName = name + "_" + hash;

        var image = downloadImage(url);
        if (image != null) {
            var itemModel = createItemModel(type, hashedName);
            itemModel.put("_name", name);
            itemModel.put("_owner", player.getUuidAsString());

            try {
                var file = new File(packSourceFolder.toPath().toString(), "assets/nyako_custom/models/item/" + hashedName + ".json");
                file.getParentFile().mkdirs();
                Files.writeString(file.toPath(), itemModel.toString(2));

                if (type.equals("block")) {
                    var blockModel = createBlockModel(hashedName);
                    var blockFile = new File(packSourceFolder.toPath().toString(), "assets/nyako_custom/models/block/" + hashedName + ".json");
                    blockFile.getParentFile().mkdirs();
                    Files.writeString(blockFile.toPath(), blockModel.toString(2));
                }

                var imageFile = new File(packSourceFolder.toPath().toString(),
                        "assets/nyako_custom/textures/" + (type.equals("block") ? "block" : "item") + "/" + hashedName + ".png");
                imageFile.getParentFile().mkdirs();

                ImageIO.write(image, "png", imageFile);

                addModelToManifest(hashedName, name, player.getUuidAsString());

                NyakoMod.CACHED_RESOURCE_PACK.zipResourcePack();
                NyakoMod.CACHED_RESOURCE_PACK.cacheResourcePack();
                CachedResourcePack.setPlayerResourcePack(player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONObject getManifest() {
        return manifest;
    }

    public void setManifest(JSONObject manifest) {
        this.manifest = manifest;
    }
}
