package gay.nyako.nyakomod;

import gay.nyako.nyakomod.mixin.LootTableBuilderAccessor;
import net.fabricmc.fabric.api.loot.v2.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.TableBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class NyakoLoot {
    private static final List<Identifier> coinLootTables = new ArrayList<>();
    private static final List<Identifier> itemLootTables = new ArrayList<>();

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

        itemLootTables.add(new Identifier("minecraft", "chests/simple_dungeon"));
        itemLootTables.add(new Identifier("minecraft", "chests/abandoned_mineshaft"));
        itemLootTables.add(new Identifier("minecraft", "chests/underwater_ruin_big"));
        itemLootTables.add(new Identifier("minecraft", "chests/shipwreck_treasure"));
        itemLootTables.add(new Identifier("minecraft", "chests/pillager_outpost"));

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if ((new Identifier("minecraft", "gameplay/fishing/fish")).equals(id))
            {
                // The first pool is the fish pool
                LootPool lootPool = ((LootTableBuilderAccessor) tableBuilder).getPools().get(0);
                // Copy the fish pool
                LootPool.Builder poolBuilder = FabricLootPoolBuilder.copyOf(lootPool);
                // Modify it...
                poolBuilder.with(ItemEntry.builder(NyakoItems.SPECULAR_FISH).weight(1));
                // Replace the built-in pool with our own
                ((LootTableBuilderAccessor) tableBuilder).getPools().set(0, poolBuilder.build());
            }


            if (source.isBuiltin() && (new Identifier("minecraft", "blocks/oak_leaves")).equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f))
                        .conditionally(BlockLootTableGenerator.WITHOUT_SILK_TOUCH_NOR_SHEARS)
                        //.with(
                        //        ItemEntry.builder(NyakoItems.GREEN_APPLE)
                        .with(
                                BlockLootTableGenerator.addSurvivesExplosionCondition(Blocks.OAK_LEAVES, ItemEntry.builder(NyakoItems.GREEN_APPLE))
                        ).conditionally(
                                TableBonusLootCondition.builder(Enchantments.FORTUNE, 0.005f, 0.0055555557f, 0.00625f, 0.008333334f, 0.025f)
                        );
                tableBuilder.pool(poolBuilder);
            }


            // Add a few random NyakoMod items to loot tables
            if (source.isBuiltin() && coinLootTables.contains(id)) {
                tableBuilder
                        .pool(LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f, 3.0f))
                            .with(ItemEntry.builder(NyakoItems.MAGNET).weight(10))
                            .with(ItemEntry.builder(NyakoItems.ENCUMBERING_STONE).weight(5))
                            .with(ItemEntry.builder(NyakoItems.SUPER_ENCUMBERING_STONE).weight(2))
                            .with(ItemEntry.builder(NyakoItems.BLUEPRINT).weight(20)).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f)))
                            .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_WEEZED).weight(2))
                            .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_WOLVES).weight(2))
                            .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_MERRY).weight(2))
                            .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_MOONLIGHT).weight(2))
                            .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_WELCOME).weight(2))
                            .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_MASK).weight(2))
                            .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_CLUNK).weight(2))
                            .build()
                        );
            }

            // Check if the loot table is one of the coin loot tables
            if (source.isBuiltin() && coinLootTables.contains(id)) {
                // Add a random amount of copper coins to the loot table
                var copperMin = 50;
                var copperMax = 95;
                var copperWeight = 1;

                switch (id.toString()) {
                    // treasure
                    case "minecraft:chests/end_city_treasure":
                    case "minecraft:chests/jungle_temple":
                    case "minecraft:chests/igloo_chest":
                        tableBuilder.pool(addLootTableCoins(NyakoItems.EMERALD_COIN, 1, 2, 5).build());
                    case "minecraft:chests/buried_treasure":
                    case "minecraft:chests/pillager_outpost":
                        copperMin = 0;
                        copperMax = 100;
                        tableBuilder.pool(addLootTableCoins(NyakoItems.GOLD_COIN, 40, 80, 10).build());
                        break;
                    case "minecraft:chests/shipwreck_treasure":
                    case "minecraft:chests/desert_pyramid":
                        copperMin = 0;
                        copperMax = 100;
                        tableBuilder.pool(addLootTableCoins(NyakoItems.GOLD_COIN, 20, 40, 10).build());
                        break;
                }

                tableBuilder.pool(addLootTableCoins(NyakoItems.COPPER_COIN, copperMin, copperMax, copperWeight).build());
            }
        });
    }
}
