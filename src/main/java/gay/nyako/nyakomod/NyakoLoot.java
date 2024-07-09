package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.loot.v2.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.TableBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class NyakoLoot {

    public static final Identifier CRATE_LOOT_TABLE = new Identifier("nyakomod", "gameplay/fishing/crates");
    public static final Identifier WOODEN_CRATE_LOOT_TABLE = new Identifier("nyakomod", "gameplay/fishing/wooden_crate");
    public static final Identifier IRON_CRATE_LOOT_TABLE = new Identifier("nyakomod", "gameplay/fishing/iron_crate");
    public static final Identifier GOLDEN_CRATE_LOOT_TABLE = new Identifier("nyakomod", "gameplay/fishing/golden_crate");
    public static final Identifier DIAMOND_CRATE_LOOT_TABLE = new Identifier("nyakomod", "gameplay/fishing/diamond_crate");
    public static final Identifier NETHERITE_CRATE_LOOT_TABLE = new Identifier("nyakomod", "gameplay/fishing/netherite_crate");

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
        coinLootTables.add(new Identifier("minecraft", "chests/ancient_city"));
        coinLootTables.add(new Identifier("minecraft", "chests/ancient_city_ice_box"));

        // Dungeons Arise
        coinLootTables.add(new Identifier("dungeons_arise", "abandoned_temple/abandoned_temple_entrance"));
        coinLootTables.add(new Identifier("dungeons_arise", "abandoned_temple/abandoned_temple_map"));
        coinLootTables.add(new Identifier("dungeons_arise", "abandoned_temple/abandoned_temple_top"));
        coinLootTables.add(new Identifier("dungeons_arise", "aviary/aviary_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "aviary/aviary_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "aviary/aviary_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "bandit_towers/bandit_towers_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "bandit_towers/bandit_towers_gardens"));
        coinLootTables.add(new Identifier("dungeons_arise", "bandit_towers/bandit_towers_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "bandit_towers/bandit_towers_rooms"));
        coinLootTables.add(new Identifier("dungeons_arise", "bandit_towers/bandit_towers_supply"));
        coinLootTables.add(new Identifier("dungeons_arise", "bandit_towers/bandit_towers_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "bandit_village/bandit_village_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "bandit_village/bandit_village_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "bandit_village/bandit_village_supply"));
        coinLootTables.add(new Identifier("dungeons_arise", "bandit_village/bandit_village_tents"));
        coinLootTables.add(new Identifier("dungeons_arise", "bathhouse/bathhouse_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "bathhouse/bathhouse_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "ceryneian_hind/ceryneian_hind_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "fishing_hut/fishing_hut_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "foundry/foundry_chains"));
        coinLootTables.add(new Identifier("dungeons_arise", "foundry/foundry_lava_pit"));
        coinLootTables.add(new Identifier("dungeons_arise", "foundry/foundry_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "foundry/foundry_passage_exterior"));
        coinLootTables.add(new Identifier("dungeons_arise", "foundry/foundry_passage_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "foundry/foundry_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "giant_mushroom/red_giant_mushroom"));
        coinLootTables.add(new Identifier("dungeons_arise", "giant_mushroom/twin_giant_mushroom"));
        coinLootTables.add(new Identifier("dungeons_arise", "greenwood_pub/greenwood_pub_barrels_hallways"));
        coinLootTables.add(new Identifier("dungeons_arise", "greenwood_pub/greenwood_pub_barrels_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "greenwood_pub/greenwood_pub_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "heavenly_challenger/heavenly_challenger_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "heavenly_challenger/heavenly_challenger_supply"));
        coinLootTables.add(new Identifier("dungeons_arise", "heavenly_challenger/heavenly_challenger_theater"));
        coinLootTables.add(new Identifier("dungeons_arise", "heavenly_challenger/heavenly_challenger_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "heavenly_conqueror/heavenly_conqueror_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "heavenly_conqueror/heavenly_conqueror_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "heavenly_conqueror/heavenly_conqueror_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "heavenly_rider/heavenly_rider_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "heavenly_rider/heavenly_rider_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "heavenly_rider/heavenly_rider_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_campsite/illager_campsite_map"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_campsite/illager_campsite_supply"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_campsite/illager_campsite_tent"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_corsair/illager_corsair_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_corsair/illager_corsair_supply"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_corsair/illager_corsair_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_fort/illager_fort_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_fort/illager_fort_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_fort/illager_fort_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_galley/illager_galley_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_galley/illager_galley_supply"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_galley/illager_galley_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_windmill/illager_windmill_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "illager_windmill/illager_windmill_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "infested_temple/infested_temple_room_bookshelf"));
        coinLootTables.add(new Identifier("dungeons_arise", "infested_temple/infested_temple_room_forge"));
        coinLootTables.add(new Identifier("dungeons_arise", "infested_temple/infested_temple_room_garden"));
        coinLootTables.add(new Identifier("dungeons_arise", "infested_temple/infested_temple_room_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "infested_temple/infested_temple_room_supply"));
        coinLootTables.add(new Identifier("dungeons_arise", "infested_temple/infested_temple_room_table"));
        coinLootTables.add(new Identifier("dungeons_arise", "infested_temple/infested_temple_top_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "jungle_tree_house/jungle_tree_house_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "jungle_tree_house/jungle_tree_house_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "jungle_tree_house/jungle_tree_house_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "keep_kayra/keep_kayra_garden_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "keep_kayra/keep_kayra_garden_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "keep_kayra/keep_kayra_library_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "keep_kayra/keep_kayra_library_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "keep_kayra/keep_kayra_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "keep_kayra/keep_kayra_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "lighthouse/lighthouse_top"));
        coinLootTables.add(new Identifier("dungeons_arise", "merchant_campsite/merchant_campsite_map"));
        coinLootTables.add(new Identifier("dungeons_arise", "merchant_campsite/merchant_campsite_supply"));
        coinLootTables.add(new Identifier("dungeons_arise", "merchant_campsite/merchant_campsite_tent"));
        coinLootTables.add(new Identifier("dungeons_arise", "mines_treasure_big"));
        coinLootTables.add(new Identifier("dungeons_arise", "mines_treasure_medium"));
        coinLootTables.add(new Identifier("dungeons_arise", "mines_treasure_small"));
        coinLootTables.add(new Identifier("dungeons_arise", "mining_system/mining_system_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "mining_system/mining_system_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "monastery/monastery_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "monastery/monastery_bridges"));
        coinLootTables.add(new Identifier("dungeons_arise", "monastery/monastery_map"));
        coinLootTables.add(new Identifier("dungeons_arise", "mushroom_house/mushroom_house_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "mushroom_house/mushroom_house_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "mushroom_house/mushroom_house_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "mushroom_mines/mushroom_mines_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "mushroom_mines/mushroom_mines_ores"));
        coinLootTables.add(new Identifier("dungeons_arise", "mushroom_mines/mushroom_mines_tools"));
        coinLootTables.add(new Identifier("dungeons_arise", "mushroom_mines/mushroom_mines_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "mushroom_village/mushroom_village_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "mushroom_village/mushroom_village_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "plague_asylum/plague_asylum_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "plague_asylum/plague_asylum_cells"));
        coinLootTables.add(new Identifier("dungeons_arise", "plague_asylum/plague_asylum_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "plague_asylum/plague_asylum_potions"));
        coinLootTables.add(new Identifier("dungeons_arise", "plague_asylum/plague_asylum_storage"));
        coinLootTables.add(new Identifier("dungeons_arise", "plague_asylum/plague_asylum_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "scorched_mines/scorched_mines_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "scorched_mines/scorched_mines_housing"));
        coinLootTables.add(new Identifier("dungeons_arise", "scorched_mines/scorched_mines_hub"));
        coinLootTables.add(new Identifier("dungeons_arise", "scorched_mines/scorched_mines_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "scorched_mines/scorched_mines_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "shiraz_palace/shiraz_palace_elite"));
        coinLootTables.add(new Identifier("dungeons_arise", "shiraz_palace/shiraz_palace_gardens"));
        coinLootTables.add(new Identifier("dungeons_arise", "shiraz_palace/shiraz_palace_library"));
        coinLootTables.add(new Identifier("dungeons_arise", "shiraz_palace/shiraz_palace_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "shiraz_palace/shiraz_palace_rooms"));
        coinLootTables.add(new Identifier("dungeons_arise", "shiraz_palace/shiraz_palace_towers"));
        coinLootTables.add(new Identifier("dungeons_arise", "shiraz_palace/shiraz_palace_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "small_blimp/small_blimp_coal_storage"));
        coinLootTables.add(new Identifier("dungeons_arise", "small_blimp/small_blimp_redstone_chamber"));
        coinLootTables.add(new Identifier("dungeons_arise", "small_blimp/small_blimp_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "small_prairie_house/small_prairie_house_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "small_prairie_house/small_prairie_house_normal"));
        coinLootTables.add(new Identifier("dungeons_arise", "small_prairie_house/small_prairie_house_ruined"));
        coinLootTables.add(new Identifier("dungeons_arise", "thornborn_towers/thornborn_towers_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "thornborn_towers/thornborn_towers_rooms"));
        coinLootTables.add(new Identifier("dungeons_arise", "thornborn_towers/thornborn_towers_top_rooms"));
        coinLootTables.add(new Identifier("dungeons_arise", "thornborn_towers/thornborn_towers_top_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "typhon/typhon_treasure"));
        coinLootTables.add(new Identifier("dungeons_arise", "undead_pirate_ship/undead_pirate_ship_barrels"));
        coinLootTables.add(new Identifier("dungeons_arise", "undead_pirate_ship/undead_pirate_ship_enchants"));
        coinLootTables.add(new Identifier("dungeons_arise", "undead_pirate_ship/undead_pirate_ship_supply"));
        coinLootTables.add(new Identifier("dungeons_arise", "undead_pirate_ship/undead_pirate_ship_treasure"));

        itemLootTables.add(new Identifier("minecraft", "chests/simple_dungeon"));
        itemLootTables.add(new Identifier("minecraft", "chests/abandoned_mineshaft"));
        itemLootTables.add(new Identifier("minecraft", "chests/underwater_ruin_big"));
        itemLootTables.add(new Identifier("minecraft", "chests/shipwreck_treasure"));
        itemLootTables.add(new Identifier("minecraft", "chests/pillager_outpost"));

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if ((new Identifier("minecraft", "gameplay/fishing/fish")).equals(id))
            {
                tableBuilder.modifyPools(poolBuilder -> poolBuilder.with(ItemEntry.builder(NyakoItems.SPECULAR_FISH).weight(1)));
            }

            if(source.isBuiltin() && (new Identifier("minecraft", "gameplay/fishing/treasure")).equals(id))
            {
                LootPool lootPool = ((LootTableBuilderAccessor) tableBuilder).getPools().get(0);
                LootPool.Builder poolBuilder = FabricLootPoolBuilder.copyOf(lootPool);
                poolBuilder.with(LootTableEntry.builder(NyakoLoot.CRATE_LOOT_TABLE));
                ((LootTableBuilderAccessor) tableBuilder).getPools().set(0, poolBuilder.build());
            }

            if (source.isBuiltin() && (new Identifier("minecraft", "blocks/oak_leaves")).equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f))
                        .conditionally(BlockLootTableGenerator.WITHOUT_SILK_TOUCH_NOR_SHEARS)
                        .with(ItemEntry.builder(NyakoItems.GREEN_APPLE).weight(1))
                        /*.with(
                                BlockLootTableGenerator.addSurvivesExplosionCondition(Blocks.OAK_LEAVES, ItemEntry.builder(NyakoItems.GREEN_APPLE))
                        )*/.conditionally(
                                TableBonusLootCondition.builder(Enchantments.FORTUNE, 0.005f, 0.0055555557f, 0.00625f, 0.008333334f, 0.025f)
                        );
                tableBuilder.pool(poolBuilder);
            }


            // Add a few random NyakoMod items to loot tables
            if (source.isBuiltin() && itemLootTables.contains(id)) {
                tableBuilder
                        .pool(LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f, 3.0f))
                            .with(ItemEntry.builder(NyakoItems.MAGNET).weight(10))
                            .with(ItemEntry.builder(NyakoItems.ENCUMBERING_STONE).weight(5))
                            .with(ItemEntry.builder(NyakoItems.SUPER_ENCUMBERING_STONE).weight(2))
                            .with(ItemEntry.builder(NyakoItems.BLUEPRINT).weight(20)).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f)))
                            .with(ItemEntry.builder(NyakoItems.ENDER_GEM).weight(1))
                            .build()
                        );
            }

            // Add a few random NyakoMod items to loot tables
            if (source.isBuiltin() && itemLootTables.contains(id)) {
                tableBuilder
                        .pool(LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f, 3.0f))
                                .with(ItemEntry.builder(NyakoItems.MAGNET).weight(10))
                                .with(ItemEntry.builder(NyakoItems.ENCUMBERING_STONE).weight(5))
                                .with(ItemEntry.builder(NyakoItems.SUPER_ENCUMBERING_STONE).weight(2))
                                .with(ItemEntry.builder(NyakoItems.BLUEPRINT).weight(20)).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f)))
                                .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_WEEZED).weight(1))
                                .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_WOLVES).weight(1))
                                .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_MERRY).weight(1))
                                .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_MOONLIGHT).weight(1))
                                .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_WELCOME).weight(1))
                                .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_MASK).weight(1))
                                .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_CLUNK).weight(1))
                                .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_SKIBIDI).weight(1))
                                .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_MERRY2).weight(1))
                                .with(ItemEntry.builder(NyakoItems.MUSIC_DISC_SKIBIDI_REAL).weight(1))
                                .build()
                        );
            }

            if (source.isBuiltin() && (id.toString().equals("minecraft:chests/end_city_treasure")))
            {
                tableBuilder
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(NyakoItems.ENDER_GEM))
                                .conditionally(RandomChanceLootCondition.builder(0.25f))
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
                    case "minecraft:chests/ancient_city":
                    case "minecraft:chests/ancient_city_ice_box":
                    case "dungeons_arise:shiraz_palace/shiraz_palace_treasure":
                        tableBuilder.pool(addLootTableCoins(NyakoItems.EMERALD_COIN, 1, 2, 5).build());
                    case "minecraft:chests/buried_treasure":
                        copperMin = 0;
                        copperMax = 100;
                        tableBuilder.pool(addLootTableCoins(NyakoItems.GOLD_COIN, 40, 80, 10).build());
                        break;
                    case "minecraft:chests/pillager_outpost":
                        copperMin = 0;
                        copperMax = 100;
                        tableBuilder.pool(addLootTableCoins(NyakoItems.GOLD_COIN, 5, 15, 10).build());
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
