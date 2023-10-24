package gay.nyako.nyakomod;

import gay.nyako.nyakomod.entity.PetDragonEntity;
import gay.nyako.nyakomod.item.*;
import gay.nyako.nyakomod.item.gacha.DiscordGachaItem;
import gay.nyako.nyakomod.item.gacha.GachaItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.Arrays;

public class NyakoItems {

    public static final Item SPUNCH_BLOCK               = register("spunch_block",              new BlockItem(NyakoBlocks.SPUNCH_BLOCK, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item LAUNCHER                   = register("launcher",                  new BlockItem(NyakoBlocks.LAUNCHER, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item BRICKUS                    = register("brickus",                   new BlockItem(NyakoBlocks.BRICKUS, new FabricItemSettings()));
    public static final Item BRICKUS_STAIRS             = register("brickus_stairs",            new BlockItem(NyakoBlocks.BRICKUS_STAIRS, new FabricItemSettings()));
    public static final Item BRICKUS_SLAB               = register("brickus_slab",              new BlockItem(NyakoBlocks.BRICKUS_SLAB, new FabricItemSettings()));
    public static final Item BRICKUS_WALL               = register("brickus_wall",              new BlockItem(NyakoBlocks.BRICKUS_WALL, new FabricItemSettings()));
    public static final Item MAIN_SHOP                  = register("main_shop",                 new BlockItem(NyakoBlocks.MAIN_SHOP, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item DRAFTING_TABLE             = register("drafting_table",            new BlockItem(NyakoBlocks.DRAFTING_TABLE, new FabricItemSettings()));
    public static final Item PLASTEEL_CASING            = register("plasteel_casing",           new BlockItem(NyakoBlocks.PLASTEEL_CASING, new FabricItemSettings()));
    public static final Item PLASTEEL_SMOOTH_CASING     = register("plasteel_smooth_casing",    new BlockItem(NyakoBlocks.PLASTEEL_SMOOTH_CASING, new FabricItemSettings()));
    public static final Item PLASTEEL_PLATING           = register("plasteel_plating",          new BlockItem(NyakoBlocks.PLASTEEL_PLATING, new FabricItemSettings()));
    public static final Item PLASTEEL_PILLAR            = register("plasteel_pillar",           new BlockItem(NyakoBlocks.PLASTEEL_PILLAR, new FabricItemSettings()));
    public static final Item FIREBLU                    = register("fireblu",                   new BlockItem(NyakoBlocks.FIREBLU, new FabricItemSettings()));
    public static final Item MATTER_VORTEX              = register("matter_vortex",             new BlockItem(NyakoBlocks.MATTER_VORTEX, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item TRUE_BLOCK                 = register("true_block",                new BlockItem(NyakoBlocks.TRUE_BLOCK, new FabricItemSettings()));

    public static final Item DRIP_JACKET                = register("drip_jacket",               new ArmorItem(NyakoMod.DRIP_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings().fireproof()));
    public static final Item STAFF_OF_SMITING           = register("staff_of_smiting",          new StaffOfSmitingItem(new FabricItemSettings().maxCount(1).fireproof().rarity(Rarity.RARE)));
    public static final Item PRESENT                    = register("present",                   new PresentItem(new FabricItemSettings().maxCount(1)));
    public static final Item CUSTOM_ITEM                = register("custom",                    new CustomItem(new FabricItemSettings().maxCount(69).rarity(Rarity.EPIC)));
    public static final Item DEV_NULL                   = register("dev_null",                  new DevNullItem(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item RETENTIVE_BALL             = register("retentive_ball",            new RetentiveBallItem(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item BLUEPRINT_WORKBENCH        = register("blueprint_workbench",       new BlockItem(NyakoBlocks.BLUEPRINT_WORKBENCH, new Item.Settings()));
    public static final Item COPPER_COIN                = register("copper_coin",               new CoinItem(new FabricItemSettings().maxCount(100), NyakoBlocks.COPPER_SINGLE_COIN, "copper", 1));
    public static final Item GOLD_COIN                  = register("gold_coin",                 new CoinItem(new FabricItemSettings().maxCount(100), NyakoBlocks.GOLD_SINGLE_COIN, "gold", 100));
    public static final Item EMERALD_COIN               = register("emerald_coin",              new CoinItem(new FabricItemSettings().maxCount(100), NyakoBlocks.EMERALD_SINGLE_COIN, "emerald", 10000));
    public static final Item DIAMOND_COIN               = register("diamond_coin",              new CoinItem(new FabricItemSettings().maxCount(100), NyakoBlocks.DIAMOND_SINGLE_COIN, "diamond", 1000000));
    public static final Item NETHERITE_COIN             = register("netherite_coin",            new CoinItem(new FabricItemSettings().maxCount(100).fireproof(), NyakoBlocks.NETHERITE_SINGLE_COIN, "netherite", 100000000));
    public static final Item BAG_OF_COINS               = register("bag_of_coins",              new BagOfCoinsItem(new FabricItemSettings().maxCount(1)));
    public static final Item HUNGRY_BAG_OF_COINS        = register("hungry_bag_of_coins",       new BagOfCoinsItem(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TIME_IN_A_BOTTLE           = register("time_in_a_bottle",          new TimeInABottleItem(new FabricItemSettings().maxCount(1)));
    public static final Item SOUL_JAR                   = register("soul_jar",                  new SoulJarItem(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TEST_ITEM                  = register("test_item",                 new TestItem(new FabricItemSettings().maxCount(1)));
    public static final Item BLUEPRINT                  = register("blueprint",                 new BlueprintItem(new FabricItemSettings().maxCount(16)));
    public static final Item DIAMOND_GACHA              = register("diamond_gacha",             new GachaItem(new FabricItemSettings().food(FoodComponents.GOLDEN_CARROT), 2, Arrays.asList((MutableText) Text.of("You can't make tools out of these,"), (MutableText) Text.of("but at least they're healthy!"))));
    public static final Item MARIO_GACHA                = register("mario_gacha",               new GachaItem(new FabricItemSettings(), 4, (MutableText) Text.of("The lovable plumber!")));
    public static final Item LUIGI_GACHA                = register("luigi_gacha",               new GachaItem(new FabricItemSettings(), 4, (MutableText) Text.of("The lovable plumber's brother!")));
    public static final Item DISCORD_GACHA              = register("discord_gacha",             new DiscordGachaItem(new FabricItemSettings()));
    public static final Item STAFF_OF_VORBULATION       = register("staff_of_vorbulation",      new StaffOfVorbulationItem(new FabricItemSettings().maxCount(1).fireproof().rarity(Rarity.EPIC)));
    public static final Item PET_SPRITE_SUMMON          = register("pet_sprite_summon",         new PetSpriteSummonItem(new FabricItemSettings().maxCount(1).fireproof()));
    public static final Item PET_DRAGON_SUMMON          = register("pet_dragon_summon",         new PetChangeSummonItem<>(new FabricItemSettings().maxCount(1).fireproof(), NyakoEntities.PET_DRAGON, PetDragonEntity::createPet)
            .addVariation(Text.literal("Red Dragon"), "dragonpet_red")
            .addVariation(Text.literal("Lavender Dragon"), "dragonpet_lavender")
            .addVariation(Text.literal("Blue Dragon"), "dragonpet_blue")
            .addVariation(Text.literal("Green Dragon"), "dragonpet_green")
            .addVariation(Text.literal("Flame Dragon"), "dragonpet_flame")
            .addVariation(Text.literal("Gray Dragon"), "dragonpet_gray")
            .addVariation(Text.literal("Golden Dragon"), "dragonpet_golden")
            .addVariation(Text.literal("Diamond Dragon"), "dragonpet_diamond")
            .addVariation(Text.literal("Ashen Dragon"), "dragonpet_ashen")
            .addVariation(Text.literal("Trans Dragon"), "dragonpet_trans")
    );
    public static final Item PIAMOND_DICKAXE            = register("piamond_dickaxe",           new PiamondDickaxeItem(NyakoToolMaterials.DIAMOND_WOOD, 1, -2.8f, new FabricItemSettings().maxCount(1)));
    public static final Item TWO_TALL                   = register("two_tall",                  new Item(new FabricItemSettings().maxCount(1)));
    public static final Item NETHER_PORTAL              = registerMC("nether_portal",           new BlockItem(Blocks.NETHER_PORTAL, new FabricItemSettings()));
    public static final Item FIRE                       = registerMC("fire",                    new BlockItem(Blocks.FIRE,          new FabricItemSettings()));
    public static final Item SOUL_FIRE                  = registerMC("soul_fire",               new BlockItem(Blocks.SOUL_FIRE,     new FabricItemSettings()));
    public static final Item WATER                      = registerMC("water",                   new BlockItem(Blocks.WATER,         new FabricItemSettings()));
    public static final Item LAVA                       = registerMC("lava",                    new BlockItem(Blocks.LAVA,          new FabricItemSettings()));
    public static final Item NOTE_BLOCK_PLUS            = register("note_block_plus",           new BlockItem(NyakoBlocks.NOTE_BLOCK_PLUS, new FabricItemSettings()));
    public static final Item PRESENT_WRAPPER            = register("present_wrapper",           new BlockItem(NyakoBlocks.PRESENT_WRAPPER, new FabricItemSettings()));
    public static final Item NETHER_PORTAL_BUCKET       = register("nether_portal_bucket",      new NetherPortalBucketItem(new FabricItemSettings().maxCount(1)));
    public static final Item NETHER_PORTAL_STRUCTURE    = register("nether_portal_structure",   new Item(new FabricItemSettings().maxCount(1)));
    public static final Item LEFT_DIAMOND               = register("left_diamond",              new Item(new FabricItemSettings()));
    public static final Item RIGHT_DIAMOND              = register("right_diamond",             new Item(new FabricItemSettings()));
    public static final Item BOOKSHELF_STAIRS           = register("bookshelf_stairs",          new BlockItem(NyakoBlocks.BOOKSHELF_STAIRS, new FabricItemSettings()));
    public static final Item GOLD_STARRY_BLOCK          = register("gold_starry_block",         new BlockItem(NyakoBlocks.GOLD_STARRY_BLOCK, new FabricItemSettings()));
    public static final Item BLUE_STARRY_BLOCK          = register("blue_starry_block",         new BlockItem(NyakoBlocks.BLUE_STARRY_BLOCK, new FabricItemSettings()));
    public static final Item TITANSTONE                 = register("titanstone",                new BlockItem(NyakoBlocks.TITANSTONE, new FabricItemSettings()));
    public static final Item ALLYBOX                    = register("allybox",                   new BlockItem(NyakoBlocks.ALLYBOX, new FabricItemSettings()));
    public static final Item DRAGON_MILK_BUCKET         = register("dragon_milk_bucket",        new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item MONITOR                    = register("monitor",                   new MonitorItem(new FabricItemSettings().maxCount(64)));
    public static final Item FLINT_AND_STEEL_PLUS       = register("flint_and_steel_plus",      new FlintAndSteelPlusItem(new FabricItemSettings().maxDamage(16)));
    public static final Item CREEPER                    = register("creeper",                   new BlockItem(NyakoBlocks.CREEPER, new FabricItemSettings()));
    public static final Item WITHER                     = register("wither",                    new BlockItem(NyakoBlocks.WITHER, new FabricItemSettings()));
    public static final Item HORSE_MILK_BUCKET          = register("horse_milk_bucket",         new Item(new FabricItemSettings().maxCount(1)));
    public static final Item MAGNET                     = register("magnet",                    new MagnetItem(new FabricItemSettings().maxDamage(60 * 4)));
    public static final Item ENCUMBERING_STONE          = register("encumbering_stone",         new EncumberingStoneItem(new FabricItemSettings().maxCount(1), true));
    public static final Item SUPER_ENCUMBERING_STONE    = register("super_encumbering_stone",   new EncumberingStoneItem(new FabricItemSettings().maxCount(1), false));
    public static final Item DRAGON_SCALE               = register("dragon_scale",              new DragonScaleItem(new FabricItemSettings()));
    public static final Item JEAN_JACKET                = register("jean_jacket",               new ArmorItem(NyakoMod.JEAN_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().fireproof()));
    public static final Item JEAN_HAT                   = register("jean_hat",                  new ArmorItem(NyakoMod.JEAN_ARMOR_MATERIAL, ArmorItem.Type.HELMET,  new FabricItemSettings().fireproof()));
    public static final Item JEANS                      = register("jeans",                     new ArmorItem(NyakoMod.JEAN_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,  new FabricItemSettings().fireproof()));
    public static final Item JEAN_SOCKS                 = register("jean_socks",                new ArmorItem(NyakoMod.JEAN_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,  new FabricItemSettings().fireproof()));
    public static final Item TOTEM_OF_DYING             = register("totem_of_dying",            new TotemOfDyingItem(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item FOAM_ZOMBIE                = register("foam_zombie",               new FoamZombieItem(new FabricItemSettings()));
    public static final Item GROWN_FOAM_ZOMBIE          = register("grown_foam_zombie",         new GrownFoamZombieItem(new FabricItemSettings()));
    public static final Item MEGA_DIAMOND_PICKAXE       = register("mega_diamond_pickaxe",      new MegaDiamondPickaxeItem(new FabricItemSettings()));
    public static final Item SMITHING_HAMMER            = register("smithing_hammer",           new SmithingHammerItem(new FabricItemSettings().maxDamage(250)));
    public static final Item ROD_OF_DISCORD             = register("rod_of_discord",            new RodOfDiscordItem(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item DIAMOND_APPLE              = register("diamond_apple",             new Item(new FabricItemSettings().rarity(Rarity.RARE).food(NyakoFoodComponents.DIAMOND_APPLE)));
    public static final Item EMERALD_APPLE              = register("emerald_apple",             new Item(new FabricItemSettings().rarity(Rarity.RARE).food(NyakoFoodComponents.EMERALD_APPLE)));
    public static final Item GREEN_APPLE                = register("green_apple",               new Item(new FabricItemSettings().food(FoodComponents.APPLE)));
    public static final Item SPECULAR_FISH              = register("specular_fish",             new Item(new FabricItemSettings().food(NyakoFoodComponents.SPECULAR_FISH)));
    public static final Item RECALL_POTION              = register("recall_potion",             new RecallPotionItem(new FabricItemSettings().maxCount(1)));
    public static final Item NETHER_REACTOR_CORE        = register("nether_reactor_core",       new BlockItem(NyakoBlocks.NETHER_REACTOR_CORE, new FabricItemSettings()));
    public static final Item GLOWING_OBSIDIAN           = register("glowing_obsidian",          new BlockItem(NyakoBlocks.GLOWING_OBSIDIAN, new FabricItemSettings()));
    public static final Item ENDER_GEM                  = register("ender_gem",                 new Item(new FabricItemSettings().rarity(Rarity.EPIC)));
    public static final Item ECHO_PEARL                 = register("echo_pearl",                new EchoPearlItem(new FabricItemSettings().rarity(Rarity.EPIC).maxCount(16)));

    public static final Item MUSIC_DISC_WOLVES          = NyakoDiscs.WOLVES.item();
    public static final Item MUSIC_DISC_MASK            = NyakoDiscs.MASK.item();
    public static final Item MUSIC_DISC_CLUNK           = NyakoDiscs.CLUNK.item();
    public static final Item MUSIC_DISC_MERRY           = NyakoDiscs.MERRY.item();
    public static final Item MUSIC_DISC_WEEZED          = NyakoDiscs.WEEZED.item();
    public static final Item MUSIC_DISC_MOONLIGHT       = NyakoDiscs.MOONLIGHT.item();
    public static final Item MUSIC_DISC_WELCOME         = NyakoDiscs.WELCOME.item();
    public static final Item MUSIC_DISC_SKIBIDI         = NyakoDiscs.SKIBIDI.item();

    public static final Item ECHO_DIRT                  = register("echo_dirt",                 new BlockItem(NyakoBlocks.ECHO_DIRT, new FabricItemSettings()));
    public static final Item ECHO_STONE                 = register("echo_stone",                new BlockItem(NyakoBlocks.ECHO_STONE, new FabricItemSettings()));
    public static final Item ECHO_GROWTH                = register("echo_growth",               new BlockItem(NyakoBlocks.ECHO_GROWTH, new FabricItemSettings()));
    public static final Item ECHO_SLATE                 = register("echo_slate",                new BlockItem(NyakoBlocks.ECHO_SLATE, new FabricItemSettings()));

    public static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, new Identifier("nyakomod", id), item);
    }

    public static Item registerMC(String id, Item item) {
        return Registry.register(Registries.ITEM, new Identifier("minecraft", id), item);
    }
}
