package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class NyakoModLoot {
    private static List<Identifier> coinLootTables = new ArrayList<>();

    private static LootPool.Builder addLootTableCoins(Item coinItem, int min, int max, int weight) {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(coinItem)
                        .weight(weight)
                        .quality(0)
                        .apply(SetCountLootFunction.builder(
                                UniformLootNumberProvider.create(min, max)
                        ))
                );
    }

    public static void register() {
        coinLootTables.add(new Identifier("minecraft", "chests/simple_dungeon"));
        coinLootTables.add(new Identifier("minecraft", "chests/abandoned_mineshaft"));
        coinLootTables.add(new Identifier("minecraft", "chests/stronghold_corridor"));
        coinLootTables.add(new Identifier("minecraft", "chests/stronghold_crossing"));
        coinLootTables.add(new Identifier("minecraft", "chests/stronghold_library"));
        coinLootTables.add(new Identifier("minecraft", "chests/woodland_mansion"));
        coinLootTables.add(new Identifier("minecraft", "chests/underwater_ruin_small"));
        coinLootTables.add(new Identifier("minecraft", "chests/underwater_ruin_big"));
        coinLootTables.add(new Identifier("minecraft", "chests/shipwreck_map"));
        coinLootTables.add(new Identifier("minecraft", "chests/shipwreck_supply"));
        coinLootTables.add(new Identifier("minecraft", "chests/nether_bridge"));
        coinLootTables.add(new Identifier("minecraft", "chests/shipwreck_treasure"));
        coinLootTables.add(new Identifier("minecraft", "chests/buried_treasure"));
        coinLootTables.add(new Identifier("minecraft", "chests/end_city_treasure"));
        coinLootTables.add(new Identifier("minecraft", "chests/pillager_outpost"));
        coinLootTables.add(new Identifier("minecraft", "chests/jungle_temple"));
        coinLootTables.add(new Identifier("minecraft", "chests/desert_pyramid"));
        coinLootTables.add(new Identifier("minecraft", "chests/igloo_chest"));
        coinLootTables.add(new Identifier("minecraft", "chests/ruined_portal"));
        coinLootTables.add(new Identifier("minecraft", "chests/village/village_weaponsmith"));

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            // Check if the loot table is one of the coin loot tables
            if (source.isBuiltin() && coinLootTables.contains(id)) {
                // Add a random amount of copper coins to the loot table
                var copperMin = 50;
                var copperMax = 95;
                var copperWeight = 1;

                switch (id.toString()) {
                    // treasure
                    case "minecraft:chests/end_city_treasure":
                        tableBuilder.pool(addLootTableCoins(NyakoModItem.EMERALD_COIN_ITEM, 1, 2, 5).build());
                    case "minecraft:chests/buried_treasure":
                    case "minecraft:chests/shipwreck_treasure":
                        copperMin = 0;
                        copperMax = 100;
                        tableBuilder.pool(addLootTableCoins(NyakoModItem.GOLD_COIN_ITEM, 40, 80, 10).build());
                        break;
                }

                tableBuilder.pool(addLootTableCoins(NyakoModItem.COPPER_COIN_ITEM, copperMin, copperMax, copperWeight).build());
            }
        });
    }
}
