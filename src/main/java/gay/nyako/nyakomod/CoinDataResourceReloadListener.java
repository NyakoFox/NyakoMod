package gay.nyako.nyakomod;

import com.google.gson.JsonArray;
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
        CunkCoinDataValues.clear();

        manager.findResources("coin_values", identifier -> identifier.getPath().endsWith(".json")).forEach((resourceId, resource) -> {
            try {
                var entityId = new Identifier(
                        resourceId.getNamespace(),
                        resourceId.getPath().substring(12, resourceId.getPath().length() - 5)
                );

                // Use GSon to parse the JSON file into a JsonObject
                JsonObject coinJson = JsonParser.parseReader(new InputStreamReader(resource.getInputStream())).getAsJsonObject();
                long coinValue = coinJson.get("value").getAsLong();
                var cunkCoinData = new CunkCoinData(coinValue);

                // Load dimension multipliers
                JsonObject dimensionMultipliers = coinJson.getAsJsonObject("dimension_multipliers");
                if (dimensionMultipliers != null) {
                    dimensionMultipliers.entrySet().forEach(entry -> {
                        String dimensionIdString = entry.getKey();
                        double multiplier = entry.getValue().getAsDouble();
                        Identifier dimensionIdentifier = Identifier.tryParse(dimensionIdString);
                        cunkCoinData.addMultiplier(dimensionIdentifier, multiplier);
                    });
                }

                // Load NBT conditions
                JsonArray nbtConditions = coinJson.getAsJsonArray("nbt_conditions");
                if (nbtConditions != null) {
                    for (int i = 0; i < nbtConditions.size(); i++) {
                        JsonObject condition = nbtConditions.get(i).getAsJsonObject();
                        String key = condition.get("key").getAsString();
                        var value = condition.get("value").getAsString();
                        double multiplier = condition.get("multiplier").getAsDouble();
                        cunkCoinData.addNBTCondition(key, value, multiplier);
                    }
                }

                CunkCoinDataValues.add(entityId, cunkCoinData);
            } catch (Exception e) {
                NyakoMod.LOGGER.error("Error occurred while loading resource json " + resourceId, e);
            }
        });
    }
}
