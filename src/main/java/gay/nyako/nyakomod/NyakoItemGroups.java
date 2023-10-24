package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NyakoItemGroups {
    private static final ItemGroup CUSTOM_ITEMS = register("custom_items", FabricItemGroup.builder()
            .icon(() -> new ItemStack(NyakoItems.DIAMOND_APPLE))
            .displayName(Text.translatable("itemGroup.nyakomod.custom_items"))
            .entries((context, entries) -> {
                entries.add(NyakoItems.SPUNCH_BLOCK);
                entries.add(NyakoItems.LAUNCHER);
                entries.add(NyakoItems.BRICKUS);
                entries.add(NyakoItems.BRICKUS_STAIRS);
                entries.add(NyakoItems.BRICKUS_SLAB);
                entries.add(NyakoItems.BRICKUS_WALL);
                entries.add(NyakoItems.MAIN_SHOP);
                entries.add(NyakoItems.DRAFTING_TABLE);
                entries.add(NyakoItems.PLASTEEL_CASING);
                entries.add(NyakoItems.PLASTEEL_SMOOTH_CASING);
                entries.add(NyakoItems.PLASTEEL_PLATING);
                entries.add(NyakoItems.PLASTEEL_PILLAR);
                entries.add(NyakoItems.FIREBLU);
                entries.add(NyakoItems.MATTER_VORTEX);
                entries.add(NyakoItems.TRUE_BLOCK);
                entries.add(NyakoItems.DRIP_JACKET);
                entries.add(NyakoItems.STAFF_OF_SMITING);
                entries.add(NyakoItems.PRESENT);
                entries.add(NyakoItems.CUSTOM_ITEM);
                entries.add(NyakoItems.DEV_NULL);
                entries.add(NyakoItems.RETENTIVE_BALL);
                entries.add(NyakoItems.BLUEPRINT_WORKBENCH);
                entries.add(NyakoItems.COPPER_COIN);
                entries.add(NyakoItems.GOLD_COIN);
                entries.add(NyakoItems.EMERALD_COIN);
                entries.add(NyakoItems.DIAMOND_COIN);
                entries.add(NyakoItems.NETHERITE_COIN);
                entries.add(NyakoItems.BAG_OF_COINS);
                entries.add(NyakoItems.HUNGRY_BAG_OF_COINS);
                entries.add(NyakoItems.TIME_IN_A_BOTTLE);
                entries.add(NyakoItems.SOUL_JAR);
                entries.add(NyakoItems.TEST_ITEM);
                entries.add(NyakoItems.BLUEPRINT);
                entries.add(NyakoItems.DIAMOND_GACHA);
                entries.add(NyakoItems.MARIO_GACHA);
                entries.add(NyakoItems.LUIGI_GACHA);
                entries.add(NyakoItems.DISCORD_GACHA);
                entries.add(NyakoItems.STAFF_OF_VORBULATION);
                entries.add(NyakoItems.PET_SPRITE_SUMMON);
                entries.add(NyakoItems.PET_DRAGON_SUMMON);
                entries.add(NyakoItems.PIAMOND_DICKAXE);
                entries.add(NyakoItems.TWO_TALL);
                entries.add(NyakoItems.NETHER_PORTAL);
                entries.add(NyakoItems.FIRE);
                entries.add(NyakoItems.SOUL_FIRE);
                entries.add(NyakoItems.WATER);
                entries.add(NyakoItems.LAVA);
                entries.add(NyakoItems.NOTE_BLOCK_PLUS);
                entries.add(NyakoItems.PRESENT_WRAPPER);
                entries.add(NyakoItems.NETHER_PORTAL_BUCKET);
                entries.add(NyakoItems.NETHER_PORTAL_STRUCTURE);
                entries.add(NyakoItems.LEFT_DIAMOND);
                entries.add(NyakoItems.RIGHT_DIAMOND);
                entries.add(NyakoItems.BOOKSHELF_STAIRS);
                entries.add(NyakoItems.GOLD_STARRY_BLOCK);
                entries.add(NyakoItems.BLUE_STARRY_BLOCK);
                entries.add(NyakoItems.TITANSTONE);
                entries.add(NyakoItems.ALLYBOX);
                entries.add(NyakoItems.DRAGON_MILK_BUCKET);
                entries.add(NyakoItems.MONITOR);
                entries.add(NyakoItems.FLINT_AND_STEEL_PLUS);
                entries.add(NyakoItems.CREEPER);
                entries.add(NyakoItems.WITHER);
                entries.add(NyakoItems.HORSE_MILK_BUCKET);
                entries.add(NyakoItems.MAGNET);
                entries.add(NyakoItems.ENCUMBERING_STONE);
                entries.add(NyakoItems.SUPER_ENCUMBERING_STONE);
                entries.add(NyakoItems.DRAGON_SCALE);
                entries.add(NyakoItems.JEAN_JACKET);
                entries.add(NyakoItems.JEAN_HAT);
                entries.add(NyakoItems.JEANS);
                entries.add(NyakoItems.JEAN_SOCKS);
                entries.add(NyakoItems.TOTEM_OF_DYING);
                entries.add(NyakoItems.FOAM_ZOMBIE);
                entries.add(NyakoItems.GROWN_FOAM_ZOMBIE);
                entries.add(NyakoItems.MEGA_DIAMOND_PICKAXE);
                entries.add(NyakoItems.SMITHING_HAMMER);
                entries.add(NyakoItems.ROD_OF_DISCORD);
                entries.add(NyakoItems.DIAMOND_APPLE);
                entries.add(NyakoItems.EMERALD_APPLE);
                entries.add(NyakoItems.GREEN_APPLE);
                entries.add(NyakoItems.SPECULAR_FISH);
                entries.add(NyakoItems.RECALL_POTION);
                entries.add(NyakoItems.NETHER_REACTOR_CORE);
                entries.add(NyakoItems.GLOWING_OBSIDIAN);
                entries.add(NyakoItems.ENDER_GEM);
                entries.add(NyakoItems.ECHO_PEARL);
                entries.add(NyakoItems.MUSIC_DISC_WOLVES);
                entries.add(NyakoItems.MUSIC_DISC_MASK);
                entries.add(NyakoItems.MUSIC_DISC_CLUNK);
                entries.add(NyakoItems.MUSIC_DISC_MERRY);
                entries.add(NyakoItems.MUSIC_DISC_WEEZED);
                entries.add(NyakoItems.MUSIC_DISC_MOONLIGHT);
                entries.add(NyakoItems.MUSIC_DISC_WELCOME);
                entries.add(NyakoItems.MUSIC_DISC_SKIBIDI);
                entries.add(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(NyakoMod.CUNKLESS_CURSE_ENCHANTMENT, 1)));
                entries.add(NyakoItems.ECHO_DIRT);
                entries.add(NyakoItems.ECHO_STONE);
                entries.add(NyakoItems.ECHO_GROWTH);
                entries.add(NyakoItems.ECHO_SLATE);
            })
            .build());

    public static ItemGroup register(String id, ItemGroup group) {
        return Registry.register(Registries.ITEM_GROUP, RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("nyakomod", id)), group);
    }

    public static void register() {
        // Intentionally left blank
    }
}
