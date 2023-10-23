package gay.nyako.nyakomod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gay.nyako.nyakomod.screens.ShopData;
import gay.nyako.nyakomod.screens.ShopEntries;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;

public class ShopDataResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return new Identifier("nyakomod", "shops");
    }

    @Override
    public void reload(ResourceManager manager) {
        ShopEntries.shops.clear();

        manager.findResources("shops", identifier -> identifier.getPath().endsWith(".json")).forEach((resourceId, resource) -> {
            try {
                var shopId = new Identifier(
                        resourceId.getNamespace(),
                        resourceId.getPath().substring(6, resourceId.getPath().length() - 5)
                );

                var shopData = new ShopData(shopId);

                // Use GSon to parse the JSON file into a JsonObject
                JsonObject shopJson = JsonParser.parseReader(new InputStreamReader(resource.getInputStream())).getAsJsonObject();
                CunkShop.loadShopModelFromJson(shopJson, shopData);
                CunkShop.storeShopModelJson(shopJson, shopId);
            } catch (Exception e) {
                NyakoMod.LOGGER.error("Error occurred while loading resource json " + resourceId, e);
            }
        });
    }
}
