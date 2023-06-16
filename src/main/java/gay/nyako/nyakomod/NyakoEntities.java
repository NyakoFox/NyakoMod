package gay.nyako.nyakomod;

import gay.nyako.nyakomod.block.BlueprintWorkbenchBlockEntity;
import gay.nyako.nyakomod.block.NoteBlockPlusBlockEntity;
import gay.nyako.nyakomod.block.PresentWrapperBlockEntity;
import gay.nyako.nyakomod.block.SmithingTableBlockEntity;
import gay.nyako.nyakomod.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NyakoEntities {
    public static final BlockEntityType<BlueprintWorkbenchBlockEntity> BLUEPRINT_WORKBENCH_ENTITY = register("blueprint_workbench", FabricBlockEntityTypeBuilder.create(BlueprintWorkbenchBlockEntity::new, NyakoBlocks.BLUEPRINT_WORKBENCH).build(null));
    public static final BlockEntityType<NoteBlockPlusBlockEntity> NOTE_BLOCK_PLUS_ENTITY = register("note_block_plus", FabricBlockEntityTypeBuilder.create(NoteBlockPlusBlockEntity::new, NyakoBlocks.NOTE_BLOCK_PLUS).build(null));
    public static final BlockEntityType<PresentWrapperBlockEntity> PRESENT_WRAPPER_ENTITY = register("present_wrapper", FabricBlockEntityTypeBuilder.create(PresentWrapperBlockEntity::new, NyakoBlocks.PRESENT_WRAPPER).build(null));
    public static final BlockEntityType<SmithingTableBlockEntity> SMITHING_TABLE_ENTITY = register("smithing_table", FabricBlockEntityTypeBuilder.create(SmithingTableBlockEntity::new, Blocks.SMITHING_TABLE).build(null));
    public static final EntityType<TickerEntity> TICKER = register("ticker", FabricEntityTypeBuilder.create(SpawnGroup.MISC, TickerEntity::new).dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build());
    public static final EntityType<PetSpriteEntity> PET_SPRITE = register("petsprite", FabricEntityTypeBuilder.create(SpawnGroup.MISC, PetSpriteEntity::new).dimensions(EntityDimensions.changing(0.1f, 0.9f)).trackRangeBlocks(32).build());
    public static final EntityType<PetDragonEntity> PET_DRAGON = register("petdragon", FabricEntityTypeBuilder.create(SpawnGroup.MISC, PetDragonEntity::new).dimensions(EntityDimensions.changing(0.6f, 1f)).trackRangeBlocks(32).build());
    public static final EntityType<MonitorEntity> MONITOR = register("monitor", FabricEntityTypeBuilder.<MonitorEntity>create(SpawnGroup.MISC, MonitorEntity::new).dimensions(EntityDimensions.fixed(1f, 1f)).build());
    public static final EntityType<NetherPortalProjectileEntity> NETHER_PORTAL = register("nether_portal", FabricEntityTypeBuilder.<NetherPortalProjectileEntity>create(SpawnGroup.MISC, NetherPortalProjectileEntity::new).dimensions(EntityDimensions.fixed(0.25f, 0.25f)).trackRangeBlocks(4).trackedUpdateRate(10).build());

    public static <T extends Entity> EntityType<T> register(String name, EntityType<T> entityType) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier("nyakomod", name), entityType);
    }

    public static <T extends BlockEntityType<?>> T register(String name, T blockEntityType) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("nyakomod", name), blockEntityType);
    }
}
