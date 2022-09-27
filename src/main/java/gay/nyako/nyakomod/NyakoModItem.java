package gay.nyako.nyakomod;

import gay.nyako.nyakomod.entity.PetDragonEntity;
import gay.nyako.nyakomod.item.*;
import gay.nyako.nyakomod.item.gacha.DiscordGachaItem;
import gay.nyako.nyakomod.item.gacha.GachaItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;

public class NyakoModItem {

    public static final Item SPUNCH_BLOCK               = register("spunch_block",              new BlockItem(NyakoModBlock.SPUNCH_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item LAUNCHER                   = register("launcher",                  new BlockItem(NyakoModBlock.LAUNCHER, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item BRICKUS                    = register("brickus",                   new BlockItem(NyakoModBlock.BRICKUS, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item BRICKUS_STAIRS             = register("brickus_stairs",            new BlockItem(NyakoModBlock.BRICKUS_STAIRS, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item BRICKUS_SLAB               = register("brickus_slab",              new BlockItem(NyakoModBlock.BRICKUS_SLAB, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item BRICKUS_WALL               = register("brickus_wall",              new BlockItem(NyakoModBlock.BRICKUS_WALL, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item MAIN_SHOP                  = register("main_shop",                 new BlockItem(NyakoModBlock.MAIN_SHOP, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item DRAFTING_TABLE             = register("drafting_table",            new BlockItem(NyakoModBlock.DRAFTING_TABLE, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item PLASTEEL_CASING            = register("plasteel_casing",           new BlockItem(NyakoModBlock.PLASTEEL_CASING, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item PLASTEEL_SMOOTH_CASING     = register("plasteel_smooth_casing",    new BlockItem(NyakoModBlock.PLASTEEL_SMOOTH_CASING, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item PLASTEEL_PLATING           = register("plasteel_plating",          new BlockItem(NyakoModBlock.PLASTEEL_PLATING, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item PLASTEEL_PILLAR            = register("plasteel_pillar",           new BlockItem(NyakoModBlock.PLASTEEL_PILLAR, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item FIREBLU                    = register("fireblu",                   new BlockItem(NyakoModBlock.FIREBLU, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item MATTER_VORTEX              = register("matter_vortex",             new BlockItem(NyakoModBlock.MATTER_VORTEX, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item TRUE_BLOCK                 = register("true_block",                new BlockItem(NyakoModBlock.TRUE_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));

    public static final Item DRIP_JACKET                = register("drip_jacket",               new ArmorItem(NyakoMod.customArmorMaterial, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT).fireproof()));
    public static final Item STAFF_OF_SMITING_ITEM      = register("staff_of_smiting",          new StaffOfSmitingItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof()));
    public static final Item PRESENT_ITEM               = register("present",                   new PresentItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item CUSTOM_ITEM                = register("custom",                    new CustomItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(69)));
    public static final Item DEV_NULL_ITEM              = register("dev_null",                  new DevNullItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item RETENTIVE_BALL_ITEM        = register("retentive_ball",            new RetentiveBallItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item BLUEPRINT_WORKBENCH_ITEM   = register("blueprint_workbench",       new BlockItem(NyakoModBlock.BLUEPRINT_WORKBENCH, new Item.Settings().group(ItemGroup.MISC)));
    public static final Item COPPER_COIN_ITEM           = register("copper_coin",               new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), NyakoModBlock.COPPER_SINGLE_COIN, "copper", 1));
    public static final Item GOLD_COIN_ITEM             = register("gold_coin",                 new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), NyakoModBlock.GOLD_SINGLE_COIN, "gold", 100));
    public static final Item EMERALD_COIN_ITEM          = register("emerald_coin",              new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), NyakoModBlock.EMERALD_SINGLE_COIN, "emerald", 10000));
    public static final Item DIAMOND_COIN_ITEM          = register("diamond_coin",              new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), NyakoModBlock.DIAMOND_SINGLE_COIN, "diamond", 1000000));
    public static final Item NETHERITE_COIN_ITEM        = register("netherite_coin",            new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100).fireproof(), NyakoModBlock.NETHERITE_SINGLE_COIN, "netherite", 100000000));
    public static final Item BAG_OF_COINS_ITEM          = register("bag_of_coins",              new BagOfCoinsItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item HUNGRY_BAG_OF_COINS_ITEM   = register("hungry_bag_of_coins",       new BagOfCoinsItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item TIME_IN_A_BOTTLE           = register("time_in_a_bottle",          new TimeInABottleItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item SOUL_JAR                   = register("soul_jar",                  new SoulJarItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item TEST_ITEM                  = register("test_item",                 new TestItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item BLUEPRINT                  = register("blueprint",                 new BlueprintItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(16)));
    public static final Item DIAMOND_GACHA_ITEM         = register("diamond_gacha",             new GachaItem(new FabricItemSettings().group(ItemGroup.MISC).food(FoodComponents.GOLDEN_CARROT), 2, Arrays.asList((MutableText) Text.of("You can't make tools out of these,"), (MutableText) Text.of("but at least they're healthy!"))));
    public static final Item MARIO_GACHA_ITEM           = register("mario_gacha",               new GachaItem(new FabricItemSettings().group(ItemGroup.MISC), 4, (MutableText) Text.of("The lovable plumber!")));
    public static final Item LUIGI_GACHA_ITEM           = register("luigi_gacha",               new GachaItem(new FabricItemSettings().group(ItemGroup.MISC), 4, (MutableText) Text.of("The lovable plumber's brother!")));
    public static final Item DISCORD_GACHA_ITEM         = register("discord_gacha",             new DiscordGachaItem(new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item STAFF_OF_VORBULATION_ITEM  = register("staff_of_vorbulation",      new StaffOfVorbulationItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof()));
    public static final Item PET_SPRITE_SUMMON_ITEM     = register("pet_sprite_summon",         new PetSpriteSummonItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof()));
    public static final Item PET_DRAGON_SUMMON_ITEM     = register("pet_dragon_summon",         new PetSummonItem<>(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof(), NyakoMod.PET_DRAGON, PetDragonEntity::createPet));
    public static final Item PIAMOND_DICKAXE            = register("piamond_dickaxe",           new Item(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item TWO_TALL                   = register("two_tall",                  new Item(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item NETHER_PORTAL              = registerMC("nether_portal",           new BlockItem(Blocks.NETHER_PORTAL, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item FIRE                       = registerMC("fire",                    new BlockItem(Blocks.FIRE,          new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item SOUL_FIRE                  = registerMC("soul_fire",               new BlockItem(Blocks.SOUL_FIRE,     new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item WATER                      = registerMC("water",                   new BlockItem(Blocks.WATER,         new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item LAVA                       = registerMC("lava",                    new BlockItem(Blocks.LAVA,          new FabricItemSettings().group(ItemGroup.MISC)));

    public static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier("nyakomod", id), item);
    }

    public static Item registerMC(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier("minecraft", id), item);
    }
}
