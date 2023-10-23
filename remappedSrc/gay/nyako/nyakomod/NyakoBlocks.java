package gay.nyako.nyakomod;

import gay.nyako.nyakomod.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
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
    public static final Block COPPER_SINGLE_COIN        = register("copper_coin",               new SingleCoinBlock(FabricBlockSettings.create(Material.METAL).strength(0.3f)));
    public static final Block GOLD_SINGLE_COIN          = register("gold_coin",                 new SingleCoinBlock(FabricBlockSettings.create(Material.METAL).strength(0.3f)));
    public static final Block DIAMOND_SINGLE_COIN       = register("diamond_coin",              new SingleCoinBlock(FabricBlockSettings.create(Material.METAL).strength(0.3f)));
    public static final Block EMERALD_SINGLE_COIN       = register("emerald_coin",              new SingleCoinBlock(FabricBlockSettings.create(Material.METAL).strength(0.3f)));
    public static final Block NETHERITE_SINGLE_COIN     = register("netherite_coin",            new SingleCoinBlock(FabricBlockSettings.create(Material.METAL).strength(0.3f)));
    public static final Block BRICKUS                   = register("brickus",                   new Block(AbstractBlock.Settings.of(Material.STONE, MapColor.RED).requiresTool().strength(2.0f, 6.0f)));
    public static final Block BRICKUS_WALL              = register("brickus_wall",              new CustomWallBlock(AbstractBlock.Settings.copy(BRICKUS)));
    public static final Block BRICKUS_SLAB              = register("brickus_slab",              new CustomSlabBlock(AbstractBlock.Settings.copy(BRICKUS)));
    public static final Block BRICKUS_STAIRS            = register("brickus_stairs",            new CustomStairsBlock(BRICKUS.getDefaultState(), AbstractBlock.Settings.copy(BRICKUS)));
    public static final Block FIREBLU                   = register("fireblu",                   new Block(FabricBlockSettings.copy(Blocks.STONE).requiresTool()));
    public static final Block TRUE_BLOCK                = register("true_block",                new SoundBlock(FabricBlockSettings.copy(Blocks.STONE).requiresTool(), NyakoSoundEvents.TRUE_BLOCK));
    public static final Block NOTE_BLOCK_PLUS           = register("note_block_plus",           new NoteBlockPlusBlock(FabricBlockSettings.copy(Blocks.NOTE_BLOCK)));
    public static final Block PRESENT_WRAPPER           = register("present_wrapper",           new PresentWrapperBlock(FabricBlockSettings.copy(Blocks.CARTOGRAPHY_TABLE)));
    public static final Block BOOKSHELF_STAIRS          = register("bookshelf_stairs",          new CustomStairsBlock(Blocks.BOOKSHELF.getDefaultState(), AbstractBlock.Settings.copy(Blocks.BOOKSHELF)));
    public static final Block GOLD_STARRY_BLOCK         = register("gold_starry_block",         new Block(FabricBlockSettings.copy(Blocks.AMETHYST_BLOCK).requiresTool()));
    public static final Block BLUE_STARRY_BLOCK         = register("blue_starry_block",         new Block(FabricBlockSettings.copy(Blocks.AMETHYST_BLOCK).requiresTool()));
    public static final Block TITANSTONE                = register("titanstone",                new Block(FabricBlockSettings.copy(Blocks.DEEPSLATE).requiresTool()));
    public static final Block ALLYBOX                   = register("allybox",                   new Block(FabricBlockSettings.copy(Blocks.OBSIDIAN).requiresTool()));
    public static final Block CREEPER                   = register("creeper",                   new CreeperBlock(FabricBlockSettings.copy(Blocks.STONE).requiresTool()));
    public static final Block WITHER                    = register("wither",                    new WitherBlock(FabricBlockSettings.copy(Blocks.STONE).requiresTool()));
    public static final Block NETHER_REACTOR_CORE       = register("nether_reactor_core",       new NetherReactorCoreBlock(FabricBlockSettings.copy(Blocks.STONE).requiresTool()));
    public static final Block GLOWING_OBSIDIAN          = register("glowing_obsidian",          new Block(FabricBlockSettings.copy(Blocks.OBSIDIAN).luminance(state -> 15).requiresTool()));
    public static final Block ECHO_PORTAL               = register("echo_portal",               new EchoPortalBlock(AbstractBlock.Settings.create(Material.PORTAL).noCollision().strength(-1.0f).sounds(BlockSoundGroup.GLASS).luminance(state -> 11)));

    public static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier("nyakomod", id), block);
    }
}
