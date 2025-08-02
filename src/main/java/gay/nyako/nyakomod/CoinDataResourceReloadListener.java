package gay.nyako.nyakomod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gay.nyako.nyakomod.data.CunkCoinData;
import gay.nyako.nyakomod.data.CunkCoinDataValues;
import gay.nyako.nyakomod.screens.ShopEntries;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;

public class CoinDataResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return NyakoMod.id("coin_values");
    }

    @Override
    public void reload(ResourceManager manager) {
        ShopEntries.shops.clear();

        manager.findResources("coin_values", identifier -> identifier.getPath().endsWith(".json")).forEach((resourceId, resource) -> {
            try {
                var shopId = new Identifier(
                        resourceId.getNamespace(),
                        resourceId.getPath().substring(12, resourceId.getPath().length() - 5)
                );

                // Use GSon to parse the JSON file into a JsonObject
                JsonObject coinJson = JsonParser.parseReader(new InputStreamReader(resource.getInputStream())).getAsJsonObject();
                long coinValue = coinJson.get("value").getAsLong();
                var cunkCoinData = new CunkCoinData(coinValue);
                JsonObject dimensionMultipliers = coinJson.getAsJsonObject("dimension_multipliers");
                if (dimensionMultipliers != null) {
                    dimensionMultipliers.entrySet().forEach(entry -> {
                        String dimensionIdString = entry.getKey();
                        double multiplier = entry.getValue().getAsDouble();
                        Identifier dimensionIdentifier = Identifier.tryParse(dimensionIdString);
                        cunkCoinData.addMultiplier(dimensionIdentifier, multiplier);
                    });
                }
                CunkCoinDataValues.add(shopId, cunkCoinData);
            } catch (Exception e) {
                NyakoMod.LOGGER.error("Error occurred while loading resource json " + resourceId, e);
            }
        });
    }
}
