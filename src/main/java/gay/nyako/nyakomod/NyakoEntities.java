package gay.nyako.nyakomod;

import gay.nyako.nyakomod.block.*;
import gay.nyako.nyakomod.block.custom.CustomHangingSignBlockEntity;
import gay.nyako.nyakomod.block.custom.CustomSignBlockEntity;
import gay.nyako.nyakomod.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class NyakoEntities {
    public static final BlockEntityType<BlueprintWorkbenchBlockEntity> BLUEPRINT_WORKBENCH_ENTITY = register("blueprint_workbench", FabricBlockEntityTypeBuilder.create(BlueprintWorkbenchBlockEntity::new, NyakoBlocks.BLUEPRINT_WORKBENCH).build(null));
    public static final BlockEntityType<NoteBlockPlusBlockEntity> NOTE_BLOCK_PLUS_ENTITY = register("note_block_plus", FabricBlockEntityTypeBuilder.create(NoteBlockPlusBlockEntity::new, NyakoBlocks.NOTE_BLOCK_PLUS).build(null));
    public static final BlockEntityType<PresentWrapperBlockEntity> PRESENT_WRAPPER_ENTITY = register("present_wrapper", FabricBlockEntityTypeBuilder.create(PresentWrapperBlockEntity::new, NyakoBlocks.PRESENT_WRAPPER).build(null));
    public static final BlockEntityType<SmithingTableBlockEntity> SMITHING_TABLE_ENTITY = register("smithing_table", FabricBlockEntityTypeBuilder.create(SmithingTableBlockEntity::new, Blocks.SMITHING_TABLE).build(null));
    public static final BlockEntityType<NetherReactorCoreBlockEntity> NETHER_REACTOR_ENTITY = register("nether_reactor", FabricBlockEntityTypeBuilder.create(NetherReactorCoreBlockEntity::new, NyakoBlocks.NETHER_REACTOR_CORE).build(null));
    public static final BlockEntityType<CustomSignBlockEntity> CUSTOM_SIGN_BLOCK_ENTITY = register("custom_sign", FabricBlockEntityTypeBuilder.create(CustomSignBlockEntity::new, NyakoBlocks.ECHO_SIGN, NyakoBlocks.ECHO_WALL_SIGN, NyakoBlocks.BENTHIC_SIGN, NyakoBlocks.BENTHIC_WALL_SIGN).build(null));
    public static final BlockEntityType<CustomHangingSignBlockEntity> CUSTOM_HANGING_SIGN_BLOCK_ENTITY = register("custom_hanging_sign", FabricBlockEntityTypeBuilder.create(CustomHangingSignBlockEntity::new, NyakoBlocks.ECHO_HANGING_SIGN, NyakoBlocks.ECHO_WALL_HANGING_SIGN, NyakoBlocks.BENTHIC_HANGING_SIGN, NyakoBlocks.BENTHIC_WALL_HANGING_SIGN).build(null));
    public static final BlockEntityType<FanBlockEntity> FAN_BLOCK_ENTITY = register("fan", FabricBlockEntityTypeBuilder.create(FanBlockEntity::new, NyakoBlocks.FAN).build(null));

    public static final EntityType<TickerEntity> TICKER = register("ticker", FabricEntityTypeBuilder.create(SpawnGroup.MISC, TickerEntity::new).dimensions(EntityDimensions.fixed(1F, 1F)).build());
    public static final EntityType<PetSpriteEntity> PET_SPRITE = register("petsprite", FabricEntityTypeBuilder.create(SpawnGroup.MISC, PetSpriteEntity::new).dimensions(EntityDimensions.changing(0.1f, 0.9f)).trackRangeBlocks(32).build());
    public static final EntityType<PetDragonEntity> PET_DRAGON = register("petdragon", FabricEntityTypeBuilder.create(SpawnGroup.MISC, PetDragonEntity::new).dimensions(EntityDimensions.changing(0.6f, 1f)).trackRangeBlocks(32).build());
    public static final EntityType<MonitorEntity> MONITOR = register("monitor", FabricEntityTypeBuilder.<MonitorEntity>create(SpawnGroup.MISC, MonitorEntity::new).dimensions(EntityDimensions.fixed(1f, 1f)).build());
    public static final EntityType<NetherPortalProjectileEntity> NETHER_PORTAL = register("nether_portal", FabricEntityTypeBuilder.<NetherPortalProjectileEntity>create(SpawnGroup.MISC, NetherPortalProjectileEntity::new).dimensions(EntityDimensions.fixed(0.25f, 0.25f)).trackRangeBlocks(4).trackedUpdateRate(10).build());

    public static final EntityType<HerobrineEntity> HEROBRINE = register("herobrine", FabricEntityTypeBuilder.<HerobrineEntity>create(SpawnGroup.MONSTER, HerobrineEntity::new).dimensions(EntityDimensions.changing(0.6f, 1.95f)).trackRangeBlocks(32).build());
    public static final EntityType<DecayedEntity> DECAYED = register("decayed", FabricEntityTypeBuilder.<DecayedEntity>create(SpawnGroup.MONSTER, DecayedEntity::new).dimensions(EntityDimensions.changing(0.6f, 1.95f)).trackRangeBlocks(32).build());
    public static final EntityType<ObsidianArrowEntity> OBSIDIAN_ARROW = register("obsidian_arrow", FabricEntityTypeBuilder.<ObsidianArrowEntity>create(SpawnGroup.MISC, ObsidianArrowEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static <T extends Entity> EntityType<T> register(String name, EntityType<T> entityType) {
        return Registry.register(Registries.ENTITY_TYPE, new Identifier("nyakomod", name), entityType);
    }

    public static <T extends BlockEntityType<?>> T register(String name, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier("nyakomod", name), blockEntityType);
    }
}
