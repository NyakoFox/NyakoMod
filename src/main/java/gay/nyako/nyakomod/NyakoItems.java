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
import net.minecraft.util.Rarity;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;

public class NyakoItems {

    public static final Item SPUNCH_BLOCK               = register("spunch_block",              new BlockItem(NyakoBlocks.SPUNCH_BLOCK, new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON)));
    public static final Item LAUNCHER                   = register("launcher",                  new BlockItem(NyakoBlocks.LAUNCHER, new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.RARE)));
    public static final Item BRICKUS                    = register("brickus",                   new BlockItem(NyakoBlocks.BRICKUS, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item BRICKUS_STAIRS             = register("brickus_stairs",            new BlockItem(NyakoBlocks.BRICKUS_STAIRS, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item BRICKUS_SLAB               = register("brickus_slab",              new BlockItem(NyakoBlocks.BRICKUS_SLAB, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item BRICKUS_WALL               = register("brickus_wall",              new BlockItem(NyakoBlocks.BRICKUS_WALL, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item MAIN_SHOP                  = register("main_shop",                 new BlockItem(NyakoBlocks.MAIN_SHOP, new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.RARE)));
    public static final Item DRAFTING_TABLE             = register("drafting_table",            new BlockItem(NyakoBlocks.DRAFTING_TABLE, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item PLASTEEL_CASING            = register("plasteel_casing",           new BlockItem(NyakoBlocks.PLASTEEL_CASING, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item PLASTEEL_SMOOTH_CASING     = register("plasteel_smooth_casing",    new BlockItem(NyakoBlocks.PLASTEEL_SMOOTH_CASING, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item PLASTEEL_PLATING           = register("plasteel_plating",          new BlockItem(NyakoBlocks.PLASTEEL_PLATING, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item PLASTEEL_PILLAR            = register("plasteel_pillar",           new BlockItem(NyakoBlocks.PLASTEEL_PILLAR, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item FIREBLU                    = register("fireblu",                   new BlockItem(NyakoBlocks.FIREBLU, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item MATTER_VORTEX              = register("matter_vortex",             new BlockItem(NyakoBlocks.MATTER_VORTEX, new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.RARE)));
    public static final Item TRUE_BLOCK                 = register("true_block",                new BlockItem(NyakoBlocks.TRUE_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));

    public static final Item DRIP_JACKET                = register("drip_jacket",               new ArmorItem(NyakoMod.NYAKO_ARMOR_MATERIAL, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT).fireproof()));
    public static final Item STAFF_OF_SMITING_ITEM      = register("staff_of_smiting",          new StaffOfSmitingItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof().rarity(Rarity.RARE)));
    public static final Item PRESENT_ITEM               = register("present",                   new PresentItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item CUSTOM_ITEM                = register("custom",                    new CustomItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(69).rarity(Rarity.EPIC)));
    public static final Item DEV_NULL_ITEM              = register("dev_null",                  new DevNullItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item RETENTIVE_BALL_ITEM        = register("retentive_ball",            new RetentiveBallItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item BLUEPRINT_WORKBENCH_ITEM   = register("blueprint_workbench",       new BlockItem(NyakoBlocks.BLUEPRINT_WORKBENCH, new Item.Settings().group(ItemGroup.MISC)));
    public static final Item COPPER_COIN_ITEM           = register("copper_coin",               new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), NyakoBlocks.COPPER_SINGLE_COIN, "copper", 1));
    public static final Item GOLD_COIN_ITEM             = register("gold_coin",                 new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), NyakoBlocks.GOLD_SINGLE_COIN, "gold", 100));
    public static final Item EMERALD_COIN_ITEM          = register("emerald_coin",              new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), NyakoBlocks.EMERALD_SINGLE_COIN, "emerald", 10000));
    public static final Item DIAMOND_COIN_ITEM          = register("diamond_coin",              new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), NyakoBlocks.DIAMOND_SINGLE_COIN, "diamond", 1000000));
    public static final Item NETHERITE_COIN_ITEM        = register("netherite_coin",            new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100).fireproof(), NyakoBlocks.NETHERITE_SINGLE_COIN, "netherite", 100000000));
    public static final Item BAG_OF_COINS_ITEM          = register("bag_of_coins",              new BagOfCoinsItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item HUNGRY_BAG_OF_COINS_ITEM   = register("hungry_bag_of_coins",       new BagOfCoinsItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TIME_IN_A_BOTTLE           = register("time_in_a_bottle",          new TimeInABottleItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item SOUL_JAR                   = register("soul_jar",                  new SoulJarItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TEST_ITEM                  = register("test_item",                 new TestItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item BLUEPRINT                  = register("blueprint",                 new BlueprintItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(16)));
    public static final Item DIAMOND_GACHA_ITEM         = register("diamond_gacha",             new GachaItem(new FabricItemSettings().group(ItemGroup.MISC).food(FoodComponents.GOLDEN_CARROT), 2, Arrays.asList((MutableText) Text.of("You can't make tools out of these,"), (MutableText) Text.of("but at least they're healthy!"))));
    public static final Item MARIO_GACHA_ITEM           = register("mario_gacha",               new GachaItem(new FabricItemSettings().group(ItemGroup.MISC), 4, (MutableText) Text.of("The lovable plumber!")));
    public static final Item LUIGI_GACHA_ITEM           = register("luigi_gacha",               new GachaItem(new FabricItemSettings().group(ItemGroup.MISC), 4, (MutableText) Text.of("The lovable plumber's brother!")));
    public static final Item DISCORD_GACHA_ITEM         = register("discord_gacha",             new DiscordGachaItem(new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item STAFF_OF_VORBULATION_ITEM  = register("staff_of_vorbulation",      new StaffOfVorbulationItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof().rarity(Rarity.EPIC)));
    public static final Item PET_SPRITE_SUMMON_ITEM     = register("pet_sprite_summon",         new PetSpriteSummonItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof()));
    public static final Item PET_DRAGON_SUMMON_ITEM     = register("pet_dragon_summon",         new PetChangeSummonItem<>(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof(), NyakoEntities.PET_DRAGON, PetDragonEntity::createPet)
            .addVariation(Text.literal("Fire Dragon"), "dragon")
    );
    public static final Item PIAMOND_DICKAXE            = register("piamond_dickaxe",           new Item(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item TWO_TALL                   = register("two_tall",                  new Item(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item NETHER_PORTAL              = registerMC("nether_portal",           new BlockItem(Blocks.NETHER_PORTAL, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item FIRE                       = registerMC("fire",                    new BlockItem(Blocks.FIRE,          new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item SOUL_FIRE                  = registerMC("soul_fire",               new BlockItem(Blocks.SOUL_FIRE,     new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item WATER                      = registerMC("water",                   new BlockItem(Blocks.WATER,         new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item LAVA                       = registerMC("lava",                    new BlockItem(Blocks.LAVA,          new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item NOTE_BLOCK_PLUS            = register("note_block_plus",           new BlockItem(NyakoBlocks.NOTE_BLOCK_PLUS, new FabricItemSettings().group(ItemGroup.REDSTONE)));
    public static final Item PRESENT_WRAPPER            = register("present_wrapper",           new BlockItem(NyakoBlocks.PRESENT_WRAPPER, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item NETHER_PORTAL_BUCKET       = register("nether_portal_bucket",      new NetherPortalBucketItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item NETHER_PORTAL_STRUCTURE    = register("nether_portal_structure",   new Item(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item LEFT_DIAMOND               = register("left_diamond",              new Item(new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item RIGHT_DIAMOND              = register("right_diamond",             new Item(new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item BOOKSHELF_STAIRS           = register("bookshelf_stairs",          new BlockItem(NyakoBlocks.BOOKSHELF_STAIRS, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item GOLD_STARRY_BLOCK          = register("gold_starry_block",         new BlockItem(NyakoBlocks.GOLD_STARRY_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item BLUE_STARRY_BLOCK          = register("blue_starry_block",         new BlockItem(NyakoBlocks.BLUE_STARRY_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item TITANSTONE                 = register("titanstone",                new BlockItem(NyakoBlocks.TITANSTONE, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item ALLYBOX                    = register("allybox",                   new BlockItem(NyakoBlocks.ALLYBOX, new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item DRAGON_MILK_BUCKET         = register("dragon_milk_bucket",        new Item(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item MONITOR                    = register("monitor",                   new MonitorItem(new FabricItemSettings().group(ItemGroup.DECORATIONS).maxCount(64)));
    public static final Item FLINT_AND_STEEL_PLUS       = register("flint_and_steel_plus",      new FlintAndSteelPlusItem(new FabricItemSettings().maxDamage(64).group(ItemGroup.TOOLS)));

    public static final Item MUSIC_DISC_WOLVES          = NyakoDiscs.WOLVES.item();
    public static final Item MUSIC_DISC_MASK            = NyakoDiscs.MASK.item();
    public static final Item MUSIC_DISC_CLUNK           = NyakoDiscs.CLUNK.item();
    public static final Item MUSIC_DISC_MERRY           = NyakoDiscs.MERRY.item();
    public static final Item MUSIC_DISC_WEEZED          = NyakoDiscs.WEEZED.item();
    public static final Item MUSIC_DISC_MOONLIGHT       = NyakoDiscs.MOONLIGHT.item();
    public static final Item MUSIC_DISC_WELCOME         = NyakoDiscs.WELCOME.item();

    public static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier("nyakomod", id), item);
    }

    public static Item registerMC(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier("minecraft", id), item);
    }
}
