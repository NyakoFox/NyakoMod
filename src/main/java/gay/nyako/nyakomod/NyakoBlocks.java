package gay.nyako.nyakomod;

import gay.nyako.nyakomod.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.AxeItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Colors;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class NyakoBlocks {
    public static final Block SPUNCH_BLOCK              = register("spunch_block",              new SoundBlock(FabricBlockSettings.copy(Blocks.STONE).sounds(NyakoSoundEvents.SPUNCH_BLOCK_SOUND_GROUP).requiresTool(), NyakoSoundEvents.SPUNCH_BLOCK));
    public static final Block LAUNCHER                  = register("launcher",                  new LauncherBlock(FabricBlockSettings.copy(Blocks.STONE).requiresTool()));
    public static final Block MAIN_SHOP                 = register("main_shop",                 new ShopBlock(new Identifier("nyakomod", "main")));
    public static final Block BLUEPRINT_WORKBENCH       = register("blueprint_workbench",       new BlueprintWorkbenchBlock(FabricBlockSettings.copy(Blocks.CARTOGRAPHY_TABLE)));
    public static final Block MATTER_VORTEX             = register("matter_vortex",             new MatterVortexBlock(FabricBlockSettings.copy(Blocks.STONE).requiresTool()));
    public static final Block DRAFTING_TABLE            = register("drafting_table",            new DraftingTableBlock(FabricBlockSettings.copy(Blocks.CARTOGRAPHY_TABLE)));
    public static final Block PLASTEEL_CASING           = register("plasteel_casing",           new Block(FabricBlockSettings.copy(Blocks.COPPER_BLOCK).requiresTool()));
    public static final Block PLASTEEL_SMOOTH_CASING    = register("plasteel_smooth_casing",    new Block(FabricBlockSettings.copy(Blocks.COPPER_BLOCK).requiresTool()));
    public static final Block PLASTEEL_PLATING          = register("plasteel_plating",          new Block(FabricBlockSettings.copy(Blocks.COPPER_BLOCK).requiresTool()));
    public static final Block PLASTEEL_PILLAR           = register("plasteel_pillar",           new PillarBlock(FabricBlockSettings.copy(Blocks.COPPER_BLOCK).requiresTool()));
    public static final Block COPPER_SINGLE_COIN        = register("copper_coin",               new SingleCoinBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK).strength(0.3f)));
    public static final Block GOLD_SINGLE_COIN          = register("gold_coin",                 new SingleCoinBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK).strength(0.3f)));
    public static final Block DIAMOND_SINGLE_COIN       = register("diamond_coin",              new SingleCoinBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK).strength(0.3f)));
    public static final Block EMERALD_SINGLE_COIN       = register("emerald_coin",              new SingleCoinBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK).strength(0.3f)));
    public static final Block NETHERITE_SINGLE_COIN     = register("netherite_coin",            new SingleCoinBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK).strength(0.3f)));
    public static final Block BRICKUS                   = register("brickus",                   new Block(FabricBlockSettings.create().mapColor(DyeColor.RED).requiresTool().strength(2.0f, 6.0f)));
    public static final Block BRICKUS_WALL              = register("brickus_wall",              new CustomWallBlock(FabricBlockSettings.copy(BRICKUS)));
    public static final Block BRICKUS_SLAB              = register("brickus_slab",              new CustomSlabBlock(FabricBlockSettings.copy(BRICKUS)));
    public static final Block BRICKUS_STAIRS            = register("brickus_stairs",            new CustomStairsBlock(BRICKUS.getDefaultState(), FabricBlockSettings.copy(BRICKUS)));
    public static final Block FIREBLU                   = register("fireblu",                   new Block(FabricBlockSettings.copy(Blocks.STONE).requiresTool()));
    public static final Block TRUE_BLOCK                = register("true_block",                new SoundBlock(FabricBlockSettings.copy(Blocks.STONE).requiresTool(), NyakoSoundEvents.TRUE_BLOCK));
    public static final Block NOTE_BLOCK_PLUS           = register("note_block_plus",           new NoteBlockPlusBlock(FabricBlockSettings.copy(Blocks.NOTE_BLOCK)));
    public static final Block PRESENT_WRAPPER           = register("present_wrapper",           new PresentWrapperBlock(FabricBlockSettings.copy(Blocks.CARTOGRAPHY_TABLE)));
    public static final Block BOOKSHELF_STAIRS          = register("bookshelf_stairs",          new CustomStairsBlock(Blocks.BOOKSHELF.getDefaultState(), FabricBlockSettings.copy(Blocks.BOOKSHELF)));
    public static final Block GOLD_STARRY_BLOCK         = register("gold_starry_block",         new Block(FabricBlockSettings.copy(Blocks.AMETHYST_BLOCK).requiresTool()));
    public static final Block BLUE_STARRY_BLOCK         = register("blue_starry_block",         new Block(FabricBlockSettings.copy(Blocks.AMETHYST_BLOCK).requiresTool()));
    public static final Block TITANSTONE                = register("titanstone",                new Block(FabricBlockSettings.copy(Blocks.DEEPSLATE).requiresTool()));
    public static final Block ALLYBOX                   = register("allybox",                   new Block(FabricBlockSettings.copy(Blocks.OBSIDIAN).requiresTool()));
    public static final Block CREEPER                   = register("creeper",                   new CreeperBlock(FabricBlockSettings.copy(Blocks.STONE).requiresTool()));
    public static final Block WITHER                    = register("wither",                    new WitherBlock(FabricBlockSettings.copy(Blocks.STONE).requiresTool()));
    public static final Block NETHER_REACTOR_CORE       = register("nether_reactor_core",       new NetherReactorCoreBlock(FabricBlockSettings.copy(Blocks.STONE).requiresTool()));
    public static final Block GLOWING_OBSIDIAN          = register("glowing_obsidian",          new Block(FabricBlockSettings.copy(Blocks.OBSIDIAN).luminance(state -> 15).requiresTool()));
    public static final Block ECHO_PORTAL               = register("echo_portal",               new EchoPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).noCollision().strength(-1.0f).sounds(BlockSoundGroup.GLASS).luminance(state -> 11)));

    public static final Block ECHO_DIRT                 = register("echo_dirt",                 new Block(FabricBlockSettings.copy(Blocks.DIRT)));
    public static final Block ECHO_STONE                = register("echo_stone",                new Block(FabricBlockSettings.copy(Blocks.STONE).requiresTool()));
    public static final Block ECHO_GROWTH               = register("echo_growth",               new Block(FabricBlockSettings.copy(Blocks.GRASS_BLOCK)));
    public static final Block ECHO_SLATE                = register("echo_slate",                new Block(FabricBlockSettings.copy(Blocks.DEEPSLATE).requiresTool()));

    public static final Block ECHO_SPINE                = register("echo_spine",                Blocks.createNetherStemBlock(MapColor.DARK_AQUA));
    public static final Block STRIPPED_ECHO_SPINE       = register("stripped_echo_spine",       Blocks.createNetherStemBlock(MapColor.DARK_AQUA));
    public static final Block ECHO_SPUR                 = register("echo_spur",                 new PillarBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA).instrument(Instrument.BASS).strength(2.0f).sounds(BlockSoundGroup.NETHER_STEM)));
    public static final Block STRIPPED_ECHO_SPUR        = register("stripped_echo_spur",        new PillarBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA).instrument(Instrument.BASS).strength(2.0f).sounds(BlockSoundGroup.NETHER_STEM)));
    public static final Block ECHO_PLANKS               = register("echo_planks",               new Block(FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA).instrument(Instrument.BASS).strength(2.0f, 3.0f).sounds(BlockSoundGroup.NETHER_WOOD)));
    public static final Block ECHO_SLAB                 = register("echo_slab",                 new SlabBlock(FabricBlockSettings.create().mapColor(ECHO_PLANKS.getDefaultMapColor()).instrument(Instrument.BASS).strength(2.0f, 3.0f).sounds(BlockSoundGroup.NETHER_WOOD)));
    public static final Block ECHO_PRESSURE_PLATE       = register("echo_pressure_plate",       new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.create().mapColor(ECHO_PLANKS.getDefaultMapColor()).solid().instrument(Instrument.BASS).noCollision().strength(0.5f).pistonBehavior(PistonBehavior.DESTROY), NyakoBlockSetTypes.ECHO));
    public static final Block ECHO_FENCE                = register("echo_fence",                new FenceBlock(FabricBlockSettings.create().mapColor(ECHO_PLANKS.getDefaultMapColor()).instrument(Instrument.BASS).strength(2.0f, 3.0f).sounds(BlockSoundGroup.NETHER_WOOD)));
    public static final Block ECHO_TRAPDOOR             = register("echo_trapdoor",             new TrapdoorBlock(FabricBlockSettings.create().mapColor(ECHO_PLANKS.getDefaultMapColor()).instrument(Instrument.BASS).strength(3.0f).nonOpaque().allowsSpawning(Blocks::never), NyakoBlockSetTypes.ECHO));
    public static final Block ECHO_FENCE_GATE           = register("echo_fence_gate",           new FenceGateBlock(FabricBlockSettings.create().mapColor(ECHO_PLANKS.getDefaultMapColor()).solid().instrument(Instrument.BASS).strength(2.0f, 3.0f), NyakoWoodTypes.ECHO));
    public static final Block ECHO_STAIRS               = register("echo_stairs",               new StairsBlock(ECHO_PLANKS.getDefaultState(), FabricBlockSettings.copy(ECHO_PLANKS)));
    public static final Block ECHO_BUTTON               = register("echo_button",               Blocks.createWoodenButtonBlock(NyakoBlockSetTypes.ECHO, new FeatureFlag[0]));
    public static final Block ECHO_DOOR                 = register("echo_door",                 new DoorBlock(FabricBlockSettings.create().mapColor(ECHO_PLANKS.getDefaultMapColor()).instrument(Instrument.BASS).strength(3.0f).nonOpaque().pistonBehavior(PistonBehavior.DESTROY), NyakoBlockSetTypes.ECHO));
    public static final Block ECHO_SIGN                 = register("echo_sign",                 new CustomSignBlock(FabricBlockSettings.create().mapColor(ECHO_PLANKS.getDefaultMapColor()).instrument(Instrument.BASS).solid().noCollision().strength(1.0f), NyakoWoodTypes.ECHO));
    public static final Block ECHO_WALL_SIGN            = register("echo_wall_sign",            new CustomWallSignBlock(FabricBlockSettings.create().mapColor(ECHO_PLANKS.getDefaultMapColor()).instrument(Instrument.BASS).solid().noCollision().strength(1.0f).dropsLike(ECHO_SIGN), NyakoWoodTypes.ECHO));
    public static final Block ECHO_HANGING_SIGN         = register("echo_hanging_sign",         new CustomHangingSignBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA).solid().instrument(Instrument.BASS).noCollision().strength(1.0f), NyakoWoodTypes.ECHO));
    public static final Block ECHO_WALL_HANGING_SIGN    = register("echo_wall_hanging_sign",    new CustomWallHangingSignBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA).solid().instrument(Instrument.BASS).noCollision().strength(1.0f).dropsLike(ECHO_HANGING_SIGN), NyakoWoodTypes.ECHO));

    public static Block register(String id, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier("nyakomod", id), block);
    }
}
